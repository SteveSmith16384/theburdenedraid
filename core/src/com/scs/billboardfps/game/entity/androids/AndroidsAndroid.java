package com.scs.billboardfps.game.entity.androids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.components.HasAI;
import com.scs.billboardfps.game.components.HasDecal;
import com.scs.billboardfps.game.components.IsDamagable;
import com.scs.billboardfps.game.components.MovementData;
import com.scs.billboardfps.game.components.PositionData;
import com.scs.billboardfps.game.systems.MobAISystem.Mode;

public class AndroidsAndroid extends AbstractEntity {

    public AndroidsAndroid(int x, int y) {
        super(AndroidsAndroid.class.getSimpleName());

        PositionData pos = new PositionData();
        pos.position = new Vector3(x*Game.UNIT, 0, y*Game.UNIT);
        this.addComponent(pos);
        
		HasDecal hasDecal = new HasDecal();
		Texture tex = new Texture(Gdx.files.internal("androids/android.png"));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
        hasDecal.decal = Decal.newDecal(tr, true);
        hasDecal.decal.setScale(Game.UNIT / tr.getRegionWidth()); // Scale to sq size by default
        hasDecal.decal.setPosition(pos.position);
        hasDecal.faceCamera = true;
        hasDecal.faceCameraTilted = true;        
        this.addComponent(hasDecal);
        
        IsDamagable damagable = new IsDamagable(2);
        this.addComponent(damagable);
        
        HasAI ai = new HasAI(Mode.MoveLikeRook, .3f);
        this.addComponent(ai);
        
        this.addComponent(new MovementData(.75f));
        
    }
    
/*
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
*/
}
