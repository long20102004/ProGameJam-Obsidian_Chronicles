package AnimatedObjects;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constant.LIGHT.*;

public class ObjectLight {
    private BufferedImage[] animation;
    private final int xPos;
    private final int yPos;
    private int drawIndex;
    private int aniTick = 0;
    private final int aniSpeed = 60;
    private final int xDrawOffset = 100;
    private final int yDrawOffset = 100;
    private int xObjectOffset;
    private final float rate;
    public ObjectLight(int xPos, int yPos, int objectWidth, int objectHeight, float rate) {
        this.xPos = xPos + DEFAULT_WIDTH / 4 - objectWidth - 10;
        this.yPos = yPos + DEFAULT_HEIGHT / 4 - objectHeight + 10;
        this.rate = rate;
        initClass();
    }

    private void initClass() {
        animation = new BufferedImage[10];
        BufferedImage tmp = LoadSave.getImg(LoadSave.WHITE_LIGHT);
        for (int i = 0; i < 10; i++) {
            animation[i] = tmp.getSubimage(i * DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    public void update() {
        updateAniTick();
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= LIGHT_LOOP){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset, int xObjectOffset, int yObjectOffset) {
        g.drawImage(animation[drawIndex], xPos - xLvlOffset - xDrawOffset - xObjectOffset, yPos - yLvlOffset - yDrawOffset - yObjectOffset, (int) (DEFAULT_WIDTH / 5 * rate), (int) (DEFAULT_HEIGHT / 5 * rate), null);
    }
}
