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
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.ohmummy.Pill;
import com.scs.lostinthegame.game.player.weapons.IPlayersWeapon;

public class OhMummyLevel extends AbstractLevel {

	public OhMummyLevel(EntityManager _entityManager, DecalManager _decalManager) {
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

		for (int z=0 ; z<map_height ; z++) {
			for (int x=0 ; x<map_width ; x++) {
				int type = World.NOTHING;
				if (x == 0 || z == 0 || x >= map_width-1 || z >= map_height-1) {
					type = World.WALL;
				} else if (x == 1 && z == 1) {
					this.playerStartMapX = x;
					this.playerStartMapY = z;
				} else if (x == 2 && z == 2) {
					type = World.WALL;
				} else {
					Pill ch = new Pill(x, z);
					game.ecs.addEntity(ch);
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
						if (x == 0 || y == 0 || x >= map_width-1 || y >= map_height-1) {
							game.ecs.addEntity(new Wall("colours/white.png", x, y));
						} else {
							game.ecs.addEntity(new Wall("colours/magenta.png", x, y));
						}
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}

		//game.ecs.addEntity(new Floor("colours/cyan.png", map_width, map_height));
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
		font.draw(batch, "Oh Mummy!", 10, 30);
	}

}
