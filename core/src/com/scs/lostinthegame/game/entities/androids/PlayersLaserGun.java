package com.scs.lostinthegame.game.entities.androids;

import com.badlogic.gdx.math.Vector3;
import com.scs.lostinthegame.game.player.weapons.AbstractIntervalWeapon;
import com.scs.lostinthegame.game.player.weapons.IPlayersWeapon;

public class PlayersLaserGun extends AbstractIntervalWeapon implements IPlayersWeapon {

	public PlayersLaserGun() {
		super("lasergun1.png", .5f);
	}

	@Override
	protected void weaponFired(Vector3 position, Vector3 direction) {
		// TODO Auto-generated method stub
		
	}

}
