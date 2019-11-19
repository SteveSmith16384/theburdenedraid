package com.scs.lostinthegame.test;

import com.scs.lostinthegame.game.entities.minedout.Mine;
import com.scs.lostinthegame.game.player.Player;
import com.scs.lostinthegame.game.systems.CountMinesSystem;

public class TestCountMinesSystem {

	public TestCountMinesSystem() {
		Player player = new Player(null, null, 0, 1);
		CountMinesSystem cms = new CountMinesSystem(null, player);
		
		Mine mine = new Mine(1, 1);
		cms.processEntity(mine);
	}

}
