package Main;

import AnimatedObjects.ObjectManager;
import Audio.AudioPlayer;
import Enemies.EnemyManager;
import Level.Level;
import NPCs.NPC_Manager;
import Player.Player;
import Items.ItemsManager;
import Level.LevelManager;
import Player.SwordHero;
import State.*;
import State.Menu;
import State.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import utilz.ExtraMethods;
import Player.*;

import java.awt.*;
@Getter
@Setter
public class Game implements Runnable{
    public static int reward;
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private final LevelManager levelManager = new LevelManager(this);
    private final EnemyManager enemyManager = new EnemyManager(this);

    private final ObjectManager objectManager = new ObjectManager(this);

    private final ItemsManager itemsManager = new ItemsManager(this);

    private final AudioPlayer audioPlayer = new AudioPlayer();
    private final Menu menu = new Menu(this);
    private final Pause pause = new Pause(this);
    private final Playing playing = new Playing(this);
    private final Setting setting = new Setting(this);
    private final NPC_Manager npcManager = new NPC_Manager(this);
    private Player player;
    private final int FPS = 120, UPS = 200;
    public static final int TILE_DEFAULT_SIZE = 16;
    public static final float MODE = (float) (2.3f * (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1536));
    public static final int TILE_SIZE = (int) (TILE_DEFAULT_SIZE * MODE);
    public static final int NUMBER_TILES_IN_HEIGHT = 30;
    public static final int NUMBER_TILES_IN_WIDTH = 50;
    public static final int GAME_HEIGHT = NUMBER_TILES_IN_HEIGHT * TILE_SIZE;
    public static final int GAME_WIDTH = NUMBER_TILES_IN_WIDTH * TILE_SIZE;
    public static int state = 0;
    public Game() {
        initClass();
    }
    private void initClass() {
        Point spawnPoint = levelManager.getLevel().getPlayerSpawn();
        player = new SwordHero(spawnPoint.x, spawnPoint.y, this);
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        new ExtraMethods(this, levelManager.getLevel());
        Thread gameThread = new Thread(this);
        gameThread.start();
    }
    public void update(){
        switch (GameState.gameState){
            case PLAYING -> playing.update();
            case MENU -> menu.update();
            case PAUSE -> pause.update();
            case SETTING -> setting.update();
        }
    }
    public void draw(Graphics g){
        switch (GameState.gameState){
            case PLAYING -> playing.draw(g);
            case MENU -> menu.draw(g);
            case PAUSE -> pause.draw(g);
            case SETTING -> setting.draw(g);
        }
    }
    @Override
    public void run() {
        int fps = 0, ups = 0;
        double framesPerSecond = 1000000000.0 / FPS;
        double updatesPerSecond = 1000000000.0 / UPS;
        double timeUpdateCheck = 0.0, timeFrameCheck = 0.0;
        long lastTime = System.currentTimeMillis();
        long lastCheck = System.nanoTime();
        while (true) {
            long now = System.currentTimeMillis();
            long currentCheck = System.nanoTime();
            timeFrameCheck += (currentCheck - lastCheck) / framesPerSecond;
            timeUpdateCheck += (currentCheck - lastCheck) / updatesPerSecond;
            lastCheck = currentCheck;
            if (timeFrameCheck >= 1){
                fps++;
                gamePanel.repaint();
                timeFrameCheck--;
            }
            if (timeUpdateCheck >= 1){
                ups++;
                update();
                timeUpdateCheck--;
            }
            if (now - lastTime >= 1000) {
                lastTime = now;
//                System.out.println("FPS: " + fps);
//                System.out.println("UPS: " + ups);
                fps = 0;
                ups = 0;
                lastTime = now;
            }
        }
    }
    public void resetAll(){
        Playing.countReceivedAction = 0;
        Playing.index = 0;
        state = 0;
        reward = 0;
        Player.coins = 0;
        enemyManager.resetAll();
        itemsManager.resetAll();
        levelManager.resetAll();
        Player.coins = 0;
    }
    public void setPreviousPlayer(Player player, Point point){
        this.player = player;
        this.player.getHitbox().x = point.x;
        this.player.getHitbox().y = point.y;
    }

}

