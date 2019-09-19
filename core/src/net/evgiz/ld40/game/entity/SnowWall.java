package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.player.Player;
import net.evgiz.ld40.game.world.World;

public class SnowWall extends Entity {

    //private boolean open = false;
    //private float openProcess = 0.1f;
    private Decal destroyedDecal;

    public SnowWall(TextureRegion[][] tex, int x, int y) {
        super(tex, x, y, 2,2);

        destroyedDecal = Decal.newDecal(tex[3][2], true);

        attackable = true;
    }

    @Override
    public void damaged(Player player) {
        world.world[world_x + world_y * world.width] = World.NOTHING;
        attackable = false;
        decalEntity.decal = destroyedDecal;
        Game.audio.play("hurt");
    }


}
