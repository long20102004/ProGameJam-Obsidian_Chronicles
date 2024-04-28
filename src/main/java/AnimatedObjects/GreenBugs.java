package AnimatedObjects;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constant.GREEN_BUG.*;


public class GreenBugs extends AnimatedObject{
    public GreenBugs(int xPos, int yPos){
        super(xPos ,yPos);
        initClass();
    }

    private void initClass() {
        drawIndex = 5;
        BufferedImage temp = LoadSave.getImg(LoadSave.GREEN_BUG);
        animation = new BufferedImage[13];
        for (int i=0; i<animation.length; i++){
            animation[i] = temp.getSubimage(i * DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
                drawIndex = 5;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset - 50, WIDTH, HEIGHT, null);
    }
}
