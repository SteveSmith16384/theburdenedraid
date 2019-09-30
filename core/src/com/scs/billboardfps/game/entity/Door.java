package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.components.IInteractable;
import com.scs.billboardfps.game.player.Player;

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
    public void interact(Player player){
        if (locked && player.inventory.keys>0) {
        	Game.world.world[world_x + world_y * Game.world.width].type = World.NOTHING;
            decalEntity.decal = openDecal;
            interactable = false;
            player.inventory.keys--;
            Game.audio.play("door");
        }
    }

    @Override
    public String getInteractText(Player player) {
        return (locked && player.inventory.keys == 0) ? "Locked" : "Open with Key (E)";
    }


	@Override
	public boolean isInteractable() {
		return interactable;
	}

}
