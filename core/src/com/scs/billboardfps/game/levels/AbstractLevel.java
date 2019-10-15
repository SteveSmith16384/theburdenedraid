package com.scs.billboardfps.game.levels;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.ICollectionHandler;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entities.EntityManager;
import com.scs.billboardfps.game.player.weapons.IPlayersWeapon;

public abstract class AbstractLevel implements ICollectionHandler {

	protected DecalManager decalManager;
	protected EntityManager entityManager;

	protected int map_width;
	protected int map_height;
	protected int playerStartMapX = -1, playerStartMapY = -1;
	
	public AbstractLevel(EntityManager _entityManager, DecalManager _decalManager) {
		entityManager = _entityManager;
		this.decalManager = _decalManager;
	}
	
	public abstract void load(Game game);
	
	public void update(Game game, World world) {};
	
	public abstract void renderUI(SpriteBatch batch, BitmapFont font);

	public abstract IPlayersWeapon getWeapon();
	
	public int getPlayerStartX() {
		return this.playerStartMapX;
	}
	
	public int getPlayerStartY() {
		return this.playerStartMapY;
	}
	
	public abstract void levelComplete();
}
