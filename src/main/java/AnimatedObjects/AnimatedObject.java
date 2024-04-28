package AnimatedObjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AnimatedObject {
    protected int xPos, yPos;
    protected BufferedImage[] animation;
    protected int drawIndex = 0, aniTick = 0;
    protected int aniSpeed = 40;
    public AnimatedObject(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public AnimatedObject(){

    }
    public void update(){

    }
    public void draw(Graphics g, int xLevelOffset, int yLevelOffset){

    }
}
