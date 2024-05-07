package AnimatedObjects;

import Dialogue.Dialogue;
import Main.Game;
import Upgrade.Shop;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Merchant extends AnimatedObject{
    private final int xDrawOffset = (int) (0 * Game.MODE);
    private final int yDrawOffset = (int) (-3 * Game.MODE);
    private Rectangle2D.Float border;
    private Dialogue dialogue;
    private String[] conversation = new String[2];
    private Shop shop;
    public Merchant(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        border = new Rectangle2D.Float(xPos, yPos, Constant.MERCHANT.WIDTH, Constant.MERCHANT.HEIGHT);
        initClass();
    }

    private void initClass() {
        dialogue = new Dialogue();
        BufferedImage temp = LoadSave.getImg(LoadSave.MERCHANT);
        animation = new BufferedImage[20];
        for (int i = 0; i< Constant.MERCHANT.TURN_LIGHT; i++){
            animation[i] = temp.getSubimage(0, i * Constant.MERCHANT.DEFAULT_HEIGHT, Constant.MERCHANT.DEFAULT_WIDTH, Constant.MERCHANT.DEFAULT_HEIGHT);
        }
        conversation[0] = "Hello, Boy";
        conversation[1] = "Welcome to my shop, what do you want to buy?";
        dialogue.setText(conversation);
    }
    public void update(Game game){
        updateAniTick();
        dialogue.update(game, border, conversation.length);
        updateShopping(game);
    }

    private void updateShopping(Game game) {
        if (dialogue.getCurrentDialogue() == conversation.length){
            dialogue.setTalking(false);
            game.getPlaying().getShop().setShopping(true);
        }
        else game.getPlaying().getShop().setShopping(false);
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.MERCHANT.TURN_LIGHT){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
//        objectLight.draw(g, xLvlOffset, yLvlOffset, 0, (int) (15 * MODE));
        g.drawImage(animation[drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, Constant.MERCHANT.WIDTH, Constant.MERCHANT.HEIGHT, null);
        dialogue.draw(g);
    }
}
