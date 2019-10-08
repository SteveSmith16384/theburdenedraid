package com.scs.billboardfps.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.entity.Entity;
import com.scs.billboardfps.game.entity.chaos.ChaosBolt;

public class PlayersWeapon {

	private static final float defaultWeaponRotation = 30f;
	private static final float chargeWeaponRotation = -20f;
	private static final float attackWeaponRotation = 120f;

	private Sprite weaponSprite;
	private float weaponRotation;
	private Vector2 weaponPosition;
	private float attackAnimation;
	private float weaponScaleY = 1f;
	private boolean didPlayAudio = false;
	private boolean didAttack = true;

	public PlayersWeapon() {
		if (Settings.USE_WAND) {
			Texture weaponTex = new Texture(Gdx.files.internal("chaos/wand2.png"));
			weaponSprite = new Sprite(weaponTex);
			weaponSprite.setOrigin(32, 20);
			weaponSprite.setScale(7.5f, 5f);
			weaponPosition = new Vector2(0,0);
		} else {
			Texture weaponTex = new Texture(Gdx.files.internal("sword.png"));
			weaponSprite = new Sprite(weaponTex);
			weaponSprite.setOrigin(32, 0);
			weaponSprite.setScale(7.5f, 5f);
			weaponPosition = new Vector2(0,0);
		}

		weaponRotation = defaultWeaponRotation;
		weaponSprite.setRotation(defaultWeaponRotation);

	}


	public void update(CameraController cameraController) {
		float weaponBob = (float)Math.cos(cameraController.bobbing * 15f + .15f) * 20f;
		weaponSprite.setPosition(Gdx.graphics.getWidth()-300 + weaponPosition.x, -20 + weaponBob+weaponPosition.y);
		weaponSprite.setScale(6f, Math.min(5f*weaponScaleY,8f));
		weaponSprite.setRotation(weaponRotation + (float)Math.cos(cameraController.bobbing*7.5f)*5f - 2.5f);
	}

	
	public void render(SpriteBatch batch){
		weaponSprite.draw(batch);
	}

	
	public void attackPressed() {
		if (attackAnimation <= 0f) {
			attackAnimation = 1.0f;
			didAttack = false;
			didPlayAudio = false;
		}
	}
	
	
	public boolean IsAttackMade(Player player) {
		boolean res = (attackAnimation < 0.3f && !didAttack);
		if (res) {
			didAttack = true;

			if (Settings.PLAYER_SHOOTING) {
				Entity b = new ChaosBolt(this, player.position, player.camera.direction);
				Game.entityManager.add(b);
			}
		}
		return res;
	}

	
	public void checkForAttack() {
		if (attackAnimation > 0f) {
			attackAnimation -= Gdx.graphics.getDeltaTime()*4;
			if(attackAnimation > 0.3f) {
				weaponRotation = MathUtils.lerp(weaponRotation, chargeWeaponRotation, Gdx.graphics.getDeltaTime() * 8f);
				weaponPosition.set(
						MathUtils.lerp(weaponPosition.x, 30, Gdx.graphics.getDeltaTime()*20f),
						MathUtils.lerp(weaponPosition.y, -80, Gdx.graphics.getDeltaTime()*20f)
						);

				if(!didPlayAudio && attackAnimation<.8f){
					didPlayAudio = true;
					Game.audio.play("weapon");
				}

			} else {
				weaponRotation = MathUtils.lerp(weaponRotation, attackWeaponRotation, Gdx.graphics.getDeltaTime() * 15f);
				weaponPosition.set(
						MathUtils.lerp(weaponPosition.x, -150, Gdx.graphics.getDeltaTime()*20f),
						MathUtils.lerp(weaponPosition.y, 150, Gdx.graphics.getDeltaTime()*20f)
						);
				weaponScaleY = MathUtils.lerp(weaponScaleY, 2f, Gdx.graphics.getDeltaTime()*3);

				//In case of low framerate skip, unlikely
				if(!didPlayAudio){
					didPlayAudio = true;
					Game.audio.play("weapon");
				}
			}
		} else {
			weaponRotation = MathUtils.lerp(weaponRotation, defaultWeaponRotation, Gdx.graphics.getDeltaTime()*5f);
			weaponPosition.set(
					MathUtils.lerp(weaponPosition.x, 0, Gdx.graphics.getDeltaTime()*10f),
					MathUtils.lerp(weaponPosition.y, 0, Gdx.graphics.getDeltaTime()*10f)
					);
			weaponScaleY = MathUtils.lerp(weaponScaleY, 1f, Gdx.graphics.getDeltaTime()*10);
		}

	}

}
