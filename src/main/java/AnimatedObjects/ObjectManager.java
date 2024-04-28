package AnimatedObjects;

import Items.Items;
import Level.Level;
import Main.Game;
import Player.Player;
import utilz.Constant;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ObjectManager{
    private final Game game;
    private Level currentLevel;
    private Items coin;
    public ObjectManager(Game game){
        this.game = game;
        currentLevel = game.getLevelManager().getLevel();
        initCoin();
    }
    public void initCoin(){
        coin = new Items(1300, 50, Constant.ITEMS.COINS1);
    }
    public void loadObject(Level level){
        this.currentLevel = level;
    }
    public void update(){
        for (AnimatedObject object : currentLevel.objects){
            object.update();
        }
    }


    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){
        drawCoins(g);
        for (AnimatedObject object : currentLevel.objects){
            object.draw(g, xLevelOffset, yLevelOffset);
        }
    }

    private void drawCoins(Graphics g) {
        coin.draw(g, 0, 0);
        g.setColor(Color.WHITE);
        g.setFont(LoadSave.numberFont(20));
        g.drawString(String.valueOf(Player.coins), 1350, 75);
    }
}
