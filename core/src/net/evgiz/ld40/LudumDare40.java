package net.evgiz.ld40;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;

import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.modules.IModule;

public class LudumDare40 extends ApplicationAdapter { // todo - rename

	//private Game game;

	//private Texture background;
	//private boolean paused = false;

	//private int gameStage = -1;
	private IModule current_module;
	private boolean toggleFullscreen = false, fullscreen = false;

	@Override
	public void create () {
		//background = new Texture(Gdx.files.internal("background.png"));
		current_module = new Game(0, 0, 1);//menu.retro, menu.difficulty, menu.lookSensitivity);
	}


	@Override
	public void render() {
		/*if (gameStage == 1) {
			//renderUpdateGameOver();
			return;
		}*/

		if (current_module != null) {
			current_module.update();
			current_module.render();

			//playTime += Gdx.graphics.getDeltaTime();

			if(current_module.isFinished()) {
				//gameStage = 1;

				Game.audio.stopMusic();

				/*int sec = (int)playTime;
				int min = sec/60;
				sec %= 60;
				min = Math.min(min, 99);
				formatPlayTime = ((min<10)?("0"+min):min)+":"+((sec<10)?("0"+sec):sec);

				loot = game.inventory.totalLoot;*/

			}
		}/* else {
			Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			Gdx.gl.glClearColor(0,0,0,1);

			menu.batch.begin();
			drawBackground();
			menu.batch.end();

			//menu.paused = paused;
			menu.update();
			menu.render();

			if(menu.startGameSelected){
				if(!paused) {
					game = new Game(menu.retro, menu.difficulty, menu.lookSensitivity);
				} else {
					game.setSettings(menu.retro, menu.difficulty, menu.lookSensitivity);
				}
				paused = false;
				Game.audio.startMusic();
			}
		}*/
		Game.audio.update();

		if (Gdx.input.isKeyPressed(Keys.F1)) {
			if (Gdx.app.getType() == ApplicationType.WebGL) {
				if (!Gdx.graphics.isFullscreen()) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayModes()[0]);
				}
			} else {
				toggleFullscreen = true;
			}
		}

		if (this.toggleFullscreen) {
			this.toggleFullscreen = false;
			if (fullscreen) {
				Gdx.graphics.setWindowedMode(Settings.WINDOW_WIDTH_PIXELS, Settings.WINDOW_HEIGHT_PIXELS);
				//batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
				fullscreen = false;
			} else {
				DisplayMode m = null;
				for(DisplayMode mode: Gdx.graphics.getDisplayModes()) {
					if (m == null) {
						m = mode;
					} else {
						if (m.width < mode.width) {
							m = mode;
						}
					}
				}

				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				//batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
				fullscreen = true;
			}
		}

	}

	private void drawBackground() {
		/*	int size = 96;
		for (int x = 0; x < Gdx.graphics.getWidth(); x+=size) {
			for (int y = Gdx.graphics.getHeight(); y > Gdx.graphics.getHeight()-size*3; y-=size) {
				menu.batch.draw(background, x, y, size, size);
			}
		}
		 */
	}


	@Override
	public void resize(int width, int height) {
		/*if(game != null) {
			game.resize(width,height);
		}*/
	}


	@Override
	public void dispose () {
		if (current_module != null) {
			current_module.destroy();
		}
	}
}
