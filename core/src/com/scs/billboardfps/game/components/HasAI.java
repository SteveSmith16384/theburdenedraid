package com.scs.billboardfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class HasAI {

	public Vector3 direction = new Vector3();
	public float speed;
	
	public HasAI(float _speed) {
		speed = _speed;
	}
}
