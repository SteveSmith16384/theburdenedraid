package com.scs.lostinthegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.Maze;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.mapgen.AbstractDungeon;
import com.scs.lostinthegame.mapgen.DungeonGen1;

public class AliensLevel extends AbstractLevel {

	public AliensLevel(int difficulty) {
		super(difficulty);
	}


	@Override
	public void load(Game game) {
		//loadMapFromImage(game);
		//loadTestMap(game);
		loadMapFromMazegen(game);

	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
	}


	private void loadMapFromMazegen(Game game) {
		this.map_width = 16 + (this.difficulty * 3);
		this.map_height = 16 + (this.difficulty * 3);

		Game.world.world = new WorldSquare[map_width][map_height];

		DungeonGen1 maze = new DungeonGen1(20, 3, 5, 3);

		this.playerStartMapX = maze.start_pos.x;
		this.playerStartMapY = maze.start_pos.y;

		for (int z=0 ; z<map_height ; z++) {
			for (int x=0 ; x<map_width ; x++) {
				Game.world.world[x][z] = new WorldSquare();
				Game.world.world[x][z].blocked = maze.map[x][z] == AbstractDungeon.SqType.WALL;
				if (Game.world.world[x][z].blocked) {
					Wall wall = new Wall("monstermaze/wall.png", x, z);
					game.ecs.addEntity(wall);
				}
			}
		}

		//trex = new TRex(maze.middle_pos.x, maze.middle_pos.y);
		//game.ecs.addEntity(trex);

		//MonsterMazeExit exit = new MonsterMazeExit(maze.end_pos.x, maze.end_pos.y);
		//game.ecs.addEntity(exit);

		//game.ecs.addEntity(new Floor("colours/white.png", map_width, map_height, false));
}


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
		return "ALIENS (UK)";
	}


	@Override
	public String getInstructions() {
		return "";
	}

}
