package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static utilz.Constant.JUMP_BASE.*;


public class JumpBase {
    private final int xPos;
    private final int yPos;
    private BufferedImage[][] animation;
    private int drawIndex = 0;
    private int aniTick = 0;
    private final int aniSpeed = 20;
    private int type = IDLE;
    private final int yDrawOffset = (int) (20 * MODE);
    private final Rectangle2D.Float border;
    public JumpBase(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        border = new Rectangle2D.Float(xPos, yPos, WIDTH, HEIGHT);
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
