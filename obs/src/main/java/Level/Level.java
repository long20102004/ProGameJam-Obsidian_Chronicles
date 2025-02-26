package Level;

import AnimatedObjects.*;
import Enemies.*;
import Main.Game;
import AnimatedObjects.*;
import Items.Items;
import NPCs.NPC;
import NPCs.OldMan;
import UI.NPC_Buttons;
import Upgrade.Product;
import Enemies.*;
import lombok.Getter;
import lombok.Setter;
import utilz.ExtraMethods;
import org.springframework.stereotype.Component;
import utilz.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;
@Getter
@Setter
public class Level {
    private int[][] lvlData;
    private final BufferedImage tileSet;
    private final BufferedImage tileMap;
    private final BufferedImage background;
    private final BufferedImage[] tileType;
    private final int numberRow;
    private final int numberCol;
    private float tileZoom = 1;
    private final Random random = new Random();
    private int deltaValue = 0;

    private Point playerSpawn;
    private Point line2Spawn;
    private Point playerEndPoint;
    private Point playerEndPoint2;
    private Point trapStartPoint;
    private Point trapEndPoint;
    private Point teleportTrapPoint;

    private Point playerTeleport;
    private final int indexLevel;

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Items> itemsList = new ArrayList<>();
    public ArrayList<AnimatedObject> objects = new ArrayList<>();
    public ArrayList<NPC> npcList = new ArrayList<>();
    public ArrayList<NPC_Buttons> npc_buttonsList = new ArrayList<>();
    public ArrayList<Product> products = new ArrayList<>();

    public Level(BufferedImage tileMap, BufferedImage tileSet, BufferedImage background, float tileZoom, int indexLevel) {
        this.tileZoom = tileZoom;
        this.background = background;
        this.tileMap = tileMap;
        this.tileSet = tileSet;
        numberRow = (int) (tileSet.getHeight() / (Game.TILE_DEFAULT_SIZE * tileZoom));
        numberCol = (int) (tileSet.getWidth() / (Game.TILE_DEFAULT_SIZE * tileZoom));
        lvlData = new int[tileMap.getHeight()][tileMap.getWidth()];
        tileType = new BufferedImage[numberRow * numberCol];
        this.indexLevel = indexLevel;
        if (indexLevel == 0) deltaValue = 1;
        initClass();
    }

    public void initClass() {
        loadLevel();
        for (int i = 0; i < numberRow; i++) {
            for (int j = 0; j < numberCol; j++) {
                int number = i * numberCol + j;
                tileType[number] = tileSet.getSubimage((int) (j * Game.TILE_DEFAULT_SIZE * tileZoom), (int) (i * Game.TILE_DEFAULT_SIZE * tileZoom), (int) (Game.TILE_DEFAULT_SIZE * tileZoom), (int) (Game.TILE_DEFAULT_SIZE * tileZoom));
            }
        }
    }

    public void draw(Graphics g, float xLevelOffset, float yLevelOffset) {
        g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        for (int i = 0; i < tileMap.getHeight(); i++) {
            for (int j = 0; j < tileMap.getWidth(); j++) {
                if (lvlData[i][j] > 0 && lvlData[i][j] < tileType.length)
                    g.drawImage(tileType[lvlData[i][j] - deltaValue], (int) (j * Game.TILE_SIZE - xLevelOffset), (int) (i * Game.TILE_SIZE - yLevelOffset), Game.TILE_SIZE, Game.TILE_SIZE, null);
            }
        }
        for (NPC_Buttons buttons : npc_buttonsList){
            buttons.draw(g, xLevelOffset, yLevelOffset);
        }
    }
    private void loadLevel(){
        BufferedImage tmp = tileMap;
        int[][] res = new int[tileMap.getHeight()][tileMap.getWidth()];
        for (int i=0; i<res.length; i++){
            for (int j=0; j<res[0].length; j++){
                Color color = new Color(tmp.getRGB(j, i));
                loadLevelData(color.getRed(), i, j);
                loadEnemies(color.getBlue(), i, j);
                loadObjects(color.getGreen(), i, j);
            }
        }
    }

