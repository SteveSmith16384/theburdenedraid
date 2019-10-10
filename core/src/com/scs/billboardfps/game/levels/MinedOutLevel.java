package com.scs.billboardfps.game.levels;

import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entities.EntityManager;
import com.scs.billboardfps.game.player.weapons.IPlayersWeapon;

public class MinedOutLevel extends AbstractLevel {

	public MinedOutLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}

	@Override
	public void levelComplete() {
	}
	

	@Override
	public void load(Game game) {
		
	}

	@Override
	public IPlayersWeapon getWeapon() {
		return null;
	}

}
