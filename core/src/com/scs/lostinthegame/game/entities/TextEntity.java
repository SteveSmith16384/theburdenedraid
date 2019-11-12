package com.scs.lostinthegame.game.entities;

import com.scs.basicecs.AbstractEntity;
import com.scs.lostinthegame.game.components.DrawTextData;

public class TextEntity extends AbstractEntity {

	public TextEntity(String text, float _x, float _y, float _duration) {
		super("Text");

		DrawTextData dtd = new DrawTextData();
		dtd.text = text;
		dtd.x = _x;
		dtd.y = _y;
		dtd.drawUntil = _duration;

		this.addComponent(dtd);
	}


	public TextEntity(String text, float _y, float _duration) {
		super("Text");

		DrawTextData dtd = new DrawTextData();
		dtd.text = text;
		dtd.centre_x = true;
		dtd.y = _y;
		dtd.drawUntil = _duration;

		this.addComponent(dtd);
	}

}
