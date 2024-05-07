package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class JumpBase extends AnimatedObject{

    private BufferedImage[][] animation;
    private int type = Constant.JUMP_BASE.IDLE;
    private final Rectangle2D.Float border;
    public JumpBase(int xPos, int yPos){
        super(xPos, yPos);
        border = new Rectangle2D.Float(xPos, yPos, Constant.JUMP_BASE.WIDTH, Constant.JUMP_BASE.HEIGHT);
        aniSpeed = 20;
        initClass();
    }

    private void initClass() {
        BufferedImage temp = LoadSave.getImg(LoadSave.JUMP_BASE);
        animation = new BufferedImage[2][9];
        for (int i=0; i<animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = temp.getSubimage(j * Constant.JUMP_BASE.DEFAULT_WIDTH, i * Constant.JUMP_BASE.DEFAULT_HEIGHT, Constant.JUMP_BASE.DEFAULT_WIDTH, Constant.JUMP_BASE.DEFAULT_HEIGHT);
            }
        }
    }
    public void update(Game game){
        updateAniTick();
        updateStatus(game);
    }

    private void updateStatus(Game game) {
        type = Constant.JUMP_BASE.IDLE;
        if (game.getPlayer().getHitbox().intersects(border)) type = Constant.JUMP_BASE.WARP;
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.JUMP_BASE.getType(type)){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[type][drawIndex], xPos - xLvlOffset, yPos - yLvlOffset, Constant.JUMP_BASE.WIDTH, Constant.JUMP_BASE.HEIGHT, null);
    }
}
