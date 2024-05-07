package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Flower extends AnimatedObject{
    private ObjectLight objectLight;
    private final int xDrawOffset = (int) (15 * Game.MODE);
    private final int yDrawOffset = (int) (15 * Game.MODE);
    public Flower(int xPos, int yPos){
        super(xPos, yPos);
        initClass();
    }

    private void initClass() {
        objectLight = new ObjectLight(xPos, yPos, Constant.FLOWER.DEFAULT_SIZE, Constant.FLOWER.DEFAULT_SIZE, 1.3f);
        BufferedImage temp = LoadSave.getImg(LoadSave.FLOWER);
        animation = new BufferedImage[6];
        for (int i = 0; i< Constant.FLOWER.GLOW; i++){
            animation[i] = temp.getSubimage(0, i * Constant.FLOWER.DEFAULT_SIZE, Constant.FLOWER.DEFAULT_SIZE, Constant.FLOWER.DEFAULT_SIZE);
        }
    }
    public void update(){
        updateAniTick();
        objectLight.update();
    }


    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.FLOWER.GLOW){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        objectLight.draw(g, xLvlOffset, yLvlOffset, 0, 0);
        g.drawImage(animation[drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, Constant.FLOWER.SIZE, Constant.FLOWER.SIZE, null);
    }
}
