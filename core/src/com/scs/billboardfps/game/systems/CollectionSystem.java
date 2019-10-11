package com.scs.billboardfps.game.systems;

import java.util.Iterator;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.billboardfps.game.components.CanCollect;
import com.scs.billboardfps.game.components.IsCollectable;

public class CollectionSystem extends AbstractSystem {

	public CollectionSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return CanCollect.class;
	}
	
	
	@Override
	public void processEntity(AbstractEntity entity) {
		Iterator<AbstractEntity> it = ecs.getIterator();
		while (it.hasNext()) {
			AbstractEntity e = it.next();
			IsCollectable ic = (IsCollectable)e.getComponent(IsCollectable.class);
			if (ic != null) {
				e.remove();
			}
		}
	}
	
}
