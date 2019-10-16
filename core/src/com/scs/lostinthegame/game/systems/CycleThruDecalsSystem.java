package com.scs.lostinthegame.game.systems;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.game.components.HasDecal;
import com.scs.lostinthegame.game.components.HasDecalCycle;

public class CycleThruDecalsSystem extends AbstractSystem {

	public CycleThruDecalsSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return HasDecalCycle.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		HasDecalCycle hdc = (HasDecalCycle)entity.getComponent(HasDecalCycle.class);
		HasDecal hd = (HasDecal)entity.getComponent(HasDecal.class);
		
		float dt = Gdx.graphics.getDeltaTime();

		hdc.animTimer += dt;
		if(hdc.animTimer>.3f){
			hdc.animTimer-=.3f;
			hdc.decalIdx++;
			if (hdc.decalIdx >= hdc.decals.length) {
				hdc.decalIdx = 0;
			}
			hd.decal = hdc.decals[hdc.decalIdx];
			
		}
}
	

}
