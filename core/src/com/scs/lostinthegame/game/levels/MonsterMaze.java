package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.ericandthefloaters.EricBombDropper;
import com.scs.lostinthegame.game.entities.monstermaze.MonsterMazeExit;
import com.scs.lostinthegame.game.systems.EricAndTheFloatersExplosionSystem;

public class MonsterMaze extends AbstractLevel {

	public MonsterMaze(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {
		//loadMapFromImage(game);
		loadTestMap(game);

		game.player.setWeapon(new EricBombDropper());
		game.ecs.addSystem(new EricAndTheFloatersExplosionSystem(game.ecs));
	}


	private void loadTestMap(Game game) {
		this.map_width = 5;
		this.map_height = 5;

		Game.world.world = new WorldSquare[map_width][map_height];

		this.playerStartMapX = 1;
		this.playerStartMapY = 1;

		for (int z=0 ; z<map_height ; z++) {
			for (int x=0 ; x<map_width ; x++) {
				int type = World.NOTHING;
				Game.world.world[x][z] = new WorldSquare();
				if (x == 0 || z == 0 || x >= map_width-1 || z >= map_height-1) {
					type = World.WALL;
					Wall wall = new Wall("colours/black.png", x, z);
					game.ecs.addEntity(wall);
				} else if (x == 2 && z == 2) {
					type = World.WALL;
					Wall wall = new Wall("colours/black.png", x, z);
					game.ecs.addEntity(wall);
				} else if (x == 3 && z == 1) {
					// todo - t-rex
				} else if (x == 3 && z == 3) {
					MonsterMazeExit exit = new MonsterMazeExit(x, z);
					game.ecs.addEntity(exit);
				}

				Game.world.world[x][z].type = type;
			}
		}
	}


	@Override
	public void update(Game game, World world) {
	}

	
	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font) {
	}


	@Override
	public String GetName() {
		return "3D MONSTER MAZE";
	}

}
