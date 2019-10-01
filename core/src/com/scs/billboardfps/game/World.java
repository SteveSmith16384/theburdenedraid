package com.scs.billboardfps.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.data.WorldSquare;
import com.scs.billboardfps.game.decals.DecalEntity;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entity.EntityManager;
import com.scs.billboardfps.game.entity.FloorSquare;
import com.scs.billboardfps.game.renderable.RenderData;

public class World {

	public static final int NOTHING = 0;
	public static final int WALL = 1;
	public static final int BLOCKED = 2;

	public WorldSquare world[][]; // todo - make 2d
	//public int width;
	//public int height;

	//public int playersStartMapX, playerStartMapY;

	public ArrayList<ModelInstance> modelInstances = new ArrayList<ModelInstance>();

	public TextureRegion detailTexture[];

	public ArrayList<ModelInstance> specialBlocks;

	public World(DecalManager decalMan, EntityManager entityMan) {
		//entityManager = entityMan;
		//decalManager = decalMan;

		Texture detailTex = new Texture(Gdx.files.internal("detail.png"));

		int w = detailTex.getWidth()/16;
		int h = detailTex.getHeight()/16;

		detailTexture = new TextureRegion[w*h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				detailTexture[x + y*w] = new TextureRegion(detailTex, x*16, y*16, 16, 16);
			}
		}

		//load(Settings.START_LEVEL.length() > 0 ? Settings.START_LEVEL : Settings.levelOrder[0]);

	}


	public int getMapSquareAt(int x, int y) {
		if (x < 0 || y < 0) {
			//Settings.p("OOB!");
			return WALL;
		}

		//int t = x + y*width;
		//if(t < world.length) {
		try {
			return world[x][y].type;
			/*} else {
			return WALL;
		}*/
		} catch (ArrayIndexOutOfBoundsException ex) {
			return WALL;
		}
	}


	public boolean rectangleFree(float center_x, float center_z, float width, float depth) {
		//Upper left
		float x = (center_x/Game.UNIT)-(width/2) + 0.5f;
		float y = center_z/Game.UNIT-depth/2 + 0.5f;

		if (getMapSquareAt((int)(x), (int)(y))!=0) {
			return false;
		}

		//Down left
		x = center_x/Game.UNIT-width/2 + 0.5f;
		y = center_z/Game.UNIT+depth/2 + 0.5f;

		if (getMapSquareAt((int)(x), (int)(y))!=0) {
			return false;
		}

		//Upper right
		x = center_x/Game.UNIT+width/2 + 0.5f;
		y = center_z/Game.UNIT-depth/2 + 0.5f;

		if (getMapSquareAt((int)(x), (int)(y))!=0) {
			return false;
		}

		//Down right
		x = center_x/Game.UNIT+width/2 + 0.5f;
		y = center_z/Game.UNIT+depth/2 + 0.5f;

		if (getMapSquareAt((int)(x), (int)(y))!=0) {
			return false;
		}

		return true;
	}


	public boolean canSee(Vector3 pos1, Vector3 pos2) {
		Vector3 tmp = new Vector3();
		tmp.set(pos1);

		Vector3 dir = new Vector3();
		dir.set(tmp).sub(pos2);
		dir.y = 0;
		dir.nor();

		while(tmp.dst2(pos2) > (Game.UNIT/2f) * (Game.UNIT/2f)){
			tmp.mulAdd(dir, -Game.UNIT/4f);

			if (getMapSquareAt(tmp.x, tmp.z) != NOTHING) {
				return false;
			}
		}
		return true;
	}


	public int getMapSquareAt(float x, float y){
		return getMapSquareAt((int)(x/Game.UNIT+0.5f), (int)(y/Game.UNIT+0.5f));
	}

	public int getMapSquareAt(Vector3 vec){
		return getMapSquareAt((int)(vec.x+0.5f), (int)(vec.z+0.5f));
	}


}
