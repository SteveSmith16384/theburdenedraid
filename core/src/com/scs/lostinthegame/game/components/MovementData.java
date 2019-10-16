package com.scs.lostinthegame.game.components;

import com.badlogic.gdx.math.Vector3;

public class MovementData {

	public Vector3 offset = new Vector3();
	public float sizeAsFracOfMapsquare;
	
	public MovementData(float _sizeAsFracOfMapsquare) {
		sizeAsFracOfMapsquare = _sizeAsFracOfMapsquare;
	}

}
