package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.entities.Floor;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.monstermaze.MonsterMazeExit;
import com.scs.lostinthegame.game.entities.monstermaze.TRex;

public class MonsterMazeLevel extends AbstractLevel {

	public MonsterMazeLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {
		//loadMapFromImage(game);
		loadTestMap(game);
		
		//game.ecs.addEntity(new Floor("colours/white.png", map_width, map_height));
		//game.ecs.addEntity(new Ceiling("colours/white.png", map_width, map_height));

	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
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
					Wall wall = new Wall("monstermaze/wall.png", x, z);
					game.ecs.addEntity(wall);
				} else if (x == 2 && z == 2) {
					type = World.WALL;
					Wall wall = new Wall("monstermaze/wall.png", x, z);
					game.ecs.addEntity(wall);
				} else if (x == 3 && z == 1) {
					TRex trex = new TRex(x, z);
					game.ecs.addEntity(trex);
				} else if (x == 3 && z == 3) {
					MonsterMazeExit exit = new MonsterMazeExit(x, z);
					game.ecs.addEntity(exit);
				}

				Game.world.world[x][z].blocked = type == World.WALL;
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
		// todo - "REX LIES IN WAIT, followed by HE IS HUNTING FOR YOU, FOOTSTEPS APPROACHING, REX HAS SEEN YOU, and RUN HE IS BESIDE YOU or RUN HE IS BEHIND YOU"
	}


	@Override
	public String GetName() {
		return "3D MONSTER MAZE";
	}

}
