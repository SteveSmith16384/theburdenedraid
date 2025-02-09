package com.scs.lostinthegame.game.player.weapons;

import com.badlogic.gdx.math.Vector3;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.entities.PlayersLaserBullet;

public class PlayersLaserGun extends AbstractIntervalWeapon implements IPlayersWeapon {

	public PlayersLaserGun(String image) {
		super(image, .5f);
	}

	
	@Override
	protected void weaponFired(Vector3 position, Vector3 direction) {
		Game.audio.play("beepfx_samples/17_hit_3.wav");

		Game.ecs.addEntity(new PlayersLaserBullet(position, direction));
	}

}
