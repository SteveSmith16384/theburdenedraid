package com.scs.lostinthegame.game.data;

import com.scs.basicecs.AbstractEntity;

public class WorldSquare {

	public int type;
	public AbstractEntity wall;
	
	public WorldSquare() {

	}

	public WorldSquare(int _type) {
		type = _type;
	}

}
