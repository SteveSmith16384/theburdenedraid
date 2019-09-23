package net.evgiz.ld40;

public class Settings {
	
	// Hacks
	public static final boolean QUICKSTART = true;
	public static final boolean SHOOTING = false;
	public static final boolean TRY_NEW_TEX = true;
	public static final boolean HIDE_CEILING = false;
	
	public static final boolean SHOW_FPS = false;
	public static final String DEMON_LAIR = "Demon Lair";
	
	public static final int START_HEALTH = 5;
	public static final int ENEMY_HEALTH = 3;

	public static String levelOrder[] = new String[] {
			"Dungeons", "Crypt", "Ice Caves", DEMON_LAIR
	};


	private Settings() {

	}

	
	public static final void p(String s) {
		System.out.println(s);
	}
}
