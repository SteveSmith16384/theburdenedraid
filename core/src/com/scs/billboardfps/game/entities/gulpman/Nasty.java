package com.scs.billboardfps.game.entities.gulpman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.components.HarmsPlayer;
import com.scs.billboardfps.game.components.HasAI;
import com.scs.billboardfps.game.components.HasDecal;
import com.scs.billboardfps.game.components.IsDamagable;
import com.scs.billboardfps.game.components.MovementData;
import com.scs.billboardfps.game.components.PositionData;
import com.scs.billboardfps.game.systems.MobAISystem.Mode;

public class Nasty extends AbstractEntity {

    public Nasty(int x, int y) {
        super(Nasty.class.getSimpleName());

        PositionData pos = new PositionData();
        pos.position = new Vector3(x*Game.UNIT, 0, y*Game.UNIT);
        this.addComponent(pos);
        
		HasDecal hasDecal = new HasDecal();
		Texture tex = new Texture(Gdx.files.internal("gulpman/nasty.png"));
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
        
        this.addComponent(new HarmsPlayer(1));
        
    }
    
}
