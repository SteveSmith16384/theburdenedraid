package com.scs.lostinthegame.game.systems;

import com.badlogic.gdx.math.GridPoint2;
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
		PositionData playerPosData = (PositionData)Game.player.getComponent(PositionData.class);
		GridPoint2 playerPos = new GridPoint2((int)(playerPosData.position.x/Game.UNIT), (int)(playerPosData.position.z/Game.UNIT));
		PositionData minePosData = (PositionData)entity.getComponent(PositionData.class);
		GridPoint2 minePos = new GridPoint2((int)(minePosData.position.x/Game.UNIT), (int)(minePosData.position.z/Game.UNIT));
		float dist = minePos.dst(playerPos);
		Settings.p("pos: " + playerPos.x + "," + playerPos.y + " = " + dist);
		if (dist == 0) {
			Settings.p("Player walked on mine!");
			Game.player.damaged(1, null);
		} else if (dist <= 1) {
			num_mines++;
		}
	}

}
