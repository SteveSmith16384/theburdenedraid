package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.Ceiling;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.entities.Floor;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.gulpman.Cherry;
import com.scs.lostinthegame.game.entities.gulpman.Nasty;

public class GulpmanLevel extends AbstractLevel {

	private int num_cherries = 0;
	
	public GulpmanLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {
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
				} else if (x == 2 && z == 2) {
					type = World.WALL;
				} else if (x == 3 && z == 3) {
					Nasty n = new Nasty(x, z);
					game.ecs.addEntity(n);
				} else {
					Cherry ch = new Cherry(x, z);
					game.ecs.addEntity(ch);
					this.num_cherries++;
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
						game.ecs.addEntity(new Wall("colours/blue.png", x, y));
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}

		game.ecs.addEntity(new Floor("colours/cyan.png", map_width, map_height));
		game.ecs.addEntity(new Ceiling("colours/cyan.png", map_width, map_height));

	}


	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
		if (collectable instanceof Cherry) {
			this.num_cherries--;
			if (this.num_cherries <= 0) {
				Game.levelComplete = true;
			}
		}
	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font) {
		font.draw(batch, num_cherries + " remaining", 10, 30);
	}


	@Override
	public String GetName() {
		return "GULPMAN";
	}

}
