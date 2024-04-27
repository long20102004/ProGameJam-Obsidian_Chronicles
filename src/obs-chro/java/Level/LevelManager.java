package Level;

import Audio.AudioPlayer;
import Main.Game;
import Player.SwordHero;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private final ArrayList<Level> levels = new ArrayList<>();
    private BufferedImage[] lvlSprite;
    private BufferedImage[] lvlTileMap;
    private BufferedImage[] background;
    private final Game game;
    private float tileZoom = 1;

    public int getIndexLevel() {
        return indexLevel;
    }
    private int indexLevel = 0;
    private final int numberLevel = 2;
    public LevelManager(Game game){
        this.game = game;
        initClass();
    }
    private void initClass() {
        initLevel();
    }

    private void initLevel(){
        lvlTileMap = LoadSave.getAllLvlsTileMap();
        lvlSprite = LoadSave.getAllLvlsTileSet();
        background = LoadSave.getAllBackgrounds();
        for (int i=1; i<=numberLevel; i++){
            if (i == 2) tileZoom = 2f;
            if (i == 3) tileZoom = 2.1f;
            levels.add(new Level(lvlTileMap[i-1], lvlSprite[i-1], background[i-1], tileZoom, i-1));
        }
    }

    public void update(){
        switch (indexLevel){
            case 0 -> game.getAudioPlayer().setMusic(AudioPlayer.LEVEL_1);
            case 1 -> game.getAudioPlayer().setMusic(AudioPlayer.LEVEL_2);
        }
    }

    public void loadNextLevel() {
        if (game.getEnemyManager().checkWon(indexLevel)){
            indexLevel++;
            game.getObjectManager().loadObject(levels.get(indexLevel));
            game.getEnemyManager().loadEnemies(levels.get(indexLevel));
            game.getItemsManager().loadItems(levels.get(indexLevel));
            game.getPlaying().setMaxOffset();
            game.getPlayer().resetAll();
            game.getPlayer().getHitbox().x = levels.get(indexLevel).getPlayerSpawn().x;
            game.getPlayer().getHitbox().y = levels.get(indexLevel).getPlayerSpawn().y;
            new ExtraMethods(game, levels.get(indexLevel));
        }
    }

    public void draw(Graphics g, float xDrawOffset, float yDrawOffset){
        levels.get(indexLevel).draw(g, xDrawOffset, yDrawOffset);
    }
    public void resetAll(){
        indexLevel = 0;
        game.getObjectManager().loadObject(levels.get(indexLevel));
        game.getEnemyManager().loadEnemies(levels.get(indexLevel));
        game.getItemsManager().loadItems(levels.get(indexLevel));
        game.getPlaying().setMaxOffset();
        game.getPlayer().resetAll();
        Point point = levels.get(indexLevel).getPlayerSpawn();
        game.setPlayer(new SwordHero(point.x, point.y, game));
        game.getPlayer().getHitbox().x = levels.get(indexLevel).getPlayerSpawn().x;
        game.getPlayer().getHitbox().y = levels.get(indexLevel).getPlayerSpawn().y;
        new ExtraMethods(game, levels.get(indexLevel));
    }

    public Level getLevel(){
        return levels.get(indexLevel);
    }
}
