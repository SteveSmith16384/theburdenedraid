package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.entities.Floor;
import com.scs.lostinthegame.game.entities.startlevel.StartExit;

public class StartLevel extends AbstractLevel {

	public StartLevel() {
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

		game.ecs.addEntity(new Floor("start/spectrumscreen.gif", map_width, map_height, false));
		if (game.game_stage == 0) {
			game.ecs.addEntity(new StartExit(map_width/2, map_height/2));
		}
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}


	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
	}


	@Override
	public String GetName() {
		return "";
	}


	@Override
	public String getInstructions() {
		return "";
	}


	@Override
	public String getMusicFilename() {
		return "";
	}
}
