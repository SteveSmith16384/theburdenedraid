package net.evgiz.ld40;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;

import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.modules.IModule;

public class LudumDare40 extends ApplicationAdapter { // todo - rename

	private IModule current_module;
	private boolean toggleFullscreen = false, fullscreen = false;

	@Override
	public void create () {
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

			if(current_module.isFinished()) {
				Game.audio.stopMusic();
			}
		}

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
