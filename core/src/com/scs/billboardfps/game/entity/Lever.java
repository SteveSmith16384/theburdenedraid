package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.interfaces.IInteractable;
import com.scs.billboardfps.game.player.Player;

public class Lever extends Entity implements IInteractable {

	public boolean locked = true;
    private Decal left, center, right;
    private int direction = 1;
    private int state = -1;

    public Lever(TextureRegion[][] tex, int x, int y) {
        super(Lever.class.getSimpleName(), tex, x, y, 0,4);

        left = decalEntity.decal;
        center = Decal.newDecal(tex[1][4], true);
        right = Decal.newDecal(tex[2][4], true);

    }

    public int getState(){
        return state;
    }

    @Override
    public void interact(Player player) {
        if (decalEntity.decal == left) {
            direction = 1;
            decalEntity.decal = center;
            state = 0;
        } else if(decalEntity.decal == center) {
            if(direction == 1) {
                decalEntity.decal = right;
                state = 1;
            }else {
                decalEntity.decal = left;
                state = -1;
            }
        } else {
            decalEntity.decal = center;
            direction = -1;
            state = 0;
        }
        Game.audio.play("door");
    }
    

    @Override
    public String getInteractText(Player player) {
        return "Use Lever (E)";
    }


    @Override
	public boolean isInteractable() {
		return true;
	}

}
