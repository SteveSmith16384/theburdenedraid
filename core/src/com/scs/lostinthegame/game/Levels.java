package com.scs.lostinthegame.game;

import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.levels.AbstractLevel;
import com.scs.lostinthegame.game.levels.AndroidsLevel;
import com.scs.lostinthegame.game.levels.GulpmanLevel;
import com.scs.lostinthegame.game.levels.MinedOutLevel;
import com.scs.lostinthegame.game.levels.MonsterMazeLevel;
import com.scs.lostinthegame.game.levels.OhMummyLevel;

public class Levels {
	
	private int currentLevelNum = 0; 
	public int numberTimesLoopAround = 0;

	public Levels() {
	}
	
	public AbstractLevel getNextLevel(EntityManager entityManager, DecalManager decalManager) {
		switch (currentLevelNum) {
		case 1:
			return new OhMummyLevel(entityManager, decalManager, numberTimesLoopAround); // done!
		case 2:
			return new GulpmanLevel(entityManager, decalManager, numberTimesLoopAround); // done!
		case 3:
			return new AndroidsLevel(entityManager, decalManager, numberTimesLoopAround);
		case 4:
			return new MonsterMazeLevel(entityManager, decalManager, numberTimesLoopAround);
		case 5:
			return new MinedOutLevel(entityManager, decalManager, numberTimesLoopAround); // done!
		//case 6:
			//return new EricAndTheFloatersLevel(entityManager, decalManager);
			//return new MaziacsLevel(entityManager, decalManager);
			//gameLevel = new LaserSquadLevel(this.entityManager, this.decalManager);

		default:
			//throw new RuntimeException("Unknown level: " + currentLevelNum);
			// Loop around
			currentLevelNum -= 5;
			numberTimesLoopAround++;
			return getNextLevel(entityManager, decalManager);
		}
	}
	
	
	public void nextLevel() {
		this.currentLevelNum++;
	}

}
