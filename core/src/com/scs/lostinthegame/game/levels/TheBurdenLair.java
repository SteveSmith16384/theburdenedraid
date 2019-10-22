package com.scs.lostinthegame.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.data.WorldSquare;
import com.scs.lostinthegame.game.decals.DecalEntity;
import com.scs.lostinthegame.game.decals.DecalManager;
import com.scs.lostinthegame.game.entities.Entity;
import com.scs.lostinthegame.game.entities.EntityManager;
import com.scs.lostinthegame.game.entities.burdenlair.Bat;
import com.scs.lostinthegame.game.entities.burdenlair.Demon;
import com.scs.lostinthegame.game.entities.burdenlair.Door;
import com.scs.lostinthegame.game.entities.burdenlair.FlyingSkull;
import com.scs.lostinthegame.game.entities.burdenlair.Goal;
import com.scs.lostinthegame.game.entities.burdenlair.KeyEntity;
import com.scs.lostinthegame.game.entities.burdenlair.Ladder;
import com.scs.lostinthegame.game.entities.burdenlair.Lever;
import com.scs.lostinthegame.game.entities.burdenlair.LootEntity;
import com.scs.lostinthegame.game.entities.burdenlair.Slime;
import com.scs.lostinthegame.game.entities.burdenlair.SnowBarrier;
import com.scs.lostinthegame.game.entities.burdenlair.Statue;
import com.scs.lostinthegame.game.entities.chaos.Wraith;
import com.scs.lostinthegame.game.player.Inventory;
import com.scs.lostinthegame.game.player.weapons.IPlayersWeapon;
import com.scs.lostinthegame.game.player.weapons.PlayersSword;
import com.scs.lostinthegame.game.renderable.RenderData;

public class TheBurdenLair extends AbstractLevel {

	public static final String DEMON_LAIR = "Demon Lair";
	public static final String DUNGEONS = "Dungeons";
	public static String levelOrder[] = new String[] {
			"Dungeons", "Crypt", "Ice Caves", "level", DEMON_LAIR
	};


	private String targetLevel;
	public String previousLevelName;
	public String currentLevelName;

	private ArrayList<Lever> levers = new ArrayList<Lever>();
	private boolean solvedLevers = false;
	private boolean leverComplete = false;

	public ArrayList<ModelInstance> specialBlocks;

	public TheBurdenLair(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);

