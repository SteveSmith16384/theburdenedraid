package com.scs.lostinthegame.game.entities.ericandthefloaters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Art;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.components.HasAI;
import com.scs.lostinthegame.game.components.HasDecal;
import com.scs.lostinthegame.game.components.HasDecalCycle;
import com.scs.lostinthegame.game.components.IsDamagableNasty;
import com.scs.lostinthegame.game.components.MovementData;
import com.scs.lostinthegame.game.components.PositionData;
import com.scs.lostinthegame.game.systems.MobAISystem.Mode;

public class Floater extends AbstractEntity {

    public Floater(int x, int y) {
        super(Floater.class.getSimpleName());

        PositionData pos = new PositionData();
        pos.position = new Vector3(x*Game.UNIT, 0, y*Game.UNIT);
        this.addComponent(pos);
        
		HasDecal hasDecal = new HasDecal();
		Texture tex = new Texture(Gdx.files.internal("ericandthefloaters/floater1.png"));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
        hasDecal.decal = Decal.newDecal(tr, true);
        hasDecal.decal.setScale(Game.UNIT / tr.getRegionWidth()); // Scale to sq size by default
        hasDecal.faceCamera = true;
        hasDecal.faceCameraTilted = true;        
        this.addComponent(hasDecal);
        
        HasDecalCycle cycle = new HasDecalCycle(.5f, 2);
        cycle.decals[0] = hasDecal.decal;
        cycle.decals[1] = Art.DecalHelper("ericandthefloaters/floater2.png", 1f);
        this.addComponent(cycle);
        
        IsDamagableNasty damagable = new IsDamagableNasty(1);
        this.addComponent(damagable);
        
        HasAI ai = new HasAI(Mode.MoveLikeRook, 1f);
        this.addComponent(ai);
        
        this.addComponent(new MovementData(.85f));
        
    }
    
}
