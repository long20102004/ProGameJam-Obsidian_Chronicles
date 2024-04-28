package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static utilz.Constant.JUMP_BASE.*;


public class JumpBase extends AnimatedObject{

    private BufferedImage[][] animation;
    private int type = IDLE;
    private final Rectangle2D.Float border;
    public JumpBase(int xPos, int yPos){
        super(xPos, yPos);
        border = new Rectangle2D.Float(xPos, yPos, WIDTH, HEIGHT);
        aniSpeed = 20;
        initClass();
    }

    private void initClass() {
        BufferedImage temp = LoadSave.getImg(LoadSave.JUMP_BASE);
        animation = new BufferedImage[2][9];
        for (int i=0; i<animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = temp.getSubimage(j * DEFAULT_WIDTH, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }
        }
    }
    public void update(Game game){
        updateAniTick();
        updateStatus(game);
    }

    private void updateStatus(Game game) {
        type = IDLE;
        if (game.getPlayer().getHitbox().intersects(border)) type = WARP;
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= getType(type)){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[type][drawIndex], xPos - xLvlOffset, yPos - yLvlOffset, WIDTH, HEIGHT, null);
    }
}
