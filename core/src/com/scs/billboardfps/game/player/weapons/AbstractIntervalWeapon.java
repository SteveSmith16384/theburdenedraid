package com.scs.billboardfps.game.player.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.player.CameraController;

public abstract class AbstractIntervalWeapon implements IPlayersWeapon {

	private Sprite weaponSprite;
	private float timeSinceLastFire = 0;
	private final float shot_interval;

	public AbstractIntervalWeapon(String tex, float interval) {
		Texture weaponTex = new Texture(Gdx.files.internal(tex));
		this.shot_interval = interval;
		
		weaponSprite = new Sprite(weaponTex);
		weaponSprite.setOrigin(32, 20);
		weaponSprite.setScale(7.5f, 5f);
		weaponSprite.setPosition(Gdx.graphics.getWidth()-300, -20);
	}
	
	
	public void update(CameraController cameraController) {
		this.timeSinceLastFire += Gdx.graphics.getDeltaTime();
	}


	public void render(SpriteBatch batch) {
		weaponSprite.draw(batch);
	}


	public void attackPressed(Vector3 position, Vector3 direction) {
		if (timeSinceLastFire >= shot_interval) {
			timeSinceLastFire = 0;
			this.weaponFired(position, direction);
		}
	}
	
	
	protected abstract void weaponFired(Vector3 position, Vector3 direction);

}
