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
	private BitmapFont font;

	public DrawTextSystem(BasicECS ecs, SpriteBatch _batch2d, BitmapFont _font) {
		super(ecs);

		batch2d = _batch2d;
		font = _font;
	}


	@Override
	public Class<?> getComponentClass() {
		return DrawTextData.class;
	}


	public void processEntity(AbstractEntity entity) {
		DrawTextData dtd = (DrawTextData)entity.getComponent(DrawTextData.class);

		dtd.drawUntil -= Gdx.graphics.getDeltaTime();
		if (dtd.drawUntil <= 0) {
			entity.remove();
		} else {
			font.draw(batch2d, dtd.text, dtd.x, dtd.y);
		}

	}

}
