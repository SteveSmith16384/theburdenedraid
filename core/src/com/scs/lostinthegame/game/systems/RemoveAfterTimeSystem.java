package com.scs.lostinthegame.game.systems;

import com.badlogic.gdx.Gdx;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.game.components.RemoveAfterTimeData;

public class RemoveAfterTimeSystem extends AbstractSystem {

	public RemoveAfterTimeSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return RemoveAfterTimeData.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		RemoveAfterTimeData hdc = (RemoveAfterTimeData)entity.getComponent(RemoveAfterTimeData.class);

		float dt = Gdx.graphics.getDeltaTime();

		hdc.timeRemaining -= dt;
		if(hdc.timeRemaining <= 0) {
			entity.remove();
		}
	}


}
