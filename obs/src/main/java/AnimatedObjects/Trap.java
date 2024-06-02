package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Trap extends AnimatedObject {

    private BufferedImage[] animation;
    int BLUE = 0, RED = 1;
    private int type = new Random().nextInt(BLUE, RED + 1);
    private final Rectangle2D.Float border;

    public Trap(int xPos, int yPos) {
        super(xPos, yPos);
        border = new Rectangle2D.Float(xPos, (float) yPos + 10, (float) (Constant.TRAP.WIDTH * 1.5), (float) (Constant.TRAP.HEIGHT / 1.5));
        aniSpeed = 20;
        initClass();
    }


    private void initClass() {
        BufferedImage temp;
        if (type == BLUE) temp = LoadSave.getImg(LoadSave.TRAP_BLUE);
        else temp = LoadSave.getImg(LoadSave.TRAP_RED);
        animation = new BufferedImage[17];
        for (int i = 0; i < animation.length; i++) {
            animation[i] = temp.getSubimage(i * Constant.TRAP.DEFAULT_WIDTH, 0, Constant.TRAP.DEFAULT_WIDTH, Constant.TRAP.DEFAULT_HEIGHT);
        }
    }

    public void update(Game game) {
        updateAniTick();
        updateStatus(game);
    }

    private void updateStatus(Game game) {
        if (game.getPlayer().getHitbox().intersects(border)) {
            game.getPlayer().updateHealthAndPower(-game.getPlayer().getCurrentHealth(), 0, 0);
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= 16) {
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawImage((Image) animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset, (int) (Constant.TRAP.WIDTH * 1.4), Constant.TRAP.HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) border.x - xLvlOffset, (int) border.y - yLvlOffset, (int) border.width, (int) border.height);
    }
}
