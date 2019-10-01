package com.scs.billboardfps.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.Audio;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entity.EntityManager;
import com.scs.billboardfps.game.levels.AbstractLevel;
import com.scs.billboardfps.game.levels.TheBurdenLair;
import com.scs.billboardfps.game.player.CameraController;
import com.scs.billboardfps.game.player.Inventory;
import com.scs.billboardfps.game.player.Player;
import com.scs.billboardfps.game.renderable.GameShaderProvider;
import com.scs.billboardfps.modules.IModule;

public class Game implements IModule {

	public static final float UNIT = 16f; // Square/box size
	
	//public static final Random random = new Random();
	public static final CollisionDetector collision = new CollisionDetector();
	public static final Art art = new Art();
	public static final Audio audio = new Audio();

	private SpriteBatch batch2d;
	private BitmapFont font;
	private ModelBatch batch;
	private ShaderProvider shaderProvider;

	private PerspectiveCamera camera;
	private FrameBuffer frameBuffer = null;

	public static Player player;
	public static World world;
	public Inventory inventory;
	public static EntityManager entityManager;

	private DecalManager decalManager;

	private static final int downscale = 1;

	private static boolean transition = true;
	private static float transitionProgress = 0f;
	//private String targetLevel = "level";
	private static boolean hasLoaded = false;

	public boolean game_over = false;
	public static boolean gameComplete = false;
	
	public static AbstractLevel gameLevel;
	
	public Game() {//int retro, int diff, int lookSens) {
		//downscale = scales[retro];
		batch2d = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font.fnt"));

		shaderProvider = new GameShaderProvider();
		batch = new ModelBatch(shaderProvider);

		camera = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 0, 10f);
		camera.lookAt(11f, 0, 10f);
		camera.near = .5f;
		camera.far = 30f * Game.UNIT;
		camera.update();

		decalManager = new DecalManager(camera);

		entityManager = new EntityManager(decalManager);
		world = new World(decalManager, entityManager);
		
		inventory = new Inventory();
		player = new Player(camera, world, inventory, 1, 4);

		gameLevel = new TheBurdenLair(this.entityManager, this.decalManager);

		frameBuffer = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth()/downscale, Gdx.graphics.getHeight()/downscale, true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
	}


	public void setSettings(int difficulty, int lookSensitivity) {
		//downscale = 1; // scales[retro];
		player.cameraController = new CameraController(camera, lookSensitivity);

		frameBuffer = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

	}

	public void resize(int w, int h) {
		// todo
	}


	public static void changeLevel(String level) {
		gameLevel.levelComplete();
		transition = true;
		transitionProgress = 0f;
		//targetLevel = level;
		hasLoaded = false;
	}


	public void update() {
		/*if(!gameComplete) {
			player.getPosition().set(world.spawnx * Game.UNIT, 0, world.spawny * Game.UNIT);
			player.cameraController.bobbing = 0;
		}*/

		if (transition) {
			transitionProgress += Gdx.graphics.getDeltaTime();

			if (transitionProgress >= 0.5f && !hasLoaded){
				gameLevel.load();
				hasLoaded = true;

				player.getPosition().set(gameLevel.getPlayerStartX()*Game.UNIT, 0, gameLevel.getPlayerStartY()*Game.UNIT);
				entityManager.update(world);
				camera.rotate(Vector3.Y, (float)Math.toDegrees(Math.atan2(camera.direction.z, camera.direction.x)));
				player.update();
				camera.update();

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
		entityManager.update(world);
		gameLevel.update(world);

		if (player.getHealth() <= 0 && !gameComplete) {
			game_over = true;
			Game.audio.play("gameover");
		}
	}


	public void render() {
		Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0,0,0,1);

		frameBuffer.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0,0,0,1);

		batch.begin(camera);
		for (int i = 0; i < world.modelInstances.size(); i++) {
			batch.render(world.modelInstances.get(i));
		}
		batch.end();

		decalManager.render();

		batch2d.begin();
		inventory.render(batch2d, player);
		player.render(batch2d);
		batch2d.end();

		frameBuffer.end();

		//Draw buffer and FPS
		batch2d.begin();

		float c = 1.0f;
		if (transition) {
			c = 1.0f - transitionProgress*4;
			if (transitionProgress>=.75f) {
				c = (transitionProgress-0.75f)*4;
			}
			c = MathUtils.clamp(c, 0, 1);
		}

		batch2d.setColor(c,c,c,1);
		batch2d.draw(frameBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), - Gdx.graphics.getHeight());

		if (!transition) {
			player.renderUI(batch2d, font, downscale);
		}

		if (Settings.SHOW_FPS) {
			font.draw(batch2d, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, 20);
		}

		batch2d.end();

	}


	public void destroy() {
		// todo
	}


	@Override
	public boolean isFinished() {
		return this.game_over;
	}


	@Override
	public void setFullScreen(boolean fullscreen) {
		if (fullscreen) {
			batch2d.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		} else {
			batch2d.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
	}

}

