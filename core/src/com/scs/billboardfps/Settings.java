package com.scs.billboardfps;

import java.util.Random;

public class Settings {
	
	public static final boolean RELEASE_MODE = false;
	
	// Hacks
	public static final boolean TEST_FLOOR_SQ = true;
	public static final boolean USE_WAND = true;
	public static final boolean USE_WRAITHS = true;
	public static final String START_LEVEL = "";//"Demon Lair";
	public static final boolean ENEMY_SHOOTING = false;
	public static final boolean INFINITE_KEYS = true;
	public static final boolean PLAYER_SHOOTING = true;
	//public static final boolean TRY_NEW_TEX = false;
	//public static final boolean HIDE_CEILING = false;	
	public static final boolean SHOW_FPS = false;
	
	//public static final int START_HEALTH = 5;
	public static final int ENEMY_HEALTH = 3;
	
	public static final String TITLE ="The Burdened Raid";

	public static final int WINDOW_WIDTH_PIXELS = RELEASE_MODE ? 1024 : 512;
	public static final int WINDOW_HEIGHT_PIXELS = (int)(WINDOW_WIDTH_PIXELS * .68);

	public static final int LOGICAL_WIDTH_PIXELS = 640;
	public static final int LOGICAL_HEIGHT_PIXELS = (int)(LOGICAL_WIDTH_PIXELS * .68);
	
	public static Random random = new Random();

	private Settings() {

	}

	
	public static final void p(String s) {
		System.out.println(s);
	}
}
