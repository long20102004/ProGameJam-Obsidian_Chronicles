package Level;

import AnimatedObjects.*;
import Enemies.*;
import Items.Items;
import NPCs.NPC;
import NPCs.OldMan;
import UI.NPC_Buttons;
import Upgrade.Potions;
import Upgrade.Product;
import Upgrade.Skills;
import Upgrade.Weapons;
import utilz.ExtraMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static Main.Game.*;
import static utilz.Constant.ITEMS.BANDAGE;
import static utilz.Constant.ITEMS.COINS1;

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

    private Point playerSpawn;

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
        numberRow = (int) (tileSet.getHeight() / (TILE_DEFAULT_SIZE * tileZoom));
        numberCol = (int) (tileSet.getWidth() / (TILE_DEFAULT_SIZE * tileZoom));
        lvlData = new int[tileMap.getHeight()][tileMap.getWidth()];
        tileType = new BufferedImage[numberRow * numberCol];
        this.indexLevel = indexLevel;
        initClass();
    }

    public void initClass() {
        loadLevel();
        for (int i = 0; i < numberRow; i++) {
            for (int j = 0; j < numberCol; j++) {
                int number = i * numberCol + j;
                tileType[number] = tileSet.getSubimage((int) (j * TILE_DEFAULT_SIZE * tileZoom), (int) (i * TILE_DEFAULT_SIZE * tileZoom), (int) (TILE_DEFAULT_SIZE * tileZoom), (int) (TILE_DEFAULT_SIZE * tileZoom));
            }
        }
    }

    public void draw(Graphics g, float xLevelOffset, float yLevelOffset) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        for (int i = 0; i < tileMap.getHeight(); i++) {
            for (int j = 0; j < tileMap.getWidth(); j++) {
                if (lvlData[i][j] > 0 && lvlData[i][j] < tileType.length)
                    g.drawImage(tileType[lvlData[i][j] - 1], (int) (j * TILE_SIZE - xLevelOffset), (int) (i * TILE_SIZE - yLevelOffset), TILE_SIZE, TILE_SIZE, null);
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
            case 255 -> playerSpawn = new Point(y * TILE_SIZE, x * TILE_SIZE);
            case 250 -> playerTeleport = new Point(y * TILE_SIZE, x * TILE_SIZE);
        }
    }
    private void loadEnemies(int blueValue, int i, int j){
        switch (blueValue){
            case 1 -> enemies.add(new Ghoul(j * TILE_SIZE, i * TILE_SIZE));
            case 2 -> enemies.add(new Summoner(j * TILE_SIZE, i * TILE_SIZE));
            case 3 -> enemies.add(new Spitter(j * TILE_SIZE, i * TILE_SIZE));
            case 4 -> enemies.add(new Shielder(j * TILE_SIZE, i * TILE_SIZE));
            case 5 -> enemies.add(new Dagger(j * TILE_SIZE, i * TILE_SIZE));
            case 6 -> enemies.add(new Hoarder(j * TILE_SIZE, i * TILE_SIZE));
            case 7 -> enemies.add(new Guardian(j * TILE_SIZE, i * TILE_SIZE));
            case 8 -> enemies.add(new Hive(j * TILE_SIZE, i * TILE_SIZE));
        }
    }

    private void loadObjects(int greenValue, int i, int j) {
        switch (greenValue){
            case 1 -> objects.add(new LightBugs(j * TILE_SIZE, i * TILE_SIZE, false));
            case 2 -> objects.add(new Flower(j * TILE_SIZE, i * TILE_SIZE));
            case 3 -> objects.add(new JumpBase(j * TILE_SIZE,i * TILE_SIZE));
            case 4 -> {
                int kind = ExtraMethods.random.nextInt(0,2);
                if (kind == 1) objects.add(new Bugs(j * TILE_SIZE, i * TILE_SIZE));
                else objects.add(new GreenBugs(j * TILE_SIZE, i * TILE_SIZE));
            }
            case 5,6,7 -> objects.add(new Trees(j * TILE_SIZE, i * TILE_SIZE, random.nextInt(1,3)));
            case 8 -> objects.add(new LightBugs(j * TILE_SIZE, i * TILE_SIZE, true));
            case 9 -> itemsList.add(new Items((j - 1) * TILE_SIZE, i * TILE_SIZE, COINS1));
            case 10 -> itemsList.add(new Items((j - 1) * TILE_SIZE, i * TILE_SIZE, BANDAGE));
            case 11 -> {
                objects.add(new Merchant(j * TILE_SIZE, i * TILE_SIZE));
                npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.E, j * TILE_SIZE, (int) ((i - 0.5) * TILE_SIZE)));
            }
            case 12 -> objects.add(new Door(j * TILE_SIZE, i * TILE_SIZE, false));
            case 13 -> objects.add(new Door(j * TILE_SIZE, i * TILE_SIZE, true));
            case 14 -> objects.add(new Portal(j * TILE_SIZE, i * TILE_SIZE));
            case 15 -> {
                npcList.add(new OldMan(j * TILE_SIZE, i * TILE_SIZE));
                npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.E, j * TILE_SIZE, (int) ((i - 0.5) * TILE_SIZE)));
            }
            case 230 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.A, j * TILE_SIZE, i * TILE_SIZE));
            case 231 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.D, j * TILE_SIZE, i * TILE_SIZE));
            case 232 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.W, j * TILE_SIZE, i * TILE_SIZE));
            case 233 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.F, j * TILE_SIZE, i * TILE_SIZE));
            case 234 -> npc_buttonsList.add(new NPC_Buttons(NPC_Buttons.J, j * TILE_SIZE, i * TILE_SIZE));
        }
    }
    public int getTileType(int x, int y) {
        return lvlData[x][y];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public void setLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    public void setTileData(int xTile, int yTile, int val) {
        this.lvlData[xTile][yTile] = val;
    }

    public BufferedImage getTileMap() {
        return tileMap;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public Point getPlayerTeleport() {
        return playerTeleport;
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
}
