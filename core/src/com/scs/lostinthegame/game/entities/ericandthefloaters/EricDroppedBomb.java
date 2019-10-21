package com.scs.lostinthegame.game.entities.ericandthefloaters;

import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.components.EricAndTheFloatersBombData;

public class EricDroppedBomb extends AbstractEntity {

	public EricDroppedBomb() {
		super(EricDroppedBomb.class.getSimpleName());
		
		this.addComponent(new EricAndTheFloatersBombData(3));
		
	}

}
