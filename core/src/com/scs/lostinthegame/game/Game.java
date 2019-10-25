package com.scs.lostinthegame.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
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
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.Audio;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.components.PositionData;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.entities.TextEntity;
import com.scs.lostinthegame.game.levels.AbstractLevel;
import com.scs.lostinthegame.game.levels.AndroidsLevel;
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
	private BitmapFont font;
	private ModelBatch batch;
	private PerspectiveCamera camera;
	private FrameBuffer frameBuffer = null;

	public static Player player;
	public static World world;
	public Inventory inventory;
	public static EntityManager entityManager; // This is slowly being removed, to be replaced by BasicECS
	public static BasicECS ecs;
	public ArrayList<ModelInstance> modelInstances;

	private DecalManager decalManager;

	private static boolean transition = true;
	private static float transitionProgress = 0f;
	private static boolean hasLoaded = false;
	public boolean game_over = false;
	public static boolean gameComplete = false;
	public static boolean levelComplete = false;
	public static boolean restartLevel = false;
	private Levels levels = new Levels();
	public static AbstractLevel gameLevel;


	public Game() {
		batch2d = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font/spectrum1white.fnt"));

		batch = new ModelBatch(new GameShaderProvider());

		camera = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 0, 10f);
		camera.lookAt(11f, 0, 10f);
		camera.near = .5f;
		camera.far = 30f * Game.UNIT;
		camera.update();

		decalManager = new DecalManager(camera);

		entityManager = new EntityManager(decalManager);

		world = new World();

		inventory = new Inventory();

		player = new Player(camera, inventory, 1, 4);//, gameLevel.getWeapon());

		frameBuffer = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		levelComplete = true; // So we load the level 
		transition = true;
	}


	private void resetECS() {
		ecs = new BasicECS();
		ecs.addSystem(new DrawDecalSystem(ecs, camera));
		ecs.addSystem(new CycleThruDecalsSystem(ecs));
		ecs.addSystem(new MobAISystem(ecs));		
		ecs.addSystem(new MovementSystem(ecs));		
		ecs.addSystem(new DrawModelSystem(ecs, batch));
		ecs.addSystem(new RemoveAfterTimeSystem(ecs));
		ecs.addSystem(new CollectionSystem(ecs, gameLevel));
		ecs.addSystem(new DrawTextSystem(ecs, batch2d, font));
		ecs.addSystem(new GotToExitSystem(ecs));

		ecs.addEntity(player);

	}


	public void update() {
		/*if(!gameComplete) {
			player.getPosition().set(world.spawnx * Game.UNIT, 0, world.spawny * Game.UNIT);
			player.cameraController.bobbing = 0;
		}*/

		if (levelComplete) {
			levelComplete = false;
			levels.nextLevel();
			restartLevel = true;
			Game.audio.play("beepfx_samples/19_jet_burst.wav");
		}		
		if (restartLevel) {
			restartLevel = false;
			transition = true;
			hasLoaded = false;
			transitionProgress = 0;
			//gameLevel = levels.getNextLevel(this.entityManager, this.decalManager);
			gameLevel = new AndroidsLevel(this.entityManager, this.decalManager);
			//gameLevel = new MonsterMazeLevel(this.entityManager, this.decalManager);

			this.resetECS();

			AbstractEntity text = new TextEntity("LOADING: " + gameLevel.GetName(), 30, 30, 4);
			ecs.addEntity(text);
		}

		if (transition) {
			transitionProgress += Gdx.graphics.getDeltaTime()/3f;

			if (transitionProgress >= 0.5f && !hasLoaded) {
				loadLevel();
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
		this.ecs.getSystem(CollectionSystem.class).process();
		this.ecs.getSystem(GotToExitSystem.class).process();

		entityManager.update(world);
		gameLevel.update(this, world);

		if (player.getHealth() <= 0 && !gameComplete) {
			game_over = true;
			Game.audio.play("gameover");
		}
	}


	public void render() {
		Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//Gdx.gl.glClearColor(0,0,0,1);

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

		decalManager.render();
		if (ecs != null) {
			this.ecs.getSystem(CycleThruDecalsSystem.class).process();
			this.ecs.getSystem(DrawDecalSystem.class).process();
		}
		batch2d.begin();
		inventory.render(batch2d, player);
		player.render(batch2d);
		if (ecs != null) {
			this.ecs.getSystem(DrawTextSystem.class).process();
		}
		batch2d.end();

		frameBuffer.end();

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
			player.renderUI(batch2d, font);
		}

		if (Settings.SHOW_FPS) {
			font.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, 20);
		}

		batch2d.end();

	}


	private void loadLevel() {
		entityManager.getEntities().clear();
		decalManager.clear();
		modelInstances = new ArrayList<ModelInstance>();
		gameLevel.load(this);
		
		if (gameLevel.getPlayerStartX() < 0 || gameLevel.getPlayerStartY() < 0) {
			throw new RuntimeException ("No player start position set");
		}
		PositionData posData = (PositionData)this.player.getComponent(PositionData.class);
		posData.position.set(gameLevel.getPlayerStartX()*Game.UNIT, 0, gameLevel.getPlayerStartY()*Game.UNIT);
		entityManager.update(world);
		camera.rotate(Vector3.Y, (float)Math.toDegrees(Math.atan2(camera.direction.z, camera.direction.x)));
		player.update();
		camera.update();

		hasLoaded = true;
	}


	@Override
	public void resize(int w, int h) {
	}


	public void destroy() {
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

