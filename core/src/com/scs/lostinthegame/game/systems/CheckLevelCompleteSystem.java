package com.scs.lostinthegame.game.systems;

import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;

public class CheckLevelCompleteSystem extends AbstractSystem {

	public CheckLevelCompleteSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return null;
	}

}
