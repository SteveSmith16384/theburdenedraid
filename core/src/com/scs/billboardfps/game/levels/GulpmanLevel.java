package com.scs.billboardfps.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.data.WorldSquare;
import com.scs.billboardfps.game.decals.DecalManager;
import com.scs.billboardfps.game.entities.EntityManager;
import com.scs.billboardfps.game.entities.androids.AndroidsAndroid;
import com.scs.billboardfps.game.entities.androids.GSquare;
import com.scs.billboardfps.game.entities.androids.SSquare;
import com.scs.billboardfps.game.entities.androids.SlidingDoor;
import com.scs.billboardfps.game.entities.gulpman.Cherry;
import com.scs.billboardfps.game.player.weapons.IPlayersWeapon;

public class GulpmanLevel extends AbstractLevel {

	public GulpmanLevel(EntityManager _entityManager, DecalManager _decalManager) {
		super(_entityManager, _decalManager);
	}


	@Override
	public void load(Game game) {
		entityManager.getEntities().clear();
		decalManager.clear();
		game.modelInstances = new ArrayList<ModelInstance>();

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
				} else {
					Cherry ch = new Cherry(x, z);
					game.ecs.addEntity(ch);
				}

				Game.world.world[x][z] = new WorldSquare();
				Game.world.world[x][z].type = type;
			}
		}
	}


	private void createWalls(Game game) {
		ModelBuilder modelBuilder = new ModelBuilder();

		Material black_material = new Material(TextureAttribute.createDiffuse(new Texture("androids/black.png")));		
		Model box_model = modelBuilder.createBox(Game.UNIT,Game.UNIT,Game.UNIT, black_material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

		for (int y = 0; y < map_height; y++) {
			for (int x = 0; x < map_width; x++) {
				try {
					int block = Game.world.world[x][y].type;
					if (block == World.WALL) {
						ModelInstance instance = new ModelInstance(box_model);
						instance.transform.translate(x*Game.UNIT, Game.UNIT/2f, y*Game.UNIT);
						instance.transform.rotate(Vector3.Z, 90);
						game.modelInstances.add(instance);
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
		}


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
	}


	@Override
	public void update(Game game, World world) {
	}


	@Override
	public void levelComplete() {

	}


	@Override
	public IPlayersWeapon getWeapon() {
		return null;//new PlayersLaserGun();
	}

}
