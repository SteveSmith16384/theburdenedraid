package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;

public final class FlyingSkull extends Enemy {

    private static final float speed = 2f;

    private Vector3 direction = new Vector3();
    private Decal decal1, decal2;
    private float animTimer = 0f;

    public FlyingSkull(TextureRegion[][] tex, int x, int y) {
        super(tex, x, y, 0, 2);

        decal1 = decalEntity.decal;
        decal2 = Decal.newDecal(tex[1][2], true);

        health = 2;
    }
    

    @Override
    public void death() {
        super.death();
        Game.entityManager.dropLoot(position);//.add(new LootEntity(Game.art.items, position.x, position.z));
    }
    

    @Override
    public void update(World world) {
        super.update(world);

        if (health <= 0) {
            decalEntity.decal.setColor(Color.DARK_GRAY);
            return;
        }

        float dt = Gdx.graphics.getDeltaTime();

        if (Game.player.getPosition().dst2(position) < (Game.UNIT * 5) * (Game.UNIT * 5) && world.canSee(position, Game.player.getPosition())) {
            animTimer += dt;
            if(animTimer>.3f){
                animTimer-=.3f;
                decalEntity.decal = (decalEntity.decal==decal1) ? decal2 : decal1;
            }
            direction.set(Game.player.getPosition()).sub(position).nor();
            direction.scl(Gdx.graphics.getDeltaTime() * speed * Game.UNIT);
            direction.y = 0f;

            Vector2 moveVec = new Vector2();
            moveVec.x += direction.x;
            moveVec.y += direction.z;

            tryMove(world, moveVec, true);
        }


    }

}
