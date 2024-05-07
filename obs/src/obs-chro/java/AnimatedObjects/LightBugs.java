package AnimatedObjects;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static utilz.Constant.LIGHT_BUG.*;

public class LightBugs {
    private final int xPos;
    private final int yPos;
    private BufferedImage[] animation;
    private int drawIndex = 0;
    private int aniTick = 0;
    private final int aniSpeed = 40;
    private final boolean isReversed;
    private final int xDrawOffset = (int) (15 * MODE);
    private ObjectLight objectLight;

    public LightBugs(int xPos, int yPos, boolean isReversed) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.isReversed = isReversed;
        initClass();
    }

    private void initClass() {
        objectLight = new ObjectLight(xPos, yPos, WIDTH, HEIGHT, 3);
        BufferedImage temp = LoadSave.getImg(LoadSave.LIGHT_BUG);
        animation = new BufferedImage[10];
        for (int i = 0; i < animation.length; i++) {
            animation[i] = temp.getSubimage(i * DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
            if (drawIndex >= TURN) {
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        if (isReversed) {
            objectLight.draw(g, xLvlOffset, yLvlOffset, (int) (15 * MODE), 0);
            g.drawImage(animation[drawIndex], xPos - xLvlOffset + xDrawOffset, yPos - yLvlOffset, -WIDTH, HEIGHT, null);
        }
        else {
            objectLight.draw(g, xLvlOffset, yLvlOffset, 0, 0);
            g.drawImage(animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset, WIDTH, HEIGHT, null);
        }
    }
}
