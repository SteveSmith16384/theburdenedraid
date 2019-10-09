package com.scs.billboardfps.game.levels;

import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entity.EntityManager;
import com.scs.billboardfps.game.player.weapons.IPlayersWeapon;

public class MinedOutLevel extends AbstractLevel {

	public MinedOutLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}

	@Override
	public void levelComplete() {
		// todo		
	}
	

	@Override
	public void load(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPlayersWeapon getWeapon() {
		return null;
	}

}
