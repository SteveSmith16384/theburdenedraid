package com.scs.lostinthegame.game.systems;

import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.game.components.LeavesAPath;

public class LeavePathSystem extends AbstractSystem {// todo - delete?

	public LeavePathSystem(BasicECS ecs) {
		super(ecs);
	}


	public Class<?> getComponentClass() {
		return LeavesAPath.class;
	}


}
