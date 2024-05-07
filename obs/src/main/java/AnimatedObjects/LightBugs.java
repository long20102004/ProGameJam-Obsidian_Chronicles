package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LightBugs extends AnimatedObject{
    private final boolean isReversed;
    private final int xDrawOffset = (int) (15 * Game.MODE);
    private ObjectLight objectLight;

    public LightBugs(int xPos, int yPos, boolean isReversed) {
        super(xPos, yPos);
        this.isReversed = isReversed;
        initClass();
    }

    private void initClass() {
        objectLight = new ObjectLight(xPos, yPos, Constant.LIGHT_BUG.WIDTH, Constant.LIGHT_BUG.HEIGHT, 3);
        BufferedImage temp = LoadSave.getImg(LoadSave.LIGHT_BUG);
        animation = new BufferedImage[10];
        for (int i = 0; i < animation.length; i++) {
            animation[i] = temp.getSubimage(i * Constant.LIGHT_BUG.DEFAULT_WIDTH, 0, Constant.LIGHT_BUG.DEFAULT_WIDTH, Constant.LIGHT_BUG.DEFAULT_HEIGHT);
        }
    }

    public void update() {
        updateAniTick();
        objectLight.update();
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.LIGHT_BUG.TURN) {
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        if (isReversed) {
            objectLight.draw(g, xLvlOffset, yLvlOffset, (int) (15 * Game.MODE), 0);
            g.drawImage(animation[drawIndex], xPos - xLvlOffset + xDrawOffset, yPos - yLvlOffset, -Constant.LIGHT_BUG.WIDTH, Constant.LIGHT_BUG.HEIGHT, null);
        }
        else {
            objectLight.draw(g, xLvlOffset, yLvlOffset, 0, 0);
            g.drawImage(animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset, Constant.LIGHT_BUG.WIDTH, Constant.LIGHT_BUG.HEIGHT, null);
        }
    }
}
