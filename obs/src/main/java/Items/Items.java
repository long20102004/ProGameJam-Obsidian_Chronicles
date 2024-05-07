package Items;

import Main.Game;
import Player.Player;
import utilz.LoadSave;
import org.springframework.stereotype.Component;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

@Component
public class Items {
    private BufferedImage[] animation;
    private final int xPos;
    private final int yPos;
    private Rectangle2D.Float hitbox;
    private final int type;
    private int aniTick = 0;
    private final int aniSpeed = 30;
    private int drawIndex = 0;
    private boolean isActive = true;
    public Items(int xPos, int yPos, int type){
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.ITEMS.WIDTH, Constant.ITEMS.HEIGHT);
        initClass();
    }
    private void initClass() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.ITEMS);
        animation = new BufferedImage[Constant.ITEMS.getType(type)];
        for (int i=0; i<animation.length; i++){
            animation[i] = tmp.getSubimage(Constant.ITEMS.DEFAULT_WIDTH * i, Constant.ITEMS.DEFAULT_HEIGHT * type, Constant.ITEMS.DEFAULT_WIDTH, Constant.ITEMS.DEFAULT_HEIGHT);
        }
    }
    private void updateAniTick(){
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.ITEMS.getType(type)){
                drawIndex = 0;
            }
        }
    }
    private void updateUsage(Game game){
        if (game.getPlayer().getHitbox().intersects(hitbox)){
            isActive = false;
            switch (type){
                case Constant.ITEMS.COINS1:
                    Player.coins++;
                    break;
                case Constant.ITEMS.BANDAGE:
                    game.getPlayer().updateHealthAndPower(30, 0, 0);
                    break;
            }
        }
    }
    public void update(Game game){
        updateAniTick();
        updateUsage(game);
    }
    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset, Constant.ITEMS.WIDTH, Constant.ITEMS.HEIGHT, null);
    }
    public boolean isActive() {
        return isActive;
    }
    public void resetAll(){
        drawIndex = 0;
        isActive = true;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.ITEMS.WIDTH, Constant.ITEMS.HEIGHT);
    }
}
