package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.player.Player;
import net.evgiz.ld40.game.world.World;

public abstract class Enemy extends Entity {

    private float damageTimer = 0f;
    private Vector3 push; // Pushed away by attack
    private float pushScale = 0f;
    public int health = 3;

    public Enemy(TextureRegion[][] tex, int x, int y) {
        super(tex, x, y);

        attackable = true;

    }

    public Enemy(TextureRegion[][] tex, int x, int y, int tx, int ty) {
        super(tex, x, y, tx, ty);

        attackable = true;

    }

    public void damaged(Vector3 direction) {
        if(damageTimer>0) {
            return;
        }

        damageTimer = .5f;
        push = direction;
        pushScale = 5f;
        health--;

        if (health <= 0) {
            death();
        }else{
            Game.audio.play("hurt");
        }

    }
    

    public void death() {
        Game.audio.play("death");
        remove = true;
    }

    
    @Override
    public void update(World world) {
        float dt  = Gdx.graphics.getDeltaTime();

        Vector2 moveVec = new Vector2();

        if (pushScale>0) {
            moveVec.x += push.x * pushScale * dt * Game.UNIT;
            moveVec.y += push.z * pushScale * dt * Game.UNIT;
            pushScale -= dt * 5f;
        }

        if (damageTimer>0f) {
            if(Math.floor(damageTimer*8)%2 == 1) {
                decalEntity.decal.setColor(Color.RED);
            } else {
                decalEntity.decal.setColor(Color.WHITE);
            }
            damageTimer -= dt;
        } else {
            decalEntity.decal.setColor(Color.WHITE);
        }

        tryMove(world, moveVec, false);

    }

    
}
