package net.evgiz.ld40.game.player;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import net.evgiz.ld40.game.Game;

public class CarriedItem {

    public float rotation;
    public Sprite sprite;

    public Vector2 position;

    public CarriedItem(Texture src, int tx, int ty){
        int unit = (int)Game.UNIT;

        sprite = new Sprite(src, tx*unit, ty*unit, unit, unit);

        rotation = new Random().nextFloat()*40 - 20;
    }

}
