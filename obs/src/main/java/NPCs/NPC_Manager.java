package NPCs;

import Level.Level;
import Main.Game;
import org.springframework.stereotype.Component;

import java.awt.*;
@Component
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
    public void loadNpc(Level level){
        this.level = level;
    }

    public void update(Game game){
        for (NPC npc : level.npcList){
            if (npc.isActive) npc.update(game);
        }
    }
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset){
        for (NPC npc : level.npcList){
            if (npc.isActive) npc.draw(g, xLevelOffset, yLevelOffset);
        }
    }
}
