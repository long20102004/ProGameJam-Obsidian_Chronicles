package NPCs;

import Level.Level;
import Main.Game;

import java.awt.*;

public class NPC_Manager {
    private Game game;
    private Level level;
    public NPC_Manager(Game game){
        this.game = game;
        initClass();
    }

    private void initClass() {
        level = game.getLevelManager().getLevel();
    }

    public void update(Game game){
        for (OldMan oldMan : level.oldManList){
            if (oldMan.isActive) oldMan.update(game);
        }
    }
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset){
        for (OldMan oldMan : level.oldManList){
            if (oldMan.isActive) oldMan.draw(g, xLevelOffset, yLevelOffset);
        }
    }
}
