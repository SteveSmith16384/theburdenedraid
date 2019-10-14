package com.scs.billboardfps.game.levels;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.scs.basicecs.AbstractEntity;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.data.WorldSquare;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entities.EntityManager;
import com.scs.billboardfps.game.entities.Wall;
import com.scs.billboardfps.game.entities.ericandthefloaters.EricBombDropper;
import com.scs.billboardfps.game.entities.ericandthefloaters.Floater;
import com.scs.billboardfps.game.player.weapons.IPlayersWeapon;

public class EricAndTheFloatersLevel extends AbstractLevel {

	public EricAndTheFloatersLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {
		entityManager.getEntities().clear();
		decalManager.clear();

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
				} else if (x == 3 && z == 1) {
					Floater floater = new Floater(x, z);
					game.ecs.addEntity(floater);
				}

				Game.world.world[x][z] = new WorldSquare();
				Game.world.world[x][z].type = type;
			}
		}
	}


	private void createWalls(Game game) {
		/*ModelBuilder modelBuilder = new ModelBuilder();

		Material outer_wall_material = new Material(TextureAttribute.createDiffuse(new Texture("ericandthefloaters/ericouterwall.png")));		
		Material inner_wall_material = new Material(TextureAttribute.createDiffuse(new Texture("ericandthefloaters/ericwall.png")));		
		//Material black_material = new Material(TextureAttribute.createDiffuse(new Texture("ericandthefloaters/black.png")));
		Model outer_wall_box_model = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, outer_wall_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		Model inner_wall_box_model = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, inner_wall_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		game.modelInstances = new ArrayList<ModelInstance>();
		 */
		for (int z = 0; z < map_height; z++) {
			for (int x = 0; x < map_width; x++) {
				try {
					int block = Game.world.world[x][z].type;
					if (block == World.WALL) {
						ModelInstance instance = null;
						if (x == 0 || z == 0 || x >= map_width-1 || z >= map_height-1) {
							game.ecs.addEntity(new Wall("ericandthefloaters/ericouterwall.png", x, z));
							//instance = new ModelInstance(outer_wall_box_model);
						} else {
							game.ecs.addEntity(new Wall("ericandthefloaters/ericwall.png", x, z));
							//instance = new ModelInstance(inner_wall_box_model);
						}		
						/*
						instance.transform.translate(x*Game.UNIT, Game.UNIT/2f, z*Game.UNIT);
						instance.transform.rotate(Vector3.Z, 90);
						game.modelInstances.add(instance);*/
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}

		/*
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
		game.modelInstances.add(instance);

		// ceiling
		instance = new ModelInstance(floor);
		instance.transform.translate(0, Game.UNIT,0);
		instance.transform.rotate(Vector3.X, 180);
		instance.transform.translate(0,0,-(float)map_width* Game.UNIT);
		game.modelInstances.add(instance);
		 */
	}


	@Override
	public IPlayersWeapon getWeapon() {
		return new EricBombDropper();
	}


	@Override
	public void levelComplete() {
	}


	@Override
	public void entityCollected(AbstractEntity collector, AbstractEntity collectable) {
		
	}

}