		targetLevel = Settings.START_LEVEL.length() > 0 ? Settings.START_LEVEL : levelOrder[0];

	}


	@Override
	public void load(Game game) {//String level) {
		specialBlocks = new ArrayList<ModelInstance>();

		previousLevelName = currentLevelName;
		currentLevelName = this.targetLevel;

		//entityManager.getEntities().clear();
		//decalManager.clear();

		Texture texture = new Texture(Gdx.files.internal("level/" + targetLevel + ".png"));
		texture.getTextureData().prepare();

		Pixmap pixmap = texture.getTextureData().consumePixmap();

		map_width = pixmap.getWidth();
		map_height = pixmap.getHeight();

		Game.world.world = new WorldSquare[map_width][map_height];

		for (int x = 0; x < map_width; x++) {
			for (int y = 0; y < map_height; y++) {
				Game.world.world[x][y] = new WorldSquare();

				int pix = pixmap.getPixel(x,y);
				int result = 0;

				boolean was_spawn = spawnEntity(Game.world, pix, x, y);

				switch (pix) {
				//Red
				case -16776961:
					playerStartMapX = x;
					playerStartMapY = y;
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
					result = World.BLOCKED;
					break;

				case 0xff: //Black
					result = World.WALL;//1;
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
					Game.world.world[x][y].type = result;
				}
			}
		}

		createWalls(game);

		if (currentLevelName.equals(DEMON_LAIR)) {
			return;
		}

		addDetail();
	}


	public boolean spawnEntity(World world, int col, int x, int y) {
		boolean result = true;

		switch (col) {
		//Red spawn
		case -16776961:
			Entity ent = new Ladder(Game.art.entities, x, y, null, true);
			this.entityManager.add(ent);
			break;
			//Blue finish
		case 2555903:
			Ladder lad = new Ladder(Game.art.entities, x, y, getNextLevel());
			this.entityManager.add(lad);
			break;
			//Green slime
		case 16720383:
			if (Settings.USE_WRAITHS) {
				this.entityManager.add(new Wraith(x , y));
			} else {
				this.entityManager.add(new Slime(Game.art.entities, x , y));
			}
			break;
			//Beige flying skull
		case -5079041:
			this.entityManager.add(new FlyingSkull(Game.art.entities, x , y));
			break;
			//Gray bat
		case 1077952767:
			this.entityManager.add(new Bat(Game.art.entities, x , y));
			break;
			//Red-beige demon
		case 2083598847:
			this.entityManager.add(new Demon(Game.art.entities, x , y));
			break;
			//Cyan snowblock
		case 16777215:
			SnowBarrier snowWall = new SnowBarrier(Game.art.entities, x , y);
			snowWall.bindWorldTile(world, x, y);
			this.entityManager.add(snowWall);
			break;
			//Orange door
		case -9830145:
			Door d = new Door(Game.art.entities, x, y);
			d.bindWorldTile(world, x, y);
			this.entityManager.add(d);
			break;
			//Brown-yellow lever
		case 2137653503:
			Lever lever = new Lever(Game.art.entities, x, y);
			levers.add(lever);
			this.entityManager.add(lever);
			break;
		case 2140340223:
			this.entityManager.add(new Statue(Game.art.entities, x, y));
			break;
			//Yellow key
		case -2621185:
			KeyEntity item = new KeyEntity(Game.art.items, x, y, 0, 0);
			this.entityManager.add(item);
			break;

			//Details, barrel/pot etc
		case -1600085761:
			Entity det = new Entity("RandomLoot", Game.art.entities, x, y, getTileType(currentLevelName), 5);
			this.entityManager.add(det);
			break;

			//Lightblue final goal
		case 9764863:
			Goal goal = new Goal(Game.art.entities, x, y);
			this.entityManager.add(goal);
			//Pink loot
		case -16720641:
			for (int i = 0; i < 5; i++) {
				Entity le = null;
				/*if (Settings.NEW_BARRELS) {
					le = new NewBarrel(x, y);
				} else {*/
				le = new LootEntity(Game.art.items, x, y);
				//}
				le.position.x += Settings.random.nextFloat()*Game.UNIT*1.5f - Game.UNIT*.75f;
				le.position.z += Settings.random.nextFloat()*Game.UNIT*1.5f - Game.UNIT*.75f;
				le.position.y += Settings.random.nextFloat()*Game.UNIT*.15;
				this.entityManager.add(le);
			}
			break;
		default:
			result = false;
			break;

		}
		return result;

	}


	private void addDetail() {
		int count = map_width*map_height / 3;

		for (int i = 0; i < count; i++) {
			int x = Settings.random.nextInt(map_width);
			int y = Settings.random.nextInt(map_height);

			if (Game.world.getMapSquareAt(x,y).type == World.NOTHING) {
				DecalEntity ent = new DecalEntity(Game.world.detailTexture[Settings.random.nextInt(2)]);
				ent.position.set(x * Game.UNIT, 0, y * Game.UNIT);
				decalManager.add(ent);
			} else {
				count++;
			}
		}
	}


	private void createWalls(Game game) {
		int tileType = getTileType(currentLevelName);

		ModelBuilder modelBuilder = new ModelBuilder();

		Texture tiles = new Texture(Gdx.files.internal("tiles.png"));
		Material material = new Material(TextureAttribute.createDiffuse(tiles));		
		Model box_model = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		game.modelInstances = new ArrayList<ModelInstance>();
		specialBlocks = new ArrayList<ModelInstance>();

		for (int x = 0; x < map_width; x++) {
			for (int y = 0; y < map_height; y++) {
				int block = Game.world.world[x][y].type;
				if (block == World.WALL) {
					ModelInstance instance = new ModelInstance(box_model);
					instance.transform.translate(x*Game.UNIT, Game.UNIT/2f, y*Game.UNIT);
					instance.transform.rotate(Vector3.Z, 90);
					instance.userData = new RenderData(RenderData.ShaderType.FOG_TEXTURE, 0, tileType, 6, 6);
					game.modelInstances.add(instance);
				} else if(block >= 3 && block <= 5) {
					ModelInstance instance = new ModelInstance(box_model);
					instance.transform.translate(x* Game.UNIT,Game.UNIT/2f, y*Game.UNIT);
					instance.transform.rotate(Vector3.Z, 90);
					instance.userData = new RenderData(RenderData.ShaderType.FOG_TEXTURE, 3+block-3, tileType, 6, 6);
					game.modelInstances.add(instance);
					specialBlocks.add(instance);
				}

			}
		}


		// Create floor
		Model floor = modelBuilder.createRect(
				0f,0f, (float) map_height*Game.UNIT,
				(float)map_width*Game.UNIT,0f, (float)map_height*Game.UNIT,
				(float)map_width*Game.UNIT, 0f, 0f,
				0f,0f,0f,
				1f,1f,1f,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		ModelInstance instance = new ModelInstance(floor);
		instance.userData = new RenderData(RenderData.ShaderType.FOG_COLOR, 1, tileType, 6, 6, map_width, map_height);
		game.modelInstances.add(instance);

		// ceiling
		instance = new ModelInstance(floor);
		instance.userData = new RenderData(RenderData.ShaderType.FOG_COLOR, 2, tileType, 6, 6, map_width, map_height);
		instance.transform.translate(0, Game.UNIT,0);
		instance.transform.rotate(Vector3.X, 180);
		instance.transform.translate(0,0,-(float)map_width* Game.UNIT);
		game.modelInstances.add(instance);
	}


	private int getTileType(String level) {
		for (int i = 0; i < levelOrder.length; i++) {
			if (levelOrder[i].equals(level)) {
				return i;
			}
		}
		return 0;
	}


	public String getNextLevel() {
		for (int i = 0; i < levelOrder.length-1; i++) {
			if (levelOrder[i].equals(currentLevelName)) {
				return levelOrder[i+1];
			}
		}

		return null;
	}


	@Override
	public void renderUI(SpriteBatch batch, BitmapFont font) {
    	Inventory inv = (Inventory)Game.player.inventory;
		for (int i = 0; i < inv.keys; i++) {
			batch.draw(Game.art.items[0][0], 10 + i*50, Gdx.graphics.getHeight()-40, 48, 48);
		}
	}
	
	
	@Override
	public void update(Game game, World world) {
		if (!solvedLevers && levers.size() > 0) {
			boolean isCorrect = true;
			for (int i = 0; i < levers.size(); i++) {
				if(levers.get(i).getState() != getTargetLeverState(currentLevelName, i)){
					isCorrect = false;
					break;
				}
			}

			if (isCorrect) {
				solvedLevers = true;
				System.out.println("SOLVED LEVERS!");
				Game.audio.play("wall_open");
				Game.audio.play("success");
			}

		}

		if (solvedLevers && !leverComplete) {
			// Move walls down
			Vector3 tmp = new Vector3();
			for (ModelInstance inst : specialBlocks) {
				inst.transform.translate(-Gdx.graphics.getDeltaTime()*Game.UNIT, 0, 0);
				inst.transform.getTranslation(tmp);
			}
			// Is wall movement complete?
			if (tmp.y < -Game.UNIT/1.9f) {
				for (int y = 0; y < map_height; y++) {
					for (int x = 0; x < map_width; x++) {
						if (world.world[x][y].type >=3 && world.world[x][y].type <=5) {
							world.world[x][y].type  = World.NOTHING;
						}
					}
				}
				leverComplete = true;
			}

		}


	}


	private int getTargetLeverState(String level, int lever) {
		if (level.equals(DUNGEONS)) {
			switch(lever) {
			case 0:
				return -1;
			case 1:
				return 1;
			case 2:
				return 0;
			}
		}
		//No target
		return -2;
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
		
	}


	@Override
	public String GetName() {
		return "bURDEN lAIR";
	}

}
