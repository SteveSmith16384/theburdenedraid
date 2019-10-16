package com.scs.lostinthegame.game.entities.burdenlair;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.World;
import com.scs.lostinthegame.game.entities.Entity;
import com.scs.lostinthegame.game.interfaces.IInteractable;
import com.scs.lostinthegame.game.player.Inventory;
import com.scs.lostinthegame.game.player.Player;

public final class Door extends Entity implements IInteractable {

    private Decal openDecal;
    private boolean interactable;
    private boolean locked = true;

    public Door(TextureRegion[][] tex, int x, int y) {
        super(Door.class.getSimpleName(), tex, x, y, 0,1);

        interactable = true;
        openDecal = Decal.newDecal(tex[1][1], true);

        decalEntity.faceCamera = false;
        decalEntity.setRotation(-90f);
    }
    

    @Override
    public void interact(Player player) {
    	Inventory inv = (Inventory)player.inventory;
        if (locked && inv.keys > 0) {
        	Game.world.world[world_x][world_y].type = World.NOTHING;
            decalEntity.decal = openDecal;
            interactable = false;
            inv.keys--;
            Game.audio.play("door");
        }
    }

    @Override
    public String getInteractText(Player player) {
    	Inventory inv = (Inventory)player.inventory;
        return (locked && inv.keys == 0) ? "Locked" : "Open with Key (E)";
    }


	@Override
	public boolean isInteractable() {
		return interactable;
	}

}
