package com.scs.billboardfps.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.data.WorldSquare;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entity.EntityManager;
import com.scs.billboardfps.game.entity.androids.AndroidsAndroid;
import com.scs.billboardfps.game.entity.androids.GSquare;
import com.scs.billboardfps.game.entity.androids.SSquare;
import com.scs.billboardfps.game.entity.androids.SlidingDoor;

public class AndroidsLevel extends AbstractLevel {

	public AndroidsLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {//String level) {
		entityManager.getEntities().clear();
		decalManager.clear();

		Texture texture = new Texture(Gdx.files.internal("androids/androids_map.png"));
		texture.getTextureData().prepare();
		Pixmap pixmap = texture.getTextureData().consumePixmap();

		this.map_width = pixmap.getWidth() / 16;
		this.map_height = pixmap.getHeight() / 16;
		Game.world.world = new WorldSquare[map_width+1][map_height+1];


		playerStartMapX = map_width/2;
		this.playerStartMapY = map_height/2;

		int image_width = pixmap.getWidth();
		int image_height = pixmap.getHeight();
		int mapZ = 0;
		for (int z=8 ; z<image_height ; z+=16) {
			int mapX = 0;
			for (int x=8 ; x<image_width ; x+=16) {
				int col = pixmap.getPixel(x, z);
				int type = World.NOTHING;
				switch (col) {
				case 65791: // Wall
					type = World.WALL;
					//todo terrainUDG.addRectRange_Blocks(BlockCodes.ANDROIDS_WALL, new Vector3Int(mapX, 0, mapZ), new Vector3Int(1, WALL_HEIGHT, 1));
					break;

				case -825437953: // floor
					// Do nothing
					break;

				case 247909119: // door?
				case 336565503:
					//todo SlidingDoor door = new SlidingDoor(mapX, mapZ);
					//Game.entityManager.add(door);
					break;

				case 1338133247: // baddy?
				case -1798385153:
					break;

				case 437736447: // G square
				case -1706648833:
					GSquare gs = new GSquare(mapX, mapZ);
					Game.entityManager.add(gs);
					break;

				case -15462508: // start
					playerStartMapX = mapX;
					this.playerStartMapY = mapZ;
					break;

				case -155191809: // S square
					SSquare ss = new SSquare(mapX, mapZ);
					Game.entityManager.add(ss);
					break;

				default:
					Settings.p("Unknown colour at " + mapX + "," + mapZ + ":" + col);
				}

				Game.world.world[mapX][mapZ] = new WorldSquare();
				Game.world.world[mapX][mapZ].type = type;

				mapX++;
			}
			mapZ++;
		}

		createModels(game);

		// todo - remove
		GSquare gs2 = new GSquare(playerStartMapX, playerStartMapY);
		Game.entityManager.add(gs2);
		game.modelInstances.add(gs2.instance);
		/*FloorSquare fs = new FloorSquare(playerStartMapX-1, playerStartMapY-1);
		Game.entityManager.add(fs);
		game.modelInstances.add(fs.instance);*/
		AbstractEntity e = new AndroidsAndroid(playerStartMapX-1, playerStartMapY-1);
		game.basicEcs.addEntity(e);
		e = new AndroidsAndroid(playerStartMapX-1, playerStartMapY+1);
		game.basicEcs.addEntity(e);
		e = new AndroidsAndroid(playerStartMapX+1, playerStartMapY-1);
		game.basicEcs.addEntity(e);
		e = new AndroidsAndroid(playerStartMapX+1, playerStartMapY+1);
		game.basicEcs.addEntity(e);

	}


	private void createModels(Game game) {
		ModelBuilder modelBuilder = new ModelBuilder();

		Material white_material = new Material(TextureAttribute.createDiffuse(new Texture("androids/white.png")));		
		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture("androids/black.png")));		
		Model box_model = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		game.modelInstances = new ArrayList<ModelInstance>();

		for (int y = 0; y < map_height; y++) {
			for (int x = 0; x < map_width; x++) {
				try {
					int block = Game.world.world[x][y].type;
					if (block == World.WALL) {
						ModelInstance instance = new ModelInstance(box_model);
						instance.transform.translate(x*Game.UNIT, Game.UNIT/2f, y*Game.UNIT);
						instance.transform.rotate(Vector3.Z, 90);
						//instance.userData = new RenderData(RenderData.ShaderType.FOG_TEXTURE, 0, tileType, 6, 6);
						game.modelInstances.add(instance);
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}


		Model floor = modelBuilder.createRect(
				0f,0f, (float) map_height*Game.UNIT,
				(float)map_width*Game.UNIT,0f, (float)map_height*Game.UNIT,
				(float)map_width*Game.UNIT, 0f, 0f,
				0f,0f,0f,
				1f,1f,1f,
				white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		
		//Create floor
		ModelInstance instance = new ModelInstance(floor);
		//instance.userData = new RenderData(RenderData.ShaderType.FOG_COLOR, 1, tileType, 6, 6, width, height);
		game.modelInstances.add(instance);

		// ceiling
		//if (Settings.HIDE_CEILING == false) {
		instance = new ModelInstance(floor);
		//instance.userData = new RenderData(RenderData.ShaderType.FOG_COLOR, 2, tileType, 6, 6, width, height);
		instance.transform.translate(0, Game.UNIT,0);
		instance.transform.rotate(Vector3.X, 180);
		instance.transform.translate(0,0,-(float)map_width* Game.UNIT);
		game.modelInstances.add(instance);
		//}

	}


	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void levelComplete() {

	}


}
