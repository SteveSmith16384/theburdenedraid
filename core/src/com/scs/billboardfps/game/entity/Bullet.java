package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.components.IDamagable;
import com.scs.billboardfps.game.decals.DecalEntity;

public class Bullet extends Entity {
	
	private static final float SIZE = 0.3f;
	private static final float SPEED = 0.1f;

	public int tx, ty;
	private Vector3 dir;
	private Vector3 origPos = new Vector3();
	private Object shooter;

	public Bullet(Object _shooter, Vector3 pos, Vector3 _dir) {
		super(Bullet.class.getSimpleName());

		shooter = _shooter;
		position = new Vector3(pos);
		dir = new Vector3(_dir);

		this.decalEntity = new DecalEntity("fireball.png");//tr);

		decalEntity.setScalePropertionToSqSize(SIZE);

		decalEntity.faceCameraTilted = true;
		
		this.sizeAsFracOfMapsquare = SIZE;
	}


	@Override
	public void update(World world) {
		//float dt = Gdx.graphics.getDeltaTime();

		origPos.set(this.position);
		Vector3 offset = new Vector3(dir);
		if (this.tryMove(world, offset.scl(SPEED), true) == false) {
			// Hit a wall
			Settings.p("Bullet hit wall");
			this.remove = true;
		} else {
			// Check for collisions
			for (Entity ent : Game.entityManager.getEntities()) {
				if (ent == shooter) {
					continue;
				}
				if (ent instanceof IDamagable) {
					IDamagable id = (IDamagable)ent;
					if (id.getHealth() <= 0) {
						continue;
					}

					if (ent.getPosition().dst2(position) < Game.UNIT*SIZE) {
						id.damaged(1, new Vector3()); // todo - direction of shooter 
						this.remove = true;
						Settings.p("Bullet hit " + ent);
						break;
					}
				}

			}

		}
	}


}
