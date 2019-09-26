package com.scs.billboardfps.game;

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
import com.scs.billboardfps.game.decals.DecalEntity;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entity.EntityManager;
import com.scs.billboardfps.game.renderable.RenderData;

import java.util.ArrayList;
import java.util.Random;

public class World {

	public static final int NOTHING = 0;
	public static final int WALL = 1;

	public int world[];
	public int width;
	public int height;

	public int playersStartX, playerStartY;

	public ArrayList<ModelInstance> modelInstances;

	private TextureRegion detailTexture[];

	private DecalManager decalManager;
	private EntityManager entityManager;

	public String previousLevelName;
	public String currentLevelName; // todo - use a level num instead

	public ArrayList<ModelInstance> specialBlocks;

	public World(DecalManager decalMan, EntityManager entityMan) {
		entityManager = entityMan;
		decalManager = decalMan;

		Texture detailTex = new Texture(Gdx.files.internal("detail.png"));

		int w = detailTex.getWidth()/16;
		int h = detailTex.getHeight()/16;

		detailTexture = new TextureRegion[w*h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				detailTexture[x + y*w] = new TextureRegion(detailTex, x*16, y*16, 16, 16);
			}
		}

		load(Settings.START_LEVEL.length() > 0 ? Settings.START_LEVEL : Settings.levelOrder[0]);

	}
	

	public int getTileType(String level) {
		for (int i = 0; i < Settings.levelOrder.length; i++) {
			if(Settings.levelOrder[i].equals(level))
				return i;
		}
		return 0;
	}


	public String getNextLevel(){
		for (int i = 0; i < Settings.levelOrder.length-1; i++) {
			if(Settings.levelOrder[i].equals(currentLevelName)) {
				return Settings.levelOrder[i+1];
			}
		}

		return null;
	}


	public void load(String level) {
		previousLevelName = currentLevelName;
		currentLevelName = level;

		entityManager.getEntities().clear();
		decalManager.clear();

		Texture texture = new Texture(Gdx.files.internal("level/"+level+".png"));
		texture.getTextureData().prepare();

		Pixmap pixmap = texture.getTextureData().consumePixmap();

		width = pixmap.getWidth();
		height = pixmap.getHeight();

		playersStartX = 1;
		playerStartY = 1;

		world = new int[width * height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				world[x + y*width] = NOTHING;

				int pix = pixmap.getPixel(x,y);
				int result = 0;

				boolean was_spawn = entityManager.spawnEntity(this, pix, x, y);

				switch (pix) {
				//Red
				case -16776961:
					playersStartX = x;
					playerStartY = y;
					break;
					//Special wall 1 pink-ish
				case 1799323647:
					result = 3;
					break;
					//Special wall 2 pink-ish
				case 2134858751:
					result = 4;
					break;
					//Special wall 3 pink-ish
				case -696254465:
					result = 5;
					break;
					//Sign, prevent details from spawning here
				case 2140340223:
					result = 2;
					break;
					
				case 0xff: //Black
					result = WALL;//1;
					break;
				default:
					//Not working on HTML5, remove for that build
					if (pix!=-1 && !was_spawn) {
						// Log the colour so we know what we aren't handling
						System.out.println("Unable to handle colour " + pix);
					}
					break;
				}

				if (result > 0) {
					world[x + y*width] = result;
				}
			}
		}

		createModels();

		if (currentLevelName.equals(Settings.DEMON_LAIR)) {
			return;
		}

		addDetail();
	}


	private void addDetail() {
		int count = width*height / 3;

		Random r = new Random();

		for (int i = 0; i < count; i++) {
			int x = r.nextInt(width);
			int y = r.nextInt(height);

			if (getMapSquareAt(x,y) == NOTHING) {
				DecalEntity ent = new DecalEntity(detailTexture[r.nextInt(2)]);
				ent.position.set(x * Game.UNIT, 0, y * Game.UNIT);
				decalManager.add(ent);
			} else {
				count++;
			}
		}
	}


	private void createModels() {
		int tileType = getTileType(currentLevelName);

		ModelBuilder modelBuilder = new ModelBuilder();

		Texture tiles = new Texture(Gdx.files.internal("tiles.png"));
		Material material = new Material(TextureAttribute.createDiffuse(tiles));		
		Model box_model = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		Material material_alien = new Material(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("alienskin2.jpg"))));
		Model model_alien = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, material_alien, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

		modelInstances = new ArrayList<ModelInstance>();
		specialBlocks = new ArrayList<ModelInstance>();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int block = world[x + y*width];
				if (block == WALL) {
					ModelInstance instance = new ModelInstance(model_alien);
					instance.transform.translate(x*Game.UNIT, Game.UNIT/2f, y*Game.UNIT);
					instance.transform.rotate(Vector3.Z, 90);
					if (Settings.TRY_NEW_TEX) {
						//instance.userData = new RenderData(RenderData.ShaderType.FOG_TEXTURE);
					} else {
						instance.userData = new RenderData(RenderData.ShaderType.FOG_TEXTURE, 0, tileType, 6, 6);
					}
					modelInstances.add(instance);
				} else if(block>2 && block<=5) {
					ModelInstance instance = new ModelInstance(box_model);
					instance.transform.translate(x* Game.UNIT,Game.UNIT/2f, y*Game.UNIT);
					instance.transform.rotate(Vector3.Z, 90);
					instance.userData = new RenderData(RenderData.ShaderType.FOG_TEXTURE, 3+block-3, tileType, 6, 6);
					modelInstances.add(instance);
					specialBlocks.add(instance);
				}

			}
		}


		//Create floor
		Model floor = modelBuilder.createRect(
				0f,0f, (float) height*Game.UNIT,
				(float)width*Game.UNIT,0f, (float)height*Game.UNIT,
				(float)width*Game.UNIT, 0f, 0f,
				0f,0f,0f,
				1f,1f,1f,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		ModelInstance instance = new ModelInstance(floor);
		instance.userData = new RenderData(RenderData.ShaderType.FOG_COLOR, 1, tileType, 6, 6, width, height);
		modelInstances.add(instance);

		// ceiling
		if (Settings.HIDE_CEILING == false) {
			instance = new ModelInstance(floor);
			instance.userData = new RenderData(RenderData.ShaderType.FOG_COLOR, 2, tileType, 6, 6, width, height);
			instance.transform.translate(0, Game.UNIT,0);
			instance.transform.rotate(Vector3.X, 180);
			instance.transform.translate(0,0,-(float)width* Game.UNIT);
			modelInstances.add(instance);
		}
	}


	public int getMapSquareAt(int x, int y) {
		if (x < 0 || y < 0) {
			//Settings.p("OOB!");
			return WALL;
		}
		
		int t = x + y*width;
		if(t < world.length) {
			return world[t];
		} else {
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
