package com.scs.billboardfps.game.levels;

import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entity.EntityManager;

public abstract class AbstractLevel {

	protected DecalManager decalManager;
	protected EntityManager entityManager;

	protected int map_width;
	protected int map_height;
	protected int playerStartMapX, playerStartMapY;
	

	public AbstractLevel(EntityManager _entityManager, DecalManager _decalManager) {
		entityManager = _entityManager;
		this.decalManager = _decalManager;
	}
	

	public abstract void levelComplete();
	
	public abstract void load(Game game);
	
	public void update(Game game, World world) {};
	
	public int getPlayerStartX() {
		return this.playerStartMapX;
	}
	
	public int getPlayerStartY() {
		return this.playerStartMapY;
	}
	
}
