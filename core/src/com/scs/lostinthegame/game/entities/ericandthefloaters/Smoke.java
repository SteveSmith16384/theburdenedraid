package com.scs.lostinthegame.game.entities.ericandthefloaters;

import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.components.RemoveAfterTimeData;

public class Smoke extends AbstractEntity {

	public Smoke() {
		super(Smoke.class.getSimpleName());
		
		this.addComponent(new RemoveAfterTimeData(2));
		
	}

}
