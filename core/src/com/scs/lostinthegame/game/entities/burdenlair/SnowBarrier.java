package com.scs.lostinthegame.game.entities.burdenlair;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.entities.Entity;
import com.scs.lostinthegame.game.interfaces.IAttackable;
import com.scs.lostinthegame.game.interfaces.IDamagable;

public class SnowBarrier extends Entity implements IAttackable, IDamagable {

    private Decal destroyedDecal;
    private boolean attackable = true;

    public SnowBarrier(TextureRegion[][] tex, int x, int y) {
        super(SnowBarrier.class.getSimpleName(), tex, x, y, 2,2);

        destroyedDecal = Decal.newDecal(tex[3][2], true);
    }

    
    @Override
    public void damaged(int amt, Vector3 direction) {
    	Game.world.world[world_x][world_y].blocked = false;
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
