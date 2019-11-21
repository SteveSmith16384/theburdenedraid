package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.entities.Floor;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.chaos.ChaosWraith;
import com.scs.lostinthegame.game.entities.startlevel.StartExit;
import com.scs.lostinthegame.game.player.weapons.PlayersLaserGun;

import ssmith.lang.NumberFunctions;

public class ChaosLevel extends AbstractLevel {

	public ChaosLevel(int difficulty) {
		super(difficulty);
	}


	@Override
	public void load(Game game) {
		loadMap(game);

		//createWalls(game);

		game.player.setWeapon(new PlayersLaserGun());
	}


	private void loadMap(Game game) {
		this.map_width = 14;
		this.map_height = 10;

		Game.world.world = new WorldSquare[map_width][map_height];

		this.playerStartMapX = 1;
		this.playerStartMapY = 1;

		for (int z=0 ; z<map_height ; z++) {
			for (int x=0 ; x<map_width ; x++) {
				Game.world.world[x][z] = new WorldSquare();
				//int type = World.NOTHING;
				if (x == 0 || z == 0 || x >= map_width-1 || z >= map_height-1) {
					//Game.world.world[x][z] = new WorldSquare();
					Game.world.world[x][z].blocked = true;
					game.ecs.addEntity(new Wall("chaos/chaoswall.png", x, z));
				}
				if ( x >= map_width / 2 || z >= map_height/2) {
					if (NumberFunctions.rnd(1, 20) == 1) {
						int i = NumberFunctions.rnd(1, 5);
						switch (i) {
						case 1:
							Game.world.world[x][z].blocked = true;
							game.ecs.addEntity(new Wall("chaos/brickwall.png", x, z));
							break;
						case 2:
							game.ecs.addEntity(new ChaosWraith(x, z));
							break;
							// todo - add more
						}
					}
				}

				//Game.world.world[x][z].blocked = type == World.WALL;
			}
		}

		game.ecs.addEntity(new StartExit(map_width-1, map_height-2));

	}

	/*
	private void createWalls(Game game) {
		for (int y = 0; y < map_height; y++) {
			for (int x = 0; x < map_width; x++) {
				try {
					boolean block = Game.world.world[x][y].blocked;
					if (block) {
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}

		game.ecs.addEntity(new Floor("lasersquad/moonbase_interior_floor.png", map_width, map_height, true));
	}
	 */

	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {

	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font_white, BitmapFont font_black) {
	}


	@Override
	public String GetName() {
		return "CHAOS";
	}


	@Override
	public String getInstructions() {
		return "todo";
	}

}
