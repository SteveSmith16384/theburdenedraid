package com.scs.lostinthegame;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.scs.lostinthegame.game.Game;

public class Audio {

	private String preload[] = new String[]{
			"loot","weapon","door","pickup","hurt","death","ladder","player_hurt","read","wall_open","success","step", "select"
	};

	private Music music;
	private HashMap<String, Sound> sounds; 
	private float musicVolume;


	public Audio() {
		sounds = new HashMap<String, Sound>();

		for(String s : preload){
			Sound sfx = Gdx.audio.newSound(Gdx.files.internal("audio/"+s+".wav"));
			sounds.put(s, sfx);
		}

		music = Gdx.audio.newMusic(Gdx.files.internal("audio/music.mp3"));
		music.setLooping(true);
	}


	public void update() {
		if(!Game.gameComplete) {
			musicVolume = Math.min(musicVolume + Gdx.graphics.getDeltaTime() / 2f, 1.0f);
			music.setVolume(musicVolume);
		}else{
			musicVolume = Math.max(musicVolume-Gdx.graphics.getDeltaTime()/4f, 0f);
			music.setVolume(musicVolume);
		}
	}

	public void startMusic(){
		if(!music.isPlaying()) {
			music.play();
			music.setVolume(0f);
			musicVolume = 0f;
		}
	}

	public void stopMusic() {
		music.stop();
	}

	public void play(String name) {
		if (sounds.containsKey(name)) {
			sounds.get(name).play();
		} else {

			Sound sfx = Gdx.audio.newSound(Gdx.files.internal("audio/"+name+".wav"));
			sounds.put(name, sfx);
			System.out.println("Sound " + name + " not preloaded");
			play(name); // Loop round to play the newly-added file.
		}

	}

}
