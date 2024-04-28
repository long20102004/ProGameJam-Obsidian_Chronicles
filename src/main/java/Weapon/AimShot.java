package Weapon;

import Main.Game;
import utilz.LoadSave;

import java.awt.image.BufferedImage;

public class AimShot {
    private BufferedImage[] cursor;
    private final int typeAimCursor = 5;
    public AimShot(){
        initClass();
    }

    private void initClass() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.AIM_CURSOR);
        cursor = new BufferedImage[10];
        for (int i=0; i<10; i++){
            cursor[i] = tmp.getSubimage(16 * i, 0, 16, 16);
        }
    }
    public void update(Game game){
        setCursor(game);
    }
    private void setCursor(Game game){
        game.getGameWindow().setCursor(cursor[typeAimCursor]);
    }
}
