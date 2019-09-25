package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.World;

public final class Slime extends Enemy {

    private static final float speed = 3f;
    private static final float gravity = 10f * Game.UNIT;

    private Vector3 direction = new Vector3();

    private Decal idleDecal;
    private Decal jumpDecal;
    private Decal airDecal;

    private float jumpTimer = 0f;
    private float offsetY = 0f;
    
    private float shootTimer = 0;

    public Slime(TextureRegion[][] tex, int x, int y) {
        super(tex, x, y);

        idleDecal = decalEntity.decal;
        jumpDecal = Decal.newDecal(tex[1][0], true);
        airDecal = Decal.newDecal(tex[2][0], true);

        jumpTimer = Game.random.nextFloat() + .5f;
    }

    
    @Override
    public void death() {
        Game.audio.play("death");
        position.y = 0;
        decalEntity.decal = idleDecal;
        idleDecal.setColor(Color.DARK_GRAY);
        remove = false;

        Game.entityManager.add(new LootEntity(Game.art.items, position.x, position.z));
    }

    
    @Override
    public void update(World world) {
        super.update(world);

        if (health <= 0) {
            decalEntity.decal.setColor(Color.DARK_GRAY);
            return;
        }

        float dt = Gdx.graphics.getDeltaTime();
        
        if (Game.player.getPosition().dst2(position) < (Game.UNIT * 6) * (Game.UNIT * 6))
            jumpTimer -= dt;
        else {
            return;
        }
        
        shootTimer -= dt;
        if (shootTimer <= 0) {
        	shootTimer = 1f;        	
        	this.shoot();
        }
        
        if (jumpTimer <= 0f) {
            if (decalEntity.decal != airDecal) {
                decalEntity.decal = airDecal;
                offsetY = 2.5f * Game.UNIT;

                direction.set(Game.player.getPosition()).sub(position).nor();
                direction.y = 0f;

                direction.rotate(Vector3.Y, Game.random.nextFloat() * 20f - 10f);
                direction.scl(Gdx.graphics.getDeltaTime() * speed * Game.UNIT);

            }

            Vector2 moveVec = new Vector2();
            moveVec.x += direction.x;
            moveVec.y += direction.z;

            position.y += offsetY * Gdx.graphics.getDeltaTime();
            offsetY -= Gdx.graphics.getDeltaTime() * gravity;

            if (position.y <= 0 && offsetY <= 0) {
                position.y = 0;
                offsetY = 0;
                jumpTimer = Game.random.nextFloat() * 1.25f + .5f;
            }
            tryMove(world, moveVec, true);
        } else if (jumpTimer < .2f) {
            decalEntity.decal = jumpDecal;
        } else {
            decalEntity.decal = idleDecal;
        }


    }

}
