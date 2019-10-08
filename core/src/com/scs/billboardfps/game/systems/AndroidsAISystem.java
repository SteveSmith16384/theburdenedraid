package com.scs.billboardfps.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.components.HasAI;
import com.scs.billboardfps.game.components.HasDecal;
import com.scs.billboardfps.game.components.PositionData;
import com.scs.billboardfps.game.components.MovementData;

public class AndroidsAISystem extends AbstractSystem {

	public AndroidsAISystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return HasDecal.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PositionData pos = (PositionData)entity.getComponent(PositionData.class);
		HasAI ai = (HasAI)entity.getComponent(HasAI.class);
		MovementData movementData = (MovementData)entity.getComponent(MovementData.class);
		movementData.offset.x = 0;
		movementData.offset.y = 0;
		movementData.offset.z = 0;

		float dt = Gdx.graphics.getDeltaTime();

		if (Game.player.getPosition().dst2(pos.position) < (Game.UNIT * 5) * (Game.UNIT * 5) && Game.world.canSee(pos.position, Game.player.getPosition())) {
			ai.direction.set(Game.player.getPosition()).sub(pos.position).nor();
			ai.direction.scl(Gdx.graphics.getDeltaTime() * ai.speed * Game.UNIT);
			ai.direction.y = 0f;

			//Vector2 moveVec = new Vector2();
			movementData.offset.x = ai.direction.x;
			movementData.offset.z = ai.direction.z;

			//tryMove(Game.world, pos.position, moveVec, true);
		}

	}


}
