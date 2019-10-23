package com.scs.lostinthegame.game.systems;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.components.PositionData;
import com.scs.lostinthegame.game.components.WarnIfAdjacentData;

public class CountMinesSystem extends AbstractSystem {
	
	public int num_mines;

	public CountMinesSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return WarnIfAdjacentData.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		num_mines = 0;
		PositionData ourPos = (PositionData)entity.getComponent(PositionData.class);
		PositionData playerPos = (PositionData)Game.player.getComponent(PositionData.class);
		float dist = ourPos.position.dst(playerPos.position);
		if (dist < Game.UNIT) {
			Settings.p("Player walked on mine!");
			// todo
		} else if (dist < Game.UNIT * 2) {
			num_mines++;
		}
	}

}
