package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.Game;

public class FloorSquare extends Entity {

	public ModelInstance instance;
	
	public FloorSquare(int map_x, int map_y) {
		super(FloorSquare.class.getSimpleName());
		
		Material material = new Material(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("androids/g_square.png"))));		

		ModelBuilder modelBuilder = new ModelBuilder();
		Model floor = modelBuilder.createRect(
				0f, 0f, Game.UNIT,
				Game.UNIT, 0f, Game.UNIT,
				Game.UNIT, 0f, 0f,
				0f, 0f,0f,
				1f, 1f,1f,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
		instance = new ModelInstance(floor);

		instance.transform.translate(map_x*Game.UNIT, 0.1f, map_y*Game.UNIT);
		//instance.transform.rotate(Vector3.Z, 90);
		//instance.userData = new RenderData(RenderData.ShaderType.FOG_COLOR, 1, tileType, 6, 6, 1f, 1f);

		position = new Vector3(Game.UNIT*map_x, 0, Game.UNIT*map_y);
	}



}
