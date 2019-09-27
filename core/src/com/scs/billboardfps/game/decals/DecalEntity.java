package com.scs.billboardfps.game.decals;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.Game;

public class DecalEntity {

	//private static final float scale = 16f / Game.UNIT;

	public Decal decal;
	public boolean faceCamera = true;
	public boolean faceCameraTilted = false; // Whether to loop up to player on y_axis

	public Vector3 position;
	private float rotation = 0f;
	private Vector3 tmp = new Vector3();

	public DecalEntity() {
		position = new Vector3(0, 0, 0);
	}

	
	public DecalEntity(TextureRegion reg) {
		this();
		
		decal = Decal.newDecal(reg, true);
		decal.setScale(Game.UNIT / reg.getRegionWidth()); // Scale to sq size by default
		decal.setPosition(position);
	}
	

	public DecalEntity(String filename) {
		this();

		Texture tex = new Texture(Gdx.files.internal(filename));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());

		decal = Decal.newDecal(tr, true);
		decal.setScale(Game.UNIT / tr.getRegionWidth()); // Scale to sq size by default
		decal.setPosition(position);
	}
	

	public void setPosition(float x, float y) {
		setPosition(x, position.y, y);
	}


	public void setScalePropertionToSqSize(float s) {
		//decal.transformationOffset = new Vector2(0, 0);//.y = 0;
		decal.setScale((Game.UNIT / decal.getWidth()) * s);
	}
	
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}


	public void setRotation(float f) {
		rotation = f;
	}

	
	public void setRotationRandom() {
		setRotation(new Random().nextFloat()*360f);
	}


	public void updateTransform(Camera cam) {
		if(faceCamera) {
			tmp.set(cam.direction).scl(-1);
			if(!faceCameraTilted) {
				tmp.y = 0;
			}
			decal.setRotation(tmp, Vector3.Y);
			decal.rotateY(rotation);
		}else{
			decal.setRotationY(rotation);
		}

		decal.setPosition(position);
		decal.translateY(.5f * Game.UNIT);

	}


}
