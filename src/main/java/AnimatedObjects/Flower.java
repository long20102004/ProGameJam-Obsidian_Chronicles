package AnimatedObjects;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static utilz.Constant.FLOWER.*;

public class Flower extends AnimatedObject{
    private ObjectLight objectLight;
    private final int xDrawOffset = (int) (15 * MODE);
    private final int yDrawOffset = (int) (15 * MODE);
    public Flower(int xPos, int yPos){
        super(xPos, yPos);
        initClass();
    }

    private void initClass() {
        objectLight = new ObjectLight(xPos, yPos, DEFAULT_SIZE, DEFAULT_SIZE, 1.3f);
        BufferedImage temp = LoadSave.getImg(LoadSave.FLOWER);
        animation = new BufferedImage[6];
        for (int i=0; i<GLOW; i++){
            animation[i] = temp.getSubimage(0, i * DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE);
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
            if (drawIndex >= GLOW){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        objectLight.draw(g, xLvlOffset, yLvlOffset, 0, 0);
        g.drawImage(animation[drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, SIZE,SIZE, null);
    }
}
