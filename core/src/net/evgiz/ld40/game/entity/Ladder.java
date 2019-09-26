package net.evgiz.ld40.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.evgiz.ld40.game.Game;
import net.evgiz.ld40.game.World;
import net.evgiz.ld40.game.components.IInteractable;
import net.evgiz.ld40.game.player.Player;

public class Ladder extends Entity implements IInteractable {

    private String targetLevel;// = "nil";
    public boolean upLadder = false;

    public Ladder(TextureRegion[][] tex, int x, int y, String level) {
    	this(tex, x, y, level, false);
        /*super(tex, x, y, 3, 1);

        targetLevel = level;*/
    }
    

    public Ladder(TextureRegion[][] tex, int x, int y, String level, boolean upLadder) {
        super(tex, x, y, upLadder ? 2 : 3, 1);

        targetLevel = level;

        this.upLadder = upLadder;

    }

    
    @Override
    public void interact(Player player) {
        if(upLadder || targetLevel == null) {
        	return;
        }

        player.getPosition().x = Game.world.playersStartX*Game.UNIT;
        player.getPosition().z = Game.world.playerStartY*Game.UNIT;
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

