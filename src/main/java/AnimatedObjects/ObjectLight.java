package AnimatedObjects;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constant.LIGHT.*;

public class ObjectLight extends AnimatedObject{
    private final float rate;
    public ObjectLight(int xPos, int yPos, int objectWidth, int objectHeight, float rate) {
        this.xPos = xPos + DEFAULT_WIDTH / 4 - objectWidth - 10;
        this.yPos = yPos + DEFAULT_HEIGHT / 4 - objectHeight + 10;
        this.rate = rate;
        initClass();
    }

    private void initClass() {
        aniSpeed = 60;
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
        int xDrawOffset = 100;
        int yDrawOffset = 100;
        g.drawImage(animation[drawIndex], xPos - xLvlOffset - xDrawOffset - xObjectOffset, yPos - yLvlOffset - yDrawOffset - yObjectOffset, (int) (DEFAULT_WIDTH / 5 * rate), (int) (DEFAULT_HEIGHT / 5 * rate), null);
    }
}
