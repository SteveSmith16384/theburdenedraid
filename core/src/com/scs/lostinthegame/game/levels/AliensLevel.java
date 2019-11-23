package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.aliens.Alien;
import com.scs.lostinthegame.game.player.weapons.PlayersLaserGun;
import com.scs.lostinthegame.mapgen.AbstractDungeon;
import com.scs.lostinthegame.mapgen.DungeonGen1;

import ssmith.lang.NumberFunctions;

public class AliensLevel extends AbstractLevel {

	public AliensLevel(int difficulty) {
		super(difficulty);
	}


	@Override
	public void load(Game game) {
		//loadMapFromImage(game);
		//loadTestMap(game);
		loadMapFromMazegen(game);

		game.player.setWeapon(new PlayersLaserGun("aliens/playersgun.png"));
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
	}


	private void loadMapFromMazegen(Game game) {
		// todo - make consts
		this.map_width = 20;// + (this.difficulty * 3);
		this.map_height = 20;// + (this.difficulty * 3);

		Game.world.world = new WorldSquare[map_width][map_height];

		DungeonGen1 maze = new DungeonGen1(20, 3+this.difficulty, 5, 0);

		this.playerStartMapX = maze.centres.get(0).x;
		this.playerStartMapY = maze.centres.get(0).y;

		for (int z=0 ; z<map_height ; z++) {
			for (int x=0 ; x<map_width ; x++) {
				Game.world.world[x][z] = new WorldSquare();
				Game.world.world[x][z].blocked = maze.map[x][z] == AbstractDungeon.SqType.WALL;
				if (Game.world.world[x][z].blocked) {
					Wall wall = null;
					if (NumberFunctions.rnd(0,  1) == 0) {
						wall = new Wall("aliens/aliens_wall.png", x, z);
					} else {
						wall = new Wall("aliens/normal_wall.png", x, z);
					}
					game.ecs.addEntity(wall);
				}
			}
		}

		for (int i=1 ; i<maze.centres.size() ; i++) {
			GridPoint2 pos = maze.centres.get(i);
			//todo - re-add Alien alien  = new Alien(pos.x, pos.y);
			//game.ecs.addEntity(alien);
		}

		//MonsterMazeExit exit = new MonsterMazeExit(maze.end_pos.x, maze.end_pos.y);
		//game.ecs.addEntity(exit);

		//game.ecs.addEntity(new Floor("colours/white.png", map_width, map_height, false));
	}


	@Override
	public void update(Game game, World world) {
		if (Alien.total_aliens <= 0) {
			//todo - re-add Game.levelComplete = true;
		}
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font_white, BitmapFont font_black) {		
	}


	@Override
	public String GetName() {
		return "ALIENS (UK)";
	}


	@Override
	public String getInstructions() {
		return "";
	}

}
