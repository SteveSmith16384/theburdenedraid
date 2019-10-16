package com.scs.lostinthegame.game.entities;

import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.components.MovementData;

public class LaserBullet extends AbstractEntity {

	public LaserBullet() {
		super(LaserBullet.class.getSimpleName());
		
		this.addComponent(new MovementData(0.2f));
	}

}
