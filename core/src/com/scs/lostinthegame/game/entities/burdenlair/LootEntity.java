package com.scs.lostinthegame.game.entities.burdenlair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.scs.lostinthegame.Settings;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.decals.DecalEntity;
import com.scs.lostinthegame.game.entities.Entity;
import com.scs.lostinthegame.game.player.Inventory;

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
		super(LootEntity.class.getSimpleName());
		
		textureRegion = tex;

		type = Settings.random.nextInt(MAX_LOOT_TYPES)+1;
		tx = type%4;
		ty = type/4;

		decalEntity = new DecalEntity(tex[tx][ty]);
		position = new Vector3(x+Settings.random.nextFloat()*Game.UNIT/2-Game.UNIT/4, 0, y+Settings.random.nextFloat()*Game.UNIT/2-Game.UNIT/4);

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
			if (attractTime > 2f || Game.player.getPosition().dst2(position) < d*d) {
				// Collected
				System.out.println("Collected type " + type);
				remove = true;

			    	Inventory inv = (Inventory)Game.player.inventory;
					inv.addLoot(tx,ty);

				Game.audio.play("loot");
			}
		} else if (Game.player.getPosition().dst2(position) < Game.UNIT*Game.UNIT){
			isAttracted = true;
		}
	}

}
