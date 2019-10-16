package com.scs.lostinthegame.game.entities.minedout;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.components.PositionData;

public class Mine extends AbstractEntity {

	public Mine(int x, int y) {
		super(Mine.class.getSimpleName());

        PositionData pos = new PositionData();
        pos.position = new Vector3(x*Game.UNIT, 0, y*Game.UNIT);
        this.addComponent(pos);
        
	}

}
