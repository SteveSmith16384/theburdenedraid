package com.scs.lostinthegame.game;

import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.levels.AbstractLevel;
import com.scs.lostinthegame.game.levels.AndroidsLevel;
import com.scs.lostinthegame.game.levels.GulpmanLevel;
import com.scs.lostinthegame.game.levels.OhMummyLevel;

public class Levels {
	
	private int currentLevelNum = 0; 

	public Levels() {
	}
	
	public AbstractLevel getNextLevel(EntityManager entityManager, DecalManager decalManager) {
		switch (currentLevelNum) {
		case 1:
			return new OhMummyLevel(entityManager, decalManager);
		case 2:
			return new GulpmanLevel(entityManager, decalManager);
		case 3:
			return new AndroidsLevel(entityManager, decalManager);
			//gameLevel = new EricAndTheFloatersLevel(entityManager, decalManager);
			//gameLevel = new LaserSquadLevel(this.entityManager, this.decalManager);
			//gameLevel = new MaziacsLevel(entityManager, decalManager);
			//gameLevel = new MinedOutLevel(entityManager, decalManager);

		default:
			throw new RuntimeException("Todo");
		}
	}
	
	
	public void nextLevel() {
		this.currentLevelNum++;
	}

}
