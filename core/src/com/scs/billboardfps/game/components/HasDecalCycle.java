package com.scs.billboardfps.game.components;

import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class HasDecalCycle {

	public float animTimer;
	public int decalIdx = 0;
	public Decal decals[];
	
	public HasDecalCycle(float _animTimer, int num) {
		animTimer = _animTimer;
		decals = new Decal[num];
	}

}
