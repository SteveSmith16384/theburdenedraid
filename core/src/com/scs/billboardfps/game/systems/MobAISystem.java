package com.scs.billboardfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.components.HasAI;
import com.scs.billboardfps.game.components.HasDecal;
import com.scs.billboardfps.game.components.MovementData;
import com.scs.billboardfps.game.components.PositionData;

public class MobAISystem extends AbstractSystem {

	public enum Mode {GoForPlayer, MoveLikeRook}

	public MobAISystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return HasAI.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PositionData pos = (PositionData)entity.getComponent(PositionData.class);
		HasAI ai = (HasAI)entity.getComponent(HasAI.class);
		MovementData movementData = (MovementData)entity.getComponent(MovementData.class);
		movementData.offset.x = 0;
		movementData.offset.y = 0;
		movementData.offset.z = 0;

		if (Game.player.getPosition().dst2(pos.position) < (Game.UNIT * 5) * (Game.UNIT * 5) && Game.world.canSee(pos.position, Game.player.getPosition())) {
			switch (ai.mode) {
			case GoForPlayer:			
				ai.direction.set(Game.player.getPosition()).sub(pos.position).nor();
				ai.direction.scl(Gdx.graphics.getDeltaTime() * ai.speed * Game.UNIT);
				ai.direction.y = 0f;

				movementData.offset.x = ai.direction.x;
				movementData.offset.z = ai.direction.z;
				break;

			case MoveLikeRook:
				if (ai.direction.len2() == 0 || Settings.random.nextFloat() <= 0.1f) {
					ai.direction = getRandomDirection();
				}
				movementData.offset.x = ai.direction.x;
				movementData.offset.z = ai.direction.z;
				movementData.offset.scl(ai.speed);
				break;

			default:
				throw new RuntimeException("Unknown AI type: " + ai.mode);
			}

		}

	}
	
	
	private Vector3 getRandomDirection() {
		int i = Settings.random.nextInt(4);
		switch (i) {
		case 0: return new Vector3(1, 0, 0);
		case 1: return new Vector3(0, 0, 1);
		case 2: return new Vector3(-1, 0, 0);
		default: return new Vector3(0, 0, -1);
		}
	}


}
