package com.scs.billboardfps.game.entity.androids;

import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.entity.Entity;

public class SlidingDoor extends Entity {

    public SlidingDoor(int x, int y) {
        super(SlidingDoor.class.getSimpleName(), null, x, y, 0,1);

        decalEntity.faceCamera = false;
        decalEntity.setRotation(-90f);
    }


    @Override
	public void update(World world) {
    	// todo - open if touched, or close again
	}

}
