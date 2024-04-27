package Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer{
    public static int MENU = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;
    public static int LEVEL_3 = 3;
    public static int COMBAT = 4;
    public static int FINAL_BATTLE = 5;
    public static int TELEPORT = 0;
    public static int ATTACK_1 = 1;
    public static int ATTACK_2 = 2;
    public static int ATTACK_3 = 3;
    public static int SLIDE = 4;
    public static int JUMP = 5;
    public static int CLIMB = 6;
    public static int DASH = 7;
    public static int WALL_SLIDE = 8;
    public static int LAND = 9;
    public static int FALLING = 10;
    public static int RUNNING = 11;
    public static int CLICKED = 12;
    public static int TRANSFORM = 13;
    public static int SHOT = 14;
    private boolean isMuted;
    private final float musicVolume = 0.6f;
    private final float effectVolume = 0.7f;
    private Clip[] songs,effects;
    public static Random rand = new Random();
    private int currentSong;
    public AudioPlayer(){
        setEffect();
        setSong();
        playSong(songs[currentSong]);
    }
    private Clip getClip(String name){
        URL url = getClass().getResource("/Audio/" + name + ".wav");
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audioInputStream);
            return c;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setSong() {
        String[] name = new String[]{"menu", "Level1", "Level2", "Level3", "FinalBattle", "Combat"};
        songs = new Clip[name.length];
        for (int i=0; i<songs.length; i++){
            songs[i] = getClip(name[i]);
            setMusicVolume(songs[i]);
        }
    }

    private void setEffect() {
        String[] name = new String[]{"Teleport", "attack1", "attack2", "attack3", "Slide", "Jump", "Climb", "Dash", "WallSlide", "Land", "Falling", "Running", "ClickSound", "transform", "shot"};
        effects = new Clip[name.length];
        for (int i=0; i<effects.length; i++){
            effects[i] = getClip(name[i]);
            setEffectVolume(effects[i]);
        }
    }
    private void playSong(Clip song) {
        song.start();
        song.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void setMusicVolume(Clip clip){
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float changeVolume = (range * musicVolume) + gainControl.getMinimum();
        gainControl.setValue(changeVolume);
    }
    public void setEffectVolume(Clip clip){
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float changeVolume = (range * effectVolume) + gainControl.getMinimum();
        gainControl.setValue(changeVolume);
    }
    public void playEffectSound(int type){
        if (effects[type].getMicrosecondPosition() > 200000)
            effects[type].setMicrosecondPosition(0);
        effects[type].start();
    }
    public void setMusic(int song){
        if (song != currentSong) songs[currentSong].stop();
        currentSong = song;
    }
    public void update(){
        playSong(songs[currentSong]);
    }
}