package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;

public interface ISpriteBatch {

	void draw(Texture texture, float x, float y, float originX, float originY, float width, float height,
			float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY);
}
