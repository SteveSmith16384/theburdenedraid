package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.interfaces.IAttackable;
import com.scs.billboardfps.game.interfaces.IDamagable;
import com.scs.billboardfps.game.interfaces.IHarmsPlayer;

public abstract class Enemy extends Entity implements IDamagable, IAttackable, IHarmsPlayer {

	private float damageTimer = 0f;
	private Vector3 push; // Pushed away by attack
	private float pushScale = 0f;
	public int health = Settings.ENEMY_HEALTH;

	public Enemy(String name, int x, int y) {
		super(name, x, y);

	}

	public Enemy(String name, TextureRegion[][] tex, int x, int y) {
		super(name, tex, x, y);

	}


	public Enemy(String name, TextureRegion[][] tex, int x, int y, int tx, int ty) {
		super(name, tex, x, y, tx, ty);

	}

	public void damaged(int amt, Vector3 direction) {
		if(damageTimer>0) {
			return;
		}

		damageTimer = .5f;
		push = direction;
		pushScale = 5f;
		health -= amt;

		if (health <= 0) {
			death();
		}else{
			Game.audio.play("hurt");
		}

	}


	public void death() {
		Game.audio.play("death");
		remove = true;
	}


	@Override
	public void update(World world) {
		float dt  = Gdx.graphics.getDeltaTime();

		Vector2 moveVec = new Vector2();

		if (pushScale>0) {
			moveVec.x += push.x * pushScale * dt * Game.UNIT;
			moveVec.y += push.z * pushScale * dt * Game.UNIT;
			pushScale -= dt * 5f;
		}

		if (damageTimer>0f) {
			if(Math.floor(damageTimer*8)%2 == 1) {
				decalEntity.decal.setColor(Color.RED);
			} else {
				decalEntity.decal.setColor(Color.WHITE);
			}
			damageTimer -= dt;
		} else {
			decalEntity.decal.setColor(Color.WHITE);
		}

		tryMove(world, moveVec, false);

	}


	protected void shoot() {
		if (Settings.ENEMY_SHOOTING) {
			if (Game.world.canSee(this.position, Game.player.getPosition())) {
				Settings.p("Shooting!");
				Vector3 dir = new Vector3();
				dir.set(Game.player.getPosition()).sub(this.position).nor();
				Bullet b = new Bullet(this, this.position, dir);
				Game.entityManager.add(b);
			}
			//Settings.p("Not shooting - Cannot see player!");
		}
	}


	@Override
	public int getHealth() {
		return health;
	}

	
	@Override
	public boolean harmsPlayer() {
		return this.health > 0;
	}


	@Override
	public boolean isAttackable() {
		return this.health > 0;
	}

}
