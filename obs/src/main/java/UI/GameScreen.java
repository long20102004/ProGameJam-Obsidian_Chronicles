package UI;

import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
    }
}
