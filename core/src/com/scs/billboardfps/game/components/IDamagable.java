package com.scs.billboardfps.game.components;

import com.badlogic.gdx.math.Vector3;

public interface IDamagable {

	int getHealth();
	
	void damaged(int amt, Vector3 dir);
	
}
