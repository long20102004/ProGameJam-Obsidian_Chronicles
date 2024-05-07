package NPCs;

import Main.Game;
import Dialogue.Dialogue;
import org.springframework.stereotype.Component;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

@Component
public class NPC {
    protected int xPos, yPos;
    protected int drawIndex = 0, aniTick = 0, aniSpeed = 40;
    protected BufferedImage[] animation;
    protected Rectangle2D.Float border;
    protected Dialogue dialogue;
    protected String[] conversations = new String[5];
    protected boolean isActive = true;
    public NPC(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        border = new Rectangle2D.Float(xPos, yPos, Constant.NPC.WIDTH, Constant.NPC.HEIGHT);
    }
    private void updateAniTick(){
        aniTick++;
        if (aniTick >= aniSpeed){
            drawIndex++;
            if (drawIndex >= animation.length - 1){
                drawIndex = 0;
            }
        }
    }
    public void update(Game game){
        updateAniTick();
        dialogue.update(game, border, conversations.length);
        if (dialogue.getCurrentDialogue() == conversations.length) isActive = false;
    }
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset){
        g.setColor(Color.RED);
        g.drawImage(animation[drawIndex], (int) (xPos - xLevelOffset), (int) (yPos - yLevelOffset), Constant.NPC.WIDTH, Constant.NPC.HEIGHT, null);
//        g.drawRect((int) ((int) border.x - xLevelOffset), (int) ((int) border.y - yLevelOffset), (int) border.width, (int) border.height);
        dialogue.draw(g);
    }
}
