package com.scs.billboardfps.game.components;

import com.badlogic.gdx.math.Vector3;

public interface IDamagable { // todo - use this

	int getHealth();
	
	void damaged(int amt, Vector3 dir);
	
}
