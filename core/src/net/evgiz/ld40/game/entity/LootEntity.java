package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.World;
import net.evgiz.ld40.game.decals.DecalEntity;

public final class LootEntity extends Entity {

	private static final int MAX_LOOT_TYPES = 15;

	public int tx, ty;
	public boolean isAttracted = false;
	private float attractTime = 0f;
	public float attractWaitTime = .3f;
	private int type;

	public LootEntity(TextureRegion[][] tex, int x, int y) {
		this(tex, (float)x, (float)y);
	}

	
	public LootEntity(TextureRegion[][] tex, float x, float y) {
		texture = tex;

		type = Game.random.nextInt(MAX_LOOT_TYPES)+1;
		tx = type%4;
		ty = type/4;

		decalEntity = new DecalEntity(tex[tx][ty]);
		position = new Vector3(x+Game.random.nextFloat()*Game.UNIT/2-Game.UNIT/4, 0, y+Game.random.nextFloat()*Game.UNIT/2-Game.UNIT/4);

		decalEntity.faceCameraTilted = true;
		decalEntity.decal.setScale(decalEntity.decal.getScaleX()/2f);
		position.y = -Game.UNIT/3f;
	}


	@Override
	public void update(World wrld) {
		if (attractWaitTime > 0) {
			attractWaitTime -= Gdx.graphics.getDeltaTime();
		} else if (isAttracted) {
			position.set(
					MathUtils.lerp(position.x, Game.player.getPosition().x, Gdx.graphics.getDeltaTime()*15f),
					MathUtils.lerp(position.y, -Game.UNIT/4f, Gdx.graphics.getDeltaTime()*15f),
					MathUtils.lerp(position.z, Game.player.getPosition().z, Gdx.graphics.getDeltaTime()*15f)
					);

			attractTime += Gdx.graphics.getDeltaTime();

			float d = Game.UNIT/3f;
			if (attractTime>2f || Game.player.getPosition().dst2(position) < d*d) {
				// Collected
				System.out.println("Collected type " + type);
				remove = true;

				if (type == 1) { // todo - set correctly
					Game.player.resetHealth();//.health = Settings.START_HEALTH;
				} else {
					Game.player.inventory.addLoot(tx,ty);
				}

				Game.audio.play("loot");
			}
		} else if (Game.player.getPosition().dst2(position) < Game.UNIT*Game.UNIT){
			isAttracted = true;
		}
	}

}
