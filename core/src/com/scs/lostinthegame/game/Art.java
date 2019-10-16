package com.scs.lostinthegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class Art {

	public TextureRegion[][] entities;
	public TextureRegion[][] items;

	public Art() {
		entities = createSheet("entities.png",16,16);
		items = createSheet("items.png",16,16);
	}
	

	public static TextureRegion[][] createSheet(String src, int ww, int hh){
		Texture tex = new Texture(Gdx.files.internal(src));
		int w = tex.getWidth()/ww;
		int h = tex.getHeight()/hh;
		TextureRegion reg[][]  = new TextureRegion[w][h];

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				reg[x][y] = new TextureRegion(tex, x*16, y*16, 16, 16);
			}
		}

		return reg;

	}
	
	
	public static Decal DecalHelper(String filename, float sizePcent) {
		Texture tex = new Texture(Gdx.files.internal(filename));
		TextureRegion tr = new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight());
		Decal decal = Decal.newDecal(tr, true);
        decal.setScale(Game.UNIT * sizePcent / tr.getRegionWidth());
        return decal;
	}

}
