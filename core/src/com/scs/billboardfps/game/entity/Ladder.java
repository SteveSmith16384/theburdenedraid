package com.scs.billboardfps.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scs.billboardfps.game.Game;
import com.scs.billboardfps.game.World;
import com.scs.billboardfps.game.interfaces.IInteractable;
import com.scs.billboardfps.game.player.Player;

public class Ladder extends Entity implements IInteractable {

    private String targetLevel;// = "nil";
    public boolean upLadder = false;

    public Ladder(TextureRegion[][] tex, int x, int y, String level) {
    	this(tex, x, y, level, false);
        /*super(tex, x, y, 3, 1);

        targetLevel = level;*/
    }
    

    public Ladder(TextureRegion[][] tex, int x, int y, String level, boolean upLadder) {
        super(Ladder.class.getSimpleName(), tex, x, y, upLadder ? 2 : 3, 1);

        targetLevel = level;

        this.upLadder = upLadder;

    }

    
    @Override
    public void interact(Player player) {
        if(upLadder || targetLevel == null) {
        	return;
        }

        player.getPosition().x = Game.gameLevel.getPlayerStartX()*Game.UNIT;
        player.getPosition().z = Game.gameLevel.getPlayerStartY()*Game.UNIT;
        Game.audio.play("ladder");
        Game.changeLevel(targetLevel);
    }
    

    @Override
    public String getInteractText(Player player) {
        return (targetLevel==null || upLadder) ? "" : "Enter "+targetLevel+" (E)";
    }

    
    @Override
    public void update(World world) {
        if(upLadder){
            if(Game.player.getPosition().dst2(position)>Game.UNIT*Game.UNIT*.5f*.5f) {
                upLadder = false;
            }
        }
    }


    @Override
	public boolean isInteractable() {
		return true;
	}

}

