package com.scs.lostinthegame.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.Ceiling;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.entities.Floor;
import com.scs.lostinthegame.game.entities.Wall;
import com.scs.lostinthegame.game.entities.androids.AndroidsAndroid;
import com.scs.lostinthegame.game.entities.androids.GSquare;
import com.scs.lostinthegame.game.entities.androids.SSquare;
import com.scs.lostinthegame.game.entities.androids.SlidingDoor;
import com.scs.lostinthegame.game.player.weapons.IPlayersWeapon;

public class AndroidsLevel extends AbstractLevel {

	public AndroidsLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {
		//entityManager.getEntities().clear();
		//decalManager.clear();
		//game.modelInstances = new ArrayList<ModelInstance>();

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
					SlidingDoor door = new SlidingDoor(x, z, "colours/cyan.png");
					game.ecs.addEntity(door);
				} else if (x == 1 && z == 3) {
					GSquare ss = new GSquare(x, z);
					game.ecs.addEntity(ss);
				} else if (x == 3 && z == 1) {
					SSquare ss = new SSquare(x, z);
					game.ecs.addEntity(ss);
				}

				Game.world.world[x][z] = new WorldSquare();
				Game.world.world[x][z].type = type;
			}
		}
	}


	private void loadMapFromImage(Game game) {
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
		for (int pixel_z=8 ; pixel_z<image_height ; pixel_z+=16) {
			int mapX = 0;
			for (int pixel_x=8 ; pixel_x<image_width ; pixel_x+=16) {
				int col = pixmap.getPixel(pixel_x, pixel_z);
				int type = World.NOTHING;
				switch (col) {
				case 65791: // Wall
					type = World.WALL;
					break;

				case -825437953: // floor
					// Do nothing
					break;

				case 247909119: // door?
				case 336565503:
					SlidingDoor door = new SlidingDoor(mapX, mapZ, "colours/cyan.png");
					game.ecs.addEntity(door);
					break;

				case 1338133247: // baddy?
				case -1798385153:
					AbstractEntity e = new AndroidsAndroid(mapX, mapZ);//playerStartMapX-1, playerStartMapY-1);
					game.ecs.addEntity(e);
					Settings.p("Created " + e.name + " at " + mapX + "," + mapZ);
					break;

				case 437736447: // G square
				case -1706648833:
					GSquare gs = new GSquare(mapX, mapZ);
					game.ecs.addEntity(gs);
					break;

				case -15462508: // start
					playerStartMapX = mapX;
					this.playerStartMapY = mapZ;
					break;

				case -155191809: // S square
					SSquare ss = new SSquare(mapX, mapZ);
					game.ecs.addEntity(ss);
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

	}


	private void createWalls(Game game) {
		/*ModelBuilder modelBuilder = new ModelBuilder();

		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture("androids/black.png")));
		Model box_model = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
*/
		for (int y = 0; y < map_height; y++) {
			for (int x = 0; x < map_width; x++) {
				try {
					int block = Game.world.world[x][y].type;
					if (block == World.WALL) {
						/*ModelInstance instance = new ModelInstance(box_model);
						instance.transform.translate(x*Game.UNIT, Game.UNIT/2f, y*Game.UNIT);
						instance.transform.rotate(Vector3.Z, 90);
						game.modelInstances.add(instance);*/
						game.ecs.addEntity(new Wall("colours/black.png", x, y));
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}

/*
		Texture tex = new Texture("unit_highlighter_nw.png");
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Material white_material = new Material(TextureAttribute.createDiffuse(tex));		

		Model floor = modelBuilder.createRect(
				0f,0f, (float) map_height*Game.UNIT,
				(float)map_width*Game.UNIT, 0f, (float)map_height*Game.UNIT,
				(float)map_width*Game.UNIT, 0f, 0f,
				0f,0f,0f,
				1f,1f,1f,
				white_material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		Matrix3 mat = new Matrix3();
		mat.scl(new Vector2(map_width, map_height));
		floor.meshes.get(0).transformUV(mat);
		
		//Create floor
		ModelInstance instance = new ModelInstance(floor);
		game.modelInstances.add(instance);

		// ceiling
		instance = new ModelInstance(floor);
		instance.transform.translate(0, Game.UNIT, 0);
		instance.transform.rotate(Vector3.X, 180);
		instance.transform.translate(0, 0, -(float)map_width* Game.UNIT);
		game.modelInstances.add(instance);
		*/
		
		game.ecs.addEntity(new Floor("unit_highlighter_nw.png", map_width, map_height));
		game.ecs.addEntity(new Ceiling("unit_highlighter_nw.png", map_width, map_height));
	}


	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void levelComplete() {

	}

/*
	@Override
	public IPlayersWeapon getWeapon() {
		return null;//new PlayersLaserGun();
	}
*/

	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
		
	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font) {
		//font.draw(batch, "ANDROIDS", 10, 30);
	}
	
	
	@Override
	public String GetName() {
		return "ANDROIDS";
	}



}
