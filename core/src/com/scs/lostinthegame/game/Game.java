package com.scs.lostinthegame.game;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.bitfire.postprocessing.PostProcessing;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.Audio;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.components.PositionData;
import com.scs.lostinthegame.game.entities.Ceiling;
import com.scs.lostinthegame.game.entities.TextEntity;
import com.scs.lostinthegame.game.levels.AbstractLevel;
import com.scs.lostinthegame.game.levels.GameOverLevel;
import com.scs.lostinthegame.game.player.Inventory;
import com.scs.lostinthegame.game.player.Player;
import com.scs.lostinthegame.game.renderable.GameShaderProvider;
import com.scs.lostinthegame.game.systems.CollectionSystem;
import com.scs.lostinthegame.game.systems.CycleThruDecalsSystem;
import com.scs.lostinthegame.game.systems.DrawDecalSystem;
import com.scs.lostinthegame.game.systems.DrawModelSystem;
import com.scs.lostinthegame.game.systems.DrawTextSystem;
import com.scs.lostinthegame.game.systems.GotToExitSystem;
import com.scs.lostinthegame.game.systems.MobAISystem;
import com.scs.lostinthegame.game.systems.MovementSystem;
import com.scs.lostinthegame.game.systems.RemoveAfterTimeSystem;
import com.scs.lostinthegame.modules.IModule;

public class Game implements IModule {

	public static final float UNIT = 16f; // Square/box size

	public static final CollisionDetector collision = new CollisionDetector();
	public static final Art art = new Art();
	public static final Audio audio = new Audio();

	private SpriteBatch batch2d;
	private BitmapFont font_white, font_black;
	private ModelBatch batch;
	private PerspectiveCamera camera;
	private FrameBuffer frameBuffer = null;

	public Player player;
	public static World world;
	public Inventory inventory;
	public static BasicECS ecs;
	public ArrayList<ModelInstance> modelInstances;

	private static boolean transition = true;
	private static float transitionProgress = 0f;
	private static boolean hasLoaded = false;
	public boolean game_over = false;
	public static boolean gameComplete = false;
	public static boolean levelComplete = false;
	public static boolean restartLevel = false;
	public Levels levels = new Levels();
	public static AbstractLevel gameLevel;

	private PostProcessing post;

	public Game() {
		batch2d = new SpriteBatch();
		font_white = new BitmapFont(Gdx.files.internal("font/spectrum1white.fnt"));
		font_black = new BitmapFont(Gdx.files.internal("font/spectrum1black.fnt"));

		batch = new ModelBatch(new GameShaderProvider());

		camera = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 0, 10f);
		camera.lookAt(11f, 0, 10f);
		camera.near = .5f;
		camera.far = 30f * Game.UNIT;
		camera.update();

		world = new World();

		inventory = new Inventory();
		player = new Player(camera, inventory, 1, 4);

		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		levelComplete = true; // So we load the first level 
		transition = true;

