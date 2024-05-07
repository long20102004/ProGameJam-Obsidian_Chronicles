package AnimatedObjects;

import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;


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
            animation[i] = temp.getSubimage(i * Constant.GREEN_BUG.DEFAULT_WIDTH, 0, Constant.GREEN_BUG.DEFAULT_WIDTH, Constant.GREEN_BUG.DEFAULT_HEIGHT);
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
            if (drawIndex + 1 >= Constant.GREEN_BUG.LIGHT){
                drawIndex = 5;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset - 50, Constant.GREEN_BUG.WIDTH, Constant.GREEN_BUG.HEIGHT, null);
    }
}
