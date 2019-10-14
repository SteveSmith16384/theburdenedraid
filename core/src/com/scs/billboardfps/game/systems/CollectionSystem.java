package com.scs.billboardfps.game.systems;

import java.util.Iterator;

import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.ICollectionHandler;
import com.scs.billboardfps.game.components.CanCollect;
import com.scs.billboardfps.game.components.IsCollectable;
import com.scs.billboardfps.game.components.PositionData;

public class CollectionSystem extends AbstractSystem {

	private ICollectionHandler collectionHandler;
	
	public CollectionSystem(BasicECS ecs, ICollectionHandler _collectionHandler) {
		super(ecs);
		
		collectionHandler = _collectionHandler;
	}


	@Override
	public Class<?> getComponentClass() {
		return CanCollect.class;
	}


	@Override
	public void processEntity(AbstractEntity collector) {
		PositionData collector_pos = (PositionData)collector.getComponent(PositionData.class);
		Iterator<AbstractEntity> it = ecs.getIterator();
		while (it.hasNext()) {
			AbstractEntity collectable = it.next();
			IsCollectable ic = (IsCollectable)collectable.getComponent(IsCollectable.class);
			if (ic != null) {
				PositionData collectable_pos = (PositionData)collectable.getComponent(PositionData.class);
				if (collector_pos.position.dst(collectable_pos.position) < Game.UNIT/2f) {
					Settings.p(this + " collected");
					collectable.remove();
					collectionHandler.entityCollected(collector, collectable);
				}
			}
		}
	}

}
