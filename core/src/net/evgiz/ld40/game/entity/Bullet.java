package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import net.evgiz.ld40.Settings;
import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.World;
import net.evgiz.ld40.game.components.IDamagable;
import net.evgiz.ld40.game.decals.DecalEntity;

public class Bullet extends Entity {
	
	private static final float SPEED = 0.1f;

	public int tx, ty;
	private Vector3 dir;
	private Vector3 origPos = new Vector3();
	private Object shooter;

	public Bullet(Object _shooter, TextureRegion[][] tex, Vector3 pos, Vector3 _dir) {
		super();

		shooter = _shooter;
		texture = tex;

		tx = 1;// todo numb%4;
		ty = 1;// todo numb/4;

		decalEntity = new DecalEntity(tex[tx][ty]);
		decalEntity.decal.setScale(.4f);
		position = new Vector3(pos);
		dir = new Vector3(_dir);

		decalEntity.faceCameraTilted = true;
		decalEntity.decal.setScale(decalEntity.decal.getScaleX()/2f);
		//position.y = .5f;//0.1ffGame.UNIT/2f;
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
				/*if (!(ent instanceof Enemy) || ((Enemy)ent).health<=0) {
					continue;
				}*/
				if (ent instanceof IDamagable) {
					IDamagable id = (IDamagable)ent;
					if (id.getHealth() <= 0) {
						continue;
					}

					if (ent.getPosition().dst2(position) < Game.UNIT/2f) {
						//id.decHealth(1);
						this.remove = true;
						Settings.p("Bullet hit " + ent);
						break;
					}
				}

			}

		}
	}


}
