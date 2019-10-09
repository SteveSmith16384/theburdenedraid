package com.scs.billboardfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.systems.MobAISystem.Mode;

public class HasAI {

	public Vector3 direction = new Vector3();
	public float speed;
	public Mode mode;
	
	public HasAI(Mode _mode, float _speed) {
		mode = _mode;
		speed = _speed;
	}
}
