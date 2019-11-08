package com.scs.lostinthegame.game.systems;
/*
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.components.HarmsPlayer;
import com.scs.lostinthegame.game.components.PositionData;

public class HarmPlayerSystem extends AbstractSystem {

	private static final float hurtDistanceSquared = Game.UNIT * .5f * Game.UNIT * .5f;

	public HarmPlayerSystem(BasicECS ecs) {
		super(ecs);
	}


	@Override
	public Class<?> getComponentClass() {
		return HarmsPlayer.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		PositionData pos = (PositionData)entity.getComponent(PositionData.class);
		if (Game.player.getPosition().dst2(pos.position) < hurtDistanceSquared) {
			Game.player.damaged(1, new Vector3()); // todo - dir
		}

	}

}
*/