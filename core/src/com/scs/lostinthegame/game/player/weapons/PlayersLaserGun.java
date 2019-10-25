package com.scs.lostinthegame.game.player.weapons;

import com.badlogic.gdx.math.Vector3;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.entities.Entity;
import com.scs.lostinthegame.game.entities.chaos.ChaosBolt;

public class PlayersLaserGun extends AbstractIntervalWeapon implements IPlayersWeapon {

	public PlayersLaserGun() {
		super("lasergun1.png", .5f);
	}

	
	@Override
	protected void weaponFired(Vector3 position, Vector3 direction) {
		Entity b = new ChaosBolt(this, position, direction);
		Game.entityManager.add(b);
	}

}
