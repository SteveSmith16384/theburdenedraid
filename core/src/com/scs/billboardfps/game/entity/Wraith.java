package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.scs.billboardfps.game.Art;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.decals.DecalEntity;

public class Wraith extends Enemy {

    private static final float speed = 2f;

    private Vector3 direction = new Vector3();
    private Decal[] decals = new Decal[4];
    private int decalIdx;
    private float animTimer = 0f;

    public Wraith(int x, int y) {
        super(x, y);

        TextureRegion[][] tr = Art.createSheet("chaoswraith.png", 4, 1);

        for (int i=0 ; i<4 ; i++) {
        	decals[i] = Decal.newDecal(tr[i][0], true);
        }
        this.decalEntity = new DecalEntity();
        this.decalEntity.decal = decals[0];
        //decal2 = Decal.newDecal(tex[1][2], true);

        health = 4;
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
                decalIdx++;
                if (decalIdx >= this.decals.length) {
                	decalIdx = 0;
                }
                decalEntity.decal = decals[decalIdx];//(decalEntity.decal==decal1) ? decal2 : decal1;
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
