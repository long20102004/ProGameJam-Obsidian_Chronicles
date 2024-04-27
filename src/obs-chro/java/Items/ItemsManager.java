package Items;

import Level.Level;
import Main.Game;

import java.awt.*;

public class ItemsManager {
    private final Game game;
    private Level currentLevel;

    public ItemsManager(Game game){
        this.game = game;
        currentLevel = game.getLevelManager().getLevel();
    }
    public void loadItems(Level level){
        currentLevel = level;
    }
    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        for (Items items : currentLevel.itemsList) {
            if (items.isActive()) items.draw(g, xLvlOffset, yLvlOffset);
        }
    }
    public void update(){
        for (Items items : currentLevel.itemsList) {
            if (items.isActive()) items.update(game);
        }
    }
    public void resetAll(){
        for (Items items : currentLevel.itemsList) {
             items.resetAll();
        }
    }
}
