package AnimatedObjects;

import Level.Level;
import Main.Game;

import java.awt.*;

public class ObjectManager {
    private final Game game;
    private Level currentLevel;
    public ObjectManager(Game game){
        this.game = game;
        currentLevel = game.getLevelManager().getLevel();
    }

    public void loadObject(Level level){
        this.currentLevel = level;
    }
    public void update(){
        for(LightBugs lightBugs : currentLevel.lightBugsList){
            lightBugs.update();
        }
        for (Flower flower : currentLevel.flowerList) {
            flower.update();
        }
        for (JumpBase jumpBase : currentLevel.jumpBaseList){
            jumpBase.update(game);
        }
        for (Bugs bugs : currentLevel.bugsList){
            bugs.update();
        }
        for (Trees trees : currentLevel.treesList){
            trees.update();
        }
        for (Merchant merchant : currentLevel.merchantsList){
            merchant.update(game);
        }
        for (Door door : currentLevel.doorsList){
            door.update(game);
        }
        for (Portal portal : currentLevel.portalsList){
            portal.update(game);
        }
        for (GreenBugs greenBugs : currentLevel.greenBugsList){
            greenBugs.update();
        }
    }
    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){
        for(LightBugs lightBugs : currentLevel.lightBugsList){
            lightBugs.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Flower flower : currentLevel.flowerList) {
            flower.draw(g, xLevelOffset, yLevelOffset);
        }
        for (JumpBase jumpBase : currentLevel.jumpBaseList){
            jumpBase.draw(g,xLevelOffset,yLevelOffset);
        }
        for (Bugs bugs: currentLevel.bugsList){
            bugs.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Trees trees : currentLevel.treesList){
            trees.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Merchant merchant : currentLevel.merchantsList){
            merchant.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Door door : currentLevel.doorsList){
            door.draw(g, xLevelOffset, yLevelOffset);
        }
        for (Portal portal : currentLevel.portalsList){
            portal.draw(g, xLevelOffset, yLevelOffset);
        }
        for (GreenBugs greenBugs : currentLevel.greenBugsList){
            greenBugs.draw(g,xLevelOffset,yLevelOffset);
        }
    }



}
