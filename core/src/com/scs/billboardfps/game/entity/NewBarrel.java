package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.decals.DecalEntity;

public class NewBarrel extends Entity {

	public NewBarrel(int map_x, int map_y) {
		super();
		
		Texture tex = new Texture(Gdx.files.internal("red.png"));//barrel.png"));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
		this.decalEntity = new DecalEntity(tr);
		decalEntity.decal.transformationOffset = new Vector2(.5f, .5f); // todo - fix this
		decalEntity.setScalePropertionToSqSize(.6f);
		decalEntity.faceCamera = true;
		
		position = new Vector3(Game.UNIT*map_x, 0, Game.UNIT*map_y);

	}



}
