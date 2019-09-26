package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.decals.DecalEntity;

public class NewBarrel extends Entity {

	public NewBarrel(int map_x, int map_y) {
		super();
		
		Texture tex = new Texture(Gdx.files.internal("barrel.png"));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
		this.decalEntity = new DecalEntity(tr);
		decalEntity.setScalePropertionToSqSize(.6f);
		decalEntity.faceCamera = true;
		
		position = new Vector3(Game.UNIT*map_x, 0,Game.UNIT*map_y);

	}



}
