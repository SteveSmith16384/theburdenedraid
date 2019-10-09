package com.scs.billboardfps.game.entity.androids;

import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.player.weapons.AbstractIntervalWeapon;
import com.scs.billboardfps.game.player.weapons.IPlayersWeapon;

public class PlayersLaserGun extends AbstractIntervalWeapon implements IPlayersWeapon {

	public PlayersLaserGun() {
		super("todo", .5f);
	}

	@Override
	protected void weaponFired(Vector3 position, Vector3 direction) {
		// TODO Auto-generated method stub
		
	}

}
