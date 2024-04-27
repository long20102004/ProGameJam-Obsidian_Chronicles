package AnimatedObjects;

import Main.Game;
import Dialogue.Dialogue;
import Upgrade.Shop;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static utilz.Constant.MERCHANT.*;

public class Merchant {
    private int xPos;
    private int yPos;
    private BufferedImage[] animation;
    private int drawIndex = 0;
    private ObjectLight objectLight;
    private int aniTick = 0;
    private final int aniSpeed = 40;
    private final int xDrawOffset = (int) (0 * MODE);
    private final int yDrawOffset = (int) (-3 * MODE);
    private Rectangle2D.Float border;
    private Dialogue dialogue;
    private String[] conversation = new String[2];
    private Shop shop;
    public Merchant(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        border = new Rectangle2D.Float(xPos, yPos, WIDTH, HEIGHT);
        initClass();
    }

    private void initClass() {
        dialogue = new Dialogue();
        BufferedImage temp = LoadSave.getImg(LoadSave.MERCHANT);
        animation = new BufferedImage[20];
        for (int i=0; i<TURN_LIGHT; i++){
            animation[i] = temp.getSubimage(0, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
            if (drawIndex >= TURN_LIGHT){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
//        objectLight.draw(g, xLvlOffset, yLvlOffset, 0, (int) (15 * MODE));
        g.drawImage(animation[drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, WIDTH,HEIGHT, null);
        dialogue.draw(g);
    }
}
