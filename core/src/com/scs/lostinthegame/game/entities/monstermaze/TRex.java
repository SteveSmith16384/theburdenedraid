package com.scs.lostinthegame.game.entities.monstermaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.Art;
import com.scs.lostinthegame.game.Game;
import com.scs.lostinthegame.game.components.HarmsPlayer;
import com.scs.lostinthegame.game.components.HasAI;
import com.scs.lostinthegame.game.components.HasDecal;
import com.scs.lostinthegame.game.components.HasDecalCycle;
import com.scs.lostinthegame.game.components.MovementData;
import com.scs.lostinthegame.game.components.PositionData;
import com.scs.lostinthegame.game.systems.MobAISystem.Mode;

public class TRex extends AbstractEntity {

    public TRex(int x, int y) {
        super(TRex.class.getSimpleName());

        PositionData pos = new PositionData();
        pos.position = new Vector3(x*Game.UNIT+(Game.UNIT/2), 0, y*Game.UNIT+(Game.UNIT/2));
        this.addComponent(pos);
        
		HasDecal hasDecal = new HasDecal();
		Texture tex = new Texture(Gdx.files.internal("monstermaze/trex1.png"));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
        hasDecal.decal = Decal.newDecal(tr, true);
        hasDecal.decal.setScale(Game.UNIT / tr.getRegionWidth()); // Scale to sq size by default
        hasDecal.faceCamera = true;
        hasDecal.faceCameraTilted = true;        
        this.addComponent(hasDecal);
        
        HasDecalCycle cycle = new HasDecalCycle(.5f, 2);
        cycle.decals[0] = hasDecal.decal;
        cycle.decals[1] = Art.DecalHelper("monstermaze/trex2.png", 1f);
        this.addComponent(cycle);
        
        HasAI ai = new HasAI(Mode.GoForPlayerIfClose, 1.5f, Game.UNIT*7f);
        this.addComponent(ai);
        
        this.addComponent(new MovementData(.85f));

        this.addComponent(new HarmsPlayer(1));
    }
    
}
