package com.scs.lostinthegame.game.systems;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.components.AutoMove;
import com.scs.lostinthegame.game.components.HarmsNasties;
import com.scs.lostinthegame.game.components.HarmsPlayer;
import com.scs.lostinthegame.game.components.IsDamagableNasty;
import com.scs.lostinthegame.game.components.MovementData;
import com.scs.lostinthegame.game.components.PositionData;

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
		if (entity.isMarkedForRemoval()) {
			return;
		}
		
		MovementData movementData = (MovementData)entity.getComponent(MovementData.class);
		movementData.hitWall = false;
		
		AutoMove auto = (AutoMove)entity.getComponent(AutoMove.class);
		if (auto != null) {
			movementData.offset = auto.dir.cpy().scl(Gdx.graphics.getDeltaTime());
		}
		if (movementData.offset.x != 0 || movementData.offset.y != 0 || movementData.offset.z != 0) {
			boolean res = this.tryMove(entity, Game.world, movementData.offset, movementData.sizeAsFracOfMapsquare, true);
			if (!res) {
				movementData.hitWall = true;
				if (movementData.removeIfHitWall) {
					entity.remove();
					Settings.p(entity + " removed");
				}
			}
		}
	}


	/**
	 * Returns false if entity fails to move on any axis.
	 */
	private boolean tryMove(AbstractEntity entity, World world, Vector3 moveVec, float sizeAsFracOfMapsquare, boolean doFine) {
		if (moveVec.len() <= 0) {
			return true;
		}

		PositionData pos = (PositionData)entity.getComponent(PositionData.class);
		pos.originalPosition.set(pos.position);
		Vector3 position = pos.position;

		boolean resultX = false;
		if (world.rectangleFree(position.x+moveVec.x, position.z, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
			position.x += moveVec.x;
			resultX = true;
		}/* else if (doFine) {
			for (int i = 0; i < 10; i++) {
				if (world.rectangleFree(position.x+moveVec.x/10f, position.z, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
					position.x += moveVec.x/10f;
					resultX = true;
				} else {
					break;
				}
			}
		}*/

		boolean resultZ = false;
		if (world.rectangleFree(position.x, position.z+moveVec.z, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
			position.z += moveVec.z;
			resultZ = true;
		} /*else if (doFine) {
			for (int i = 0; i < 10; i++) {
				if(world.rectangleFree(position.x, position.z+moveVec.z/10f, sizeAsFracOfMapsquare, sizeAsFracOfMapsquare)) {
					position.z += moveVec.z/10f;
					resultZ = true;
				} else {
					break;
				}
			}
		}*/

		if (moveVec.y != 0) {
			position.y += moveVec.y;
		}

		if (checkForPlayerCollision(entity, position) || checkForNastiesCollision(entity, position)) {
			pos.position.set(pos.originalPosition); // Move back
			return false;
		}

		return resultX && resultZ;
	}


	private boolean checkForPlayerCollision(AbstractEntity entity, Vector3 pos) {
		HarmsPlayer hp = (HarmsPlayer)entity.getComponent(HarmsPlayer.class);
		if (hp != null) {
			float dist = pos.dst(Game.player.getPosition());
			if (dist < Game.UNIT * .5f) {
				Game.player.damaged(hp.damageCaused, new Vector3()); // todo - direction
				entity.remove(); // Prevent further collisions
				return true;
			}
		}
		return false;
	}


	private boolean checkForNastiesCollision(AbstractEntity bullet, Vector3 pos) {
		HarmsNasties hp = (HarmsNasties)bullet.getComponent(HarmsNasties.class);
		if (hp != null) {
			Iterator<AbstractEntity> it = ecs.getIterator();
			while (it.hasNext()) {
				AbstractEntity nasty = it.next();
				IsDamagableNasty dam = (IsDamagableNasty)nasty.getComponent(IsDamagableNasty.class);
				if (dam != null) {
					PositionData posData = (PositionData)nasty.getComponent(PositionData.class);
					float dist = pos.dst(posData.position);
					if (dist < .5f) {
						if (hp.remove_on_collision) {
							bullet.remove();
						}
						return true;
					}
				}
			}
		}
		return false;
	}

}
