package com.scs.lostinthegame.game.entities.minedout;

import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.components.CompletesLevelData;
import com.scs.lostinthegame.game.components.PositionData;

public class MinedOutExit extends AbstractEntity {

	public MinedOutExit(int map_x, int map_y) {
		super(MinedOutExit.class.getSimpleName());
		
		this.addComponent(new PositionData((map_x*Game.UNIT)-(Game.UNIT/2), (map_y*Game.UNIT)-(Game.UNIT/2)));

		this.addComponent(new CompletesLevelData());

	}

}
