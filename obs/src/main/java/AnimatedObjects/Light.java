package AnimatedObjects;

import Player.Player;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Light extends AnimatedObject{
    private final Player player;
    BufferedImage animation;
    public Light(Player player){
        super();
        this.player = player;
        initClass();
    }

    private void initClass() {
        animation = LoadSave.getImg(LoadSave.YELLOW_LIGHT);
    }
    public void update(){
        xPos = (int) player.getHitbox().getX() - 170;
        yPos = (int) player.getHitbox().getY() - 100;
    }
    public void draw(Graphics g, float xLvlOffset, float yLvlOffset){
        g.drawImage(animation, (int) (xPos - xLvlOffset), (int) (yPos - yLvlOffset), Constant.LIGHT.DEFAULT_WIDTH, Constant.LIGHT.DEFAULT_HEIGHT - 100, null);
    }
}
