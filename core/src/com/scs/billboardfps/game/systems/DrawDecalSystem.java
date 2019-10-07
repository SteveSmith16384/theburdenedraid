package com.scs.billboardfps.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.components.HasDecal;
import com.scs.billboardfps.game.components.HasPositionAndRotation;
import com.scs.billboardfps.game.decals.ShadedGroupStrategy;

public class DrawDecalSystem extends AbstractSystem {

	private Camera camera;
	private DecalBatch batch;
	private ShadedGroupStrategy groupStrategy;
	private Vector3 tmp = new Vector3();
	
	public DrawDecalSystem(BasicECS ecs, Camera _camera) {
		super(ecs);

		camera = _camera;

		groupStrategy = new ShadedGroupStrategy(camera);
		batch = new DecalBatch(groupStrategy);
	}


	public void process() {
		Iterator<AbstractEntity> it = entities.iterator();
		while (it.hasNext()) {
			AbstractEntity entity = it.next();
			this.processEntity(entity);
		}
		batch.flush();
	}


	public void processEntity(AbstractEntity entity) {
		HasDecal hasDecal = (HasDecal)entity.getComponent(HasDecal.class);
		HasPositionAndRotation hasPosition = (HasPositionAndRotation)entity.getComponent(HasPositionAndRotation.class);
		updateTransform(camera, hasDecal, hasPosition);
		
		if(!camera.frustum.sphereInFrustum(hasPosition.position, Game.UNIT)) {
			return;
		}

		batch.add(hasDecal.decal);
	}


	private void updateTransform(Camera cam, HasDecal hasDecal, HasPositionAndRotation pos) {
		if(hasDecal.faceCamera) {
			tmp.set(cam.direction).scl(-1);
			if(!hasDecal.faceCameraTilted) {
				tmp.y = 0;
			}
			hasDecal.decal.setRotation(tmp, Vector3.Y);
			hasDecal.decal.rotateY(hasDecal.rotation);
		}else{
			hasDecal.decal.setRotationY(hasDecal.rotation);
		}

		hasDecal.decal.setPosition(pos.position);
		hasDecal.decal.translateY(.5f * Game.UNIT);

	}


	public Class<?> getEntityClass() {
		return HasDecal.class;
	}

}
