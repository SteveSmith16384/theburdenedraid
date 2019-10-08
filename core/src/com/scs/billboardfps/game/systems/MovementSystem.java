package com.scs.billboardfps.game.systems;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.components.MovementData;
import com.scs.billboardfps.game.components.PositionData;

public class MovementSystem extends AbstractSystem {

	public MovementSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return MovementData.class;
	}

	
	@Override
	public void processEntity(AbstractEntity entity) {
		PositionData pos = (PositionData)entity.getComponent(PositionData.class);
		MovementData movementData = (MovementData)entity.getComponent(MovementData.class);

		if (movementData.offset.x != 0 || movementData.offset.y != 0 || movementData.offset.z != 0) {
			this.tryMove(Game.world, pos.position, movementData.offset, movementData.size, true);
		}
	}


	/**
	 * Returns false if entity fails to move on any axis.
	 */
	private boolean tryMove(World world, Vector3 position, Vector3 moveVec, float sizeAsFracOfMapsquare, boolean doFine) {
		if (moveVec.len() <= 0) {
			return true;
		}
		
		boolean resultX = false;
		if (world.rectangleFree(position.x+moveVec.x, position.z, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
			position.x += moveVec.x;
			resultX = true;
		} else if (doFine) {
			for (int i = 0; i < 10; i++) {
				if (world.rectangleFree(position.x+moveVec.x/10f, position.z, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
					position.x += moveVec.x/10f;
					resultX = true;
				} else {
					break;
				}
			}
		}

		boolean resultZ = false;
		if (world.rectangleFree(position.x, position.z+moveVec.z, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
			position.z += moveVec.z;
			resultZ = true;
		} else if (doFine) {
			for (int i = 0; i < 10; i++) {
				if(world.rectangleFree(position.x, position.z+moveVec.z/10f, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
					position.z += moveVec.z/10f;
					resultZ = true;
				} else {
					break;
				}
			}
		}
		
		if (moveVec.y != 0) {
			position.y += moveVec.y;
		}
		
		return resultX && resultZ;
	}

}
