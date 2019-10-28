package com.scs.lostinthegame.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.AbstractSystem;
import com.scs.basicecs.BasicECS;
import com.scs.lostinthegame.game.components.DrawTextData;

public class DrawTextSystem extends AbstractSystem {

	private SpriteBatch batch2d;
	private BitmapFont font_white;

	public DrawTextSystem(BasicECS ecs, SpriteBatch _batch2d, BitmapFont _font_white) {
		super(ecs);

		batch2d = _batch2d;
		font_white = _font_white;
	}


	@Override
	public Class<?> getComponentClass() {
		return DrawTextData.class;
	}


	@Override
	public void processEntity(AbstractEntity entity) {
		DrawTextData dtd = (DrawTextData)entity.getComponent(DrawTextData.class);

		dtd.drawUntil -= Gdx.graphics.getDeltaTime();
		if (dtd.drawUntil <= 0) {
			entity.remove();
		} else {
			font_white.draw(batch2d, dtd.text, dtd.x, dtd.y);
		}

	}

}
