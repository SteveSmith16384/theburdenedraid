package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;

public final class Demon extends Enemy {

	private Vector3 direction = new Vector3();
	private Decal decal1, decal2;
	private float attackTimer = 0f;
	private float speed = 0f;

	public Demon(TextureRegion[][] tex, int x, int y) {
		super(tex, x, y, 4, 0);

		decal1 = decalEntity.decal;
		decal2 = Decal.newDecal(tex[5][0], true);

		health = 4;
	}


	@Override
	public void damaged(int amt, Vector3 direction) {
		super.damaged(amt, direction);

		direction.set(Game.player.getPosition()).sub(position).nor();
		direction.scl(Game.UNIT);
		direction.y = 0f;

		speed /=2;

	}

	@Override
	public void death() {
		super.death();

		Game.entityManager.dropLoot(position);//.add(new LootEntity(Game.art.items, position.x, position.z));
		Game.entityManager.dropLoot(position);//.add(new LootEntity(Game.art.items, position.x, position.z));

	}


	@Override
	public void update(World world) {
		super.update(world);

		if (health <= 0) {
			decalEntity.decal.setColor(Color.DARK_GRAY);
			return;
		}

		float dt = Gdx.graphics.getDeltaTime();
		if (Game.player.getPosition().dst2(position) < (Game.UNIT * 6) * (Game.UNIT * 6) && world.canSee(position, Game.player.getPosition())) {
			attackTimer += dt;
			if(attackTimer > 1f){
				if(speed==0){
					direction.set(Game.player.getPosition()).sub(position).nor();
					direction.scl(Game.UNIT);
					direction.y = 0f;
				}

				speed += dt*8f;
				if(speed>5) {
					speed=5;
				}

				decalEntity.decal = decal2;

				Vector2 moveVec = new Vector2();
				moveVec.x += direction.x * Gdx.graphics.getDeltaTime() * speed;
				moveVec.y += direction.z * Gdx.graphics.getDeltaTime() * speed;

				tryMove(world, moveVec, true);
			}

			if (attackTimer > 2f) {
				speed = 0;
				attackTimer = -Game.random.nextFloat();
				decalEntity.decal = decal1;
			}

		} else {
			// Can't see or out of range of player
			speed = 0;
			attackTimer = 0;
			decalEntity.decal = decal1;
		}


	}

}
