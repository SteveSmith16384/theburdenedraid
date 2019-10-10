package com.scs.billboardfps.game.entities;

import com.scs.basicecs.AbstractEntity;
import com.scs.billboardfps.game.components.MovementData;

public class LaserBullet extends AbstractEntity {

	public LaserBullet() {
		super(LaserBullet.class.getSimpleName());
		
		this.addComponent(new MovementData(0.2f));
	}

}
