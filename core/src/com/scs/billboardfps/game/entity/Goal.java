package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;

public final class Goal extends Entity {

    public Goal(TextureRegion[][] tex, int x, int y) {
        super(tex, x, y);
    }


    @Override
    public void update(World world) {
        decalEntity.decal.setPosition(0,10,10);

        if(position.dst2(Game.player.getPosition())< (Game.UNIT*2)*(Game.UNIT*2)) {
            Game.player.inventory.gameComplete = true;

            // Attract all loot close by
            for (Entity ent : Game.entityManager.getEntities()) {
                if (ent instanceof LootEntity){
                    if(ent.position.dst2(Game.player.getPosition())<(Game.UNIT*5)*(Game.UNIT*5)){
                        ((LootEntity)ent).isAttracted = true;
                        ((LootEntity)ent).attractWaitTime = Game.random.nextFloat();
                    }
                }
            }

            remove = true;

            Game.gameComplete = true;

        }


    }

}