    private void loadLevelData(int redValue, int x, int y){
        lvlData[x][y] = redValue;
        switch (redValue){
            case 255 -> playerSpawn = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
            case 250 -> playerTeleport = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
            case 254 -> playerEndPoint = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
            case 253 -> trapStartPoint = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
            case 252 -> trapEndPoint = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
            case 251 -> teleportTrapPoint = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
            case 249 -> playerEndPoint2 = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
            case 248 -> line2Spawn = new Point(y * Game.TILE_SIZE, x * Game.TILE_SIZE);
        }
    }
    private void loadEnemies(int blueValue, int i, int j){
        if (blueValue <= 0 || blueValue > EnemyType.values().length){
            return;
        }
        try {
            String className = "Enemies." + EnemyType.getEnemyType(blueValue);
            if (className.equals("Enemies.NONE")) return;
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(int.class, int.class);
            Enemy enemy = (Enemy) constructor.newInstance(j * Game.TILE_SIZE, i * Game.TILE_SIZE);
            enemies.add(enemy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadObjects(int greenValue, int i, int j) {
        switch (greenValue){
            case 1 -> objects.add(new LightBugs(j * Game.TILE_SIZE, i * Game.TILE_SIZE, false));
            case 2 -> objects.add(new Flower(j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 3 -> objects.add(new JumpBase(j * Game.TILE_SIZE,i * Game.TILE_SIZE));
            case 4 -> {
                int kind = ExtraMethods.random.nextInt(0,2);
                if (kind == 1) objects.add(new Bugs(j * Game.TILE_SIZE, i * Game.TILE_SIZE));
                else objects.add(new GreenBugs(j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            }
            case 5,6,7 -> objects.add(new Trees(j * Game.TILE_SIZE, i * Game.TILE_SIZE, random.nextInt(1,3)));
            case 8 -> objects.add(new LightBugs(j * Game.TILE_SIZE, i * Game.TILE_SIZE, true));
            case 9 -> itemsList.add(new Items((j - 1) * Game.TILE_SIZE, i * Game.TILE_SIZE, Constant.ITEMS.COINS1));
            case 10 -> itemsList.add(new Items((j - 1) * Game.TILE_SIZE, i * Game.TILE_SIZE, Constant.ITEMS.BANDAGE));
            case 11 -> {
                objects.add(new Merchant(j * Game.TILE_SIZE, i * Game.TILE_SIZE));
                npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.E, j * Game.TILE_SIZE, (int) ((i - 0.5) * Game.TILE_SIZE)));
            }
            case 12 -> objects.add(new Door(j * Game.TILE_SIZE, i * Game.TILE_SIZE, false));
            case 13 -> objects.add(new Door(j * Game.TILE_SIZE, i * Game.TILE_SIZE, true));
            case 14 -> objects.add(new Portal(j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 15 -> {
                npcList.add(new OldMan(j * Game.TILE_SIZE, i * Game.TILE_SIZE));
                npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.E, j * Game.TILE_SIZE, (int) ((i - 0.5) * Game.TILE_SIZE)));
            }
            case 16 -> objects.add(new Trap(j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 230 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.A, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 231 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.D, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 232 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.W, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 233 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.F, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 234 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.J, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 235 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.K, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 236 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.G, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 237 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.H, j * Game.TILE_SIZE, i * Game.TILE_SIZE));
            case 238 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.U, j * Game.TILE_SIZE, i * Game.TILE_SIZE));

        }
    }
    public int getTileType(int x, int y) {
        return lvlData[x][y];
    }
    public ArrayList<? extends Enemy> getBoss(){
//        switch (indexLevel){
//            case 0 -> {
//                return hoardersList;
//            }
//            case 1 -> {
//                return guardiansList;
//            }
//        }
        return new ArrayList<>();
    }
    public ArrayList<? extends Enemy> getMustKillEnemies(){
//        switch (indexLevel){
//            case 0 -> {
//                return new ArrayList<>();
//            }
//            case 1 -> {
//                return daggerList;
//            }
//        }
        return new ArrayList<>();
    }

    public Point getPlayerEndPoint() {
        return playerEndPoint;
    }

    public Point getTrapStartPoint() {
        return trapStartPoint;
    }

    public void setTrapStartPoint(Point trapStartPoint) {
        this.trapStartPoint = trapStartPoint;
    }

    public Point getTrapEndPoint() {
        return trapEndPoint;
    }

    public void setTrapEndPoint(Point trapEndPoint) {
        this.trapEndPoint = trapEndPoint;
    }

    public Point getTeleportTrapPoint() {
        return teleportTrapPoint;
    }

    public Point getPlayerEndPoint2() {
        return playerEndPoint2;
    }

    public Point getLine2Spawn() {
        return line2Spawn;
    }

    public void setLine2Spawn(Point line2Spawn) {
        this.line2Spawn = line2Spawn;
    }
}
