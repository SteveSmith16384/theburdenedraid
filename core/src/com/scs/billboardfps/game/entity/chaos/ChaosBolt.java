package com.scs.billboardfps.game.entity.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.Settings;
import com.scs.billboardfps.game.Art;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.decals.DecalEntity;
import com.scs.billboardfps.game.entity.Entity;
import com.scs.billboardfps.game.interfaces.IDamagable;

public class ChaosBolt extends Entity {

	private static final float SIZE = 0.3f;
	private static final float SPEED = 0.1f;

	public int tx, ty;
	private Vector3 dir;
	private Vector3 origPos = new Vector3();
	private Object shooter;
	private Decal[] decals = new Decal[4];
	private int decalIdx;
	private float animTimer = 0f;

	public ChaosBolt(Object _shooter, Vector3 pos, Vector3 _dir) {
		super(ChaosBolt.class.getSimpleName());

		shooter = _shooter;
		position = new Vector3(pos);
		dir = new Vector3(_dir);

		TextureRegion[][] tr = Art.createSheet("chaos/chaosbolt.png", 4, 1);

		this.decalEntity = new DecalEntity();
		for (int i=0 ; i<4 ; i++) {
			decals[i] = Decal.newDecal(tr[i][0], true);

			// Scale all
			this.decalEntity.decal = decals[i];
			decalEntity.setScalePropertionToSqSize(SIZE);
		}
		this.decalEntity.decal = decals[0];

		decalEntity.faceCameraTilted = true;

		this.sizeAsFracOfMapsquare = SIZE;
	}


	@Override
	public void update(World world) {
		float dt = Gdx.graphics.getDeltaTime();

		animTimer += dt;
		if (animTimer>.03f) {
			animTimer-=.03f;
			decalIdx++;
			if (decalIdx >= this.decals.length) {
				decalIdx = 0;
			}
			decalEntity.decal = decals[decalIdx];
		}

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
