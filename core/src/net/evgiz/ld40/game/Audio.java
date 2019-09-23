package net.evgiz.ld40.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Audio {

    /*private class NamedSound {
    	
        public String name;
        public Sound sound;
        public NamedSound(String name, Sound sound){
            this.name = name;
            this.sound = sound;
        }
    }*/

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
            sounds.put(s, sfx);//new NamedSound(s, sfx));
        }

        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music.mp3"));
        music.setLooping(true);
    }
    

    public void update(){
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
        /*NamedSound sound = null;

        for(NamedSound s : sounds){
            if(s.name.equals(name)){
                sound = s;
                break;
            }
        }

        if(sound != null){
            sound.sound.play();
        }else{*/
            Sound sfx = Gdx.audio.newSound(Gdx.files.internal("audio/"+name+".wav"));
            sounds.put(name, sfx);
            //sfx.play();
            System.out.println("Sound " + name + " not preloaded");
            
            play(name); // Loop round to play the newly-added file.
        }

    }

    
    /*
    public void playPitched(String name, float pitch) {
        NamedSound sound = null;

        for(NamedSound s : sounds){
            if(s.name.equals(name)){
                sound = s;
                break;
            }
        }

        sound.sound.play(1f, pitch, 0);

    }
*/
}
