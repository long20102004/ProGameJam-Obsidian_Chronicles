package UI;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Main.Game.GAME_HEIGHT;
import static Main.Game.GAME_WIDTH;

public class GameScreen {
    private final GameOverLay gameOverLay;
    private BufferedImage background;
    public GameScreen(GameOverLay gameOverLay){
        this.gameOverLay = gameOverLay;
        initClass();
    }

    private void initClass() {
        background = LoadSave.getImg(LoadSave.MENU_SCREEN);
    }

    public void draw(Graphics g){
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
    }
}
