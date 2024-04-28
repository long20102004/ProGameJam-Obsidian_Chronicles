package AnimatedObjects;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constant.BUG.*;


public class Bugs extends AnimatedObject{
    public Bugs(int xPos, int yPos){
        super(xPos, yPos);
        initClass();
    }
    private void initClass() {
        BufferedImage temp = LoadSave.getImg(LoadSave.BUGS);
        animation = new BufferedImage[15];
        for (int i=0; i<animation.length; i++){
            animation[i] = temp.getSubimage(0, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }
    public void update(){
        updateAniTick();
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex + 1 >= LIGHT){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset - 50, WIDTH, HEIGHT, null);
    }
}
