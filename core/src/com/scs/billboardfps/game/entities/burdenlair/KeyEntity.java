package com.scs.billboardfps.game.entities.burdenlair;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.entities.Entity;
import com.scs.billboardfps.game.interfaces.IInteractable;
import com.scs.billboardfps.game.player.Inventory;
import com.scs.billboardfps.game.player.Player;

public class KeyEntity extends Entity implements IInteractable {

    public KeyEntity(TextureRegion[][] tex, int x, int y, int tx, int ty) {
        super("Key", tex, x, y, tx, ty);

        decalEntity.faceCameraTilted = true;

        decalEntity.decal.setScale(decalEntity.decal.getScaleX()/2f);

        position.y = -Game.UNIT/3f;

    }

    @Override
    public String getInteractText(Player player) {
        return "Pick Up (E)";
    }
    

    @Override
    public void interact(Player player) {
        remove = true;
    	Inventory inv = (Inventory)player.inventory;
        inv.keys++;
        Game.audio.play("pickup");

    }


    @Override
	public boolean isInteractable() {
		return true;
	}

}
