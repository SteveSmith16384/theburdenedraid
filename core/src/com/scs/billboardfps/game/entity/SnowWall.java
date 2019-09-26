package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.components.IAttackable;
import com.scs.billboardfps.game.components.IDamagable;

public class SnowWall extends Entity implements IAttackable, IDamagable {

    private Decal destroyedDecal;
    private boolean attackable = true;

    public SnowWall(TextureRegion[][] tex, int x, int y) {
        super(tex, x, y, 2,2);

        destroyedDecal = Decal.newDecal(tex[3][2], true);
    }

    @Override
    public void damaged(int amt, Vector3 direction) {
    	Game.world.world[world_x + world_y * Game.world.width] = World.NOTHING;
        attackable = false;
        decalEntity.decal = destroyedDecal;
        Game.audio.play("hurt");
    }

	@Override
	public boolean isAttackable() {
		return attackable;
	}

	@Override
	public int getHealth() {
		return attackable ? 1 : 0;
	}


}
