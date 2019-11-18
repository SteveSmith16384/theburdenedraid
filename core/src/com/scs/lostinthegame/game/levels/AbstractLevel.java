package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.ICollectionHandler;
import com.scs.lostinthegame.game.World;

public abstract class AbstractLevel implements ICollectionHandler {

	protected int map_width;
	protected int map_height;
	protected int playerStartMapX = -1, playerStartMapY = -1;
	protected int difficulty;
	
	public AbstractLevel(int _difficulty) {
		difficulty = _difficulty;
	}
	
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0,0,0,1);
	}

	public abstract String getInstructions(); 
	
	public abstract void load(Game game);
	
	public abstract String GetName();
	
	public void update(Game game, World world) {};
	
	public abstract void renderUI(SpriteBatch batch, BitmapFont font_white, BitmapFont font_black);

	public int getPlayerStartX() {
		return this.playerStartMapX;
	}
	
	public int getPlayerStartY() {
		return this.playerStartMapY;
	}
	
}
