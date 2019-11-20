package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.entities.Floor;
import com.scs.lostinthegame.game.entities.startlevel.StartExit;

public class IntroLevel extends AbstractLevel {

	public IntroLevel() {
		super(0);
	}


	@Override
	public void load(Game game) {
		loadMap(game);
	}


	private void loadMap(Game game) {
		this.map_width = 7;
		this.map_height = 7;

		Game.world.world = new WorldSquare[map_width][map_height];

		this.playerStartMapX = 1;
		this.playerStartMapY = 1;

		for (int z=0 ; z<map_height ; z++) {
			for (int x=0 ; x<map_width ; x++) {
				Game.world.world[x][z] = new WorldSquare();
				Game.world.world[x][z].blocked = false;
			}
		}

		//game.ecs.addEntity(new Floor("start/spectrumscreen.gif", map_width, map_height, false));
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
	}


	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font_white, BitmapFont font_black) {
		font_white.draw(batch, "10 REM Super Spectrum World", 20, Gdx.graphics.getHeight()-20);
		font_white.draw(batch, "20 REM By Stephen Carlyle-Smith", 20, Gdx.graphics.getHeight()-50);
		font_white.draw(batch, "30 REM Sfx by Shiru", 20, Gdx.graphics.getHeight()-80);
		font_white.draw(batch, "40 PRINT \"Press enter to start\"", 20, Gdx.graphics.getHeight()-110);
		font_white.draw(batch, "C Nonsense in Basic, " + Settings.VERSION, 20, 30);
	}


	@Override
	public String GetName() {
		return "";
	}


	@Override
	public String getInstructions() {
		return "";
	}

}