		if (Gdx.app.getType() != ApplicationType.WebGL) {
			post = new PostProcessing();
		}

	}


	private void resetECS() {
		ecs = new BasicECS();
		ecs.addSystem(new DrawDecalSystem(ecs, camera));
		ecs.addSystem(new CycleThruDecalsSystem(ecs));
		ecs.addSystem(new MobAISystem(ecs, player));
		ecs.addSystem(new MovementSystem(ecs, player));
		ecs.addSystem(new DrawModelSystem(ecs, batch));
		ecs.addSystem(new RemoveAfterTimeSystem(ecs));
		ecs.addSystem(new CollectionSystem(ecs, gameLevel));
		ecs.addSystem(new DrawTextSystem(ecs, batch2d, font_white));
		ecs.addSystem(new GotToExitSystem(ecs, player));

		ecs.addEntity(player);
		player.setWeapon(null);
	}


	public void update() {
		if (Settings.RELEASE_MODE == false) {
			// Cheat mode!
			if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
				this.levelComplete = true;
				if (Settings.DEBUG_LEVEL_JUMP) {
					Settings.p("X pressed");
				}
			}
		}

		if (levelComplete) {
			if (Settings.DEBUG_LEVEL_JUMP) {
				Settings.p("levelComplete");
			}
			levelComplete = false;
			levels.nextLevel();
			restartLevel = true;
			Game.audio.play("zxspectrumloadingnoise.ogg");
		}		
		if (restartLevel) {
			/*if (Settings.DEBUG_LEVEL_JUMP) {
				Settings.p("restartLevel");
			}*/
			restartLevel = false;
			transition = true;
			hasLoaded = false;
			transitionProgress = 0;

			if (Settings.TEST_SPECIFIC_LEVEL == false) {
				if (player.getLives() >= 0) {
					gameLevel = levels.getLevel();
				} else {
					gameLevel = new GameOverLevel();
				}
			} else {
				gameLevel = new GameOverLevel();
				//gameLevel = new OhMummyLevel(0);
				//gameLevel = new GulpmanLevel(0);
				//gameLevel = new AndroidsLevel(0);
				//gameLevel = new MinedOutLevel(0);
				//gameLevel = new MonsterMazeLevel(0);
			}
			if (Settings.DEBUG_LEVEL_JUMP) {
				Settings.p("New level is " + gameLevel.getClass().getSimpleName());
			}

			this.resetECS();

			if (gameLevel.GetName().length() > 0) {
				AbstractEntity text = new TextEntity("LOADING: " + gameLevel.GetName(), 30, 30, 4);
				ecs.addEntity(text);
			}
		}

		if (transition) {
			transitionProgress += Gdx.graphics.getDeltaTime()/3f;

			if (transitionProgress >= 0.5f && !hasLoaded) {
				loadLevel();
				ecs.addEntity(new Ceiling("gamer1.jpg", -10, -10, 40, 40, false, Game.UNIT*8));

				//AbstractEntity text = new TextEntity(gameLevel.getInstructions(), Gdx.graphics.getHeight()/2, 4);
				//ecs.addEntity(text);
			}
			if (transitionProgress > 1f) {
				transitionProgress = 0;
				transition = false;
			} else {
				return;
			}
		}

		player.update();
		camera.update();

		this.ecs.getSystem(RemoveAfterTimeSystem.class).process();
		this.ecs.addAndRemoveEntities();
		this.ecs.getSystem(MobAISystem.class).process();
		this.ecs.getSystem(MovementSystem.class).process();
		//this.ecs.getSystem(HarmPlayerSystem.class).process();
		this.ecs.getSystem(CollectionSystem.class).process();
		this.ecs.getSystem(GotToExitSystem.class).process();

		gameLevel.update(this, world);

		if (player.getHealth() <= 0 && !gameComplete) {
			game_over = true;
			Game.audio.play("gameover");
		}
	}


	public void render() {
		if (Gdx.app.getType() != ApplicationType.WebGL) {
			post.update(Gdx.graphics.getDeltaTime());
		}

		Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		frameBuffer.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		this.gameLevel.setBackgroundColour();

		batch.begin(camera);
		if (modelInstances != null) {
			for (int i = 0; i < modelInstances.size(); i++) {
				batch.render(modelInstances.get(i));
			}
		}

		if (ecs != null) {
			this.ecs.getSystem(DrawModelSystem.class).process();
		}
		batch.end();

		if (ecs != null) {
			this.ecs.getSystem(CycleThruDecalsSystem.class).process();
			this.ecs.getSystem(DrawDecalSystem.class).process();
		}
		batch2d.begin();
		//inventory.render(batch2d, player);
		player.render(batch2d);
		if (ecs != null) {
			this.ecs.getSystem(DrawTextSystem.class).process();
		}
		batch2d.end();

		frameBuffer.end();
		if (Gdx.app.getType() != ApplicationType.WebGL) {
			post.begin();
		}

		//Draw buffer and FPS
		batch2d.begin();

		float c = 1.0f;
		if (transition) {
			c = 1.0f - transitionProgress*4;
			if (transitionProgress >= .75f) {
				c = (transitionProgress-0.75f)*4;
			}
			c = MathUtils.clamp(c, 0, 1);
		}

		batch2d.setColor(c,c,c,1);
		batch2d.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), - Gdx.graphics.getHeight());

		if (!transition) {
			player.renderUI(batch2d, font_white);
			gameLevel.renderUI(batch2d, font_white, font_black);
		}

		if (Settings.SHOW_FPS) {
			font_white.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, 20);
		}

		batch2d.end();
		if (Gdx.app.getType() != ApplicationType.WebGL) {
			post.end();
		}
	}


	private void loadLevel() {
		modelInstances = new ArrayList<ModelInstance>();
		gameLevel.load(this);

		if (gameLevel.getPlayerStartMapX() < 0 || gameLevel.getPlayerStartMapY() < 0) {
			throw new RuntimeException ("No player start position set");
		}
		PositionData posData = (PositionData)this.player.getComponent(PositionData.class);
		posData.position.set(gameLevel.getPlayerStartMapX()*Game.UNIT+(Game.UNIT/2), 0, gameLevel.getPlayerStartMapY()*Game.UNIT+(Game.UNIT/2)); // Start in middle of square
		camera.rotate(Vector3.Y, (float)Math.toDegrees(Math.atan2(camera.direction.z, camera.direction.x)));
		player.update();
		camera.update();

		hasLoaded = true;
	}


	@Override
	public void resize(int w, int h) {
	}


	public void destroy() {
		if (Gdx.app.getType() != ApplicationType.WebGL) {
			post.dispose();
		}
		font_white.dispose(); 
		font_black.dispose();
		audio.dipose();
		batch.dispose();
	}


	@Override
	public boolean isFinished() {
		return this.game_over;
	}


	@Override
	public void setFullScreen(boolean fullscreen) {
		batch2d.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

}

