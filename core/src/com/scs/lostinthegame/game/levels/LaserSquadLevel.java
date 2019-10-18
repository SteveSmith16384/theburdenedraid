package com.scs.lostinthegame.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.entities.Floor;
import com.scs.lostinthegame.game.entities.GenericScenery;
import com.scs.lostinthegame.game.entities.GenericWallScenery;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.androids.PlayersLaserGun;
import com.scs.lostinthegame.game.entities.androids.SlidingDoor;
import com.scs.lostinthegame.game.player.weapons.IPlayersWeapon;

public class LaserSquadLevel extends AbstractLevel {

	public LaserSquadLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {
		entityManager.getEntities().clear();
		decalManager.clear();
		game.modelInstances = new ArrayList<ModelInstance>();

		//loadMapFromImage(game);
		loadTestMap(game);

		createWalls(game);
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
				if (x == 0 || z == 0 || x >= map_width-1 || z >= map_height-1) {
					type = World.WALL;
				} else if (x == 3 && z == 1) {
					GenericWallScenery s = new GenericWallScenery("VidScreen", "lasersquad/vidscreen.png", x, z, GenericWallScenery.Side.Back);
					game.ecs.addEntity(s);
				} else if (x == 3 && z == 2) {
					GenericScenery s = new GenericScenery("Barrel", "lasersquad/barrel.png", x, z);
					game.ecs.addEntity(s);
				} else if (x == 3 && z == 3) {
					GenericScenery s = new GenericScenery("GasCan", "lasersquad/gas_cannisters.png", x, z);
					game.ecs.addEntity(s);
				} else if (x == 1 && z == 2) {
					SlidingDoor door = new SlidingDoor(x, z, "lasersquad/interior_door.png");
					game.ecs.addEntity(door);
				}

				Game.world.world[x][z] = new WorldSquare();
				Game.world.world[x][z].type = type;
			}
		}
	}


	private void createWalls(Game game) {
		for (int y = 0; y < map_height; y++) {
			for (int x = 0; x < map_width; x++) {
				try {
					int block = Game.world.world[x][y].type;
					if (block == World.WALL) {
						game.ecs.addEntity(new Wall("lasersquad/moonbase_wall.png", x, y));
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}

		game.ecs.addEntity(new Floor("lasersquad/moonbase_interior_floor.png", map_width, map_height));
		//game.ecs.addEntity(new Ceiling("colours/cyan.png", map_width, map_height));

	}


	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void levelComplete() {

	}


	@Override
	public IPlayersWeapon getWeapon() {
		return null;//new PlayersLaserGun();
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
		
	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font) {
		font.draw(batch, "LASER SQUAD", 10, 30);
	}

}
