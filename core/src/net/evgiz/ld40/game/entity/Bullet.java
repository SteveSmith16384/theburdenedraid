package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.decals.DecalEntity;
import net.evgiz.ld40.game.player.Player;
import net.evgiz.ld40.game.world.World;

public class Bullet extends Entity {

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
		position = new Vector3(pos);
		dir = new Vector3(_dir);

		decalEntity.faceCameraTilted = true;
		decalEntity.decal.setScale(decalEntity.decal.getScaleX()/2f);
		//position.y = -Game.UNIT/2f;
	}


	public void update(World world, Player player) {
		//float dt = Gdx.graphics.getDeltaTime();

		origPos.set(this.position);
		Vector3 offset = new Vector3(dir);
		if (this.tryMove(world, offset.scl(.5f), true) == false) {
			// Hit a wall
			this.remove = true;
		} else {
			// Check for collisions
			for (Entity ent : player.entityManager.getEntities()) {
				if (ent == shooter) {
					continue;
				}
				if (!(ent instanceof Enemy) || ((Enemy)ent).health<=0) {
					continue;
				}

				if (ent.getPosition().dst2(position) < Game.UNIT/2f) {
					// todo - harm enemy
					this.remove = true;
					break;
				}

			}

		}
	}


}
