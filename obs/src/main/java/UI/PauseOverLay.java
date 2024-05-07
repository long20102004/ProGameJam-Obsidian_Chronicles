package UI;

import Main.Game;
import State.GameState;
import utilz.Constant;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverLay {
    private final BufferedImage pauseScreen;
    private final URM_Buttons resumeButtons;
    private final URM_Buttons restartButtons;
    private final URM_Buttons mainMenuButtons;
    public PauseOverLay() {
        resumeButtons = new URM_Buttons(650, 400, Constant.URM_BUTTONS.RESUME);
        restartButtons = new URM_Buttons(650, 500, Constant.URM_BUTTONS.RESTART);
        mainMenuButtons = new URM_Buttons(650, 600, Constant.URM_BUTTONS.MAIN_MENU);
        pauseScreen = LoadSave.getImg(LoadSave.PAUSE_BACKGROUND);
    }

    public void mouseMoved(MouseEvent e) {
        resumeButtons.setMoved(ExtraMethods.isIn(e, resumeButtons.getBound()));
        restartButtons.setMoved(ExtraMethods.isIn(e, restartButtons.getBound()));
        mainMenuButtons.setMoved(ExtraMethods.isIn(e, mainMenuButtons.getBound()));
    }

    public void mousePressed(MouseEvent e) {
//        playing.getGame().getAudioPlayer().playEffectSound(AudioPlayer.CLICKED);
        resumeButtons.setPressed(ExtraMethods.isIn(e, resumeButtons.getBound()));
        restartButtons.setPressed(ExtraMethods.isIn(e, restartButtons.getBound()));
        mainMenuButtons.setPressed(ExtraMethods.isIn(e, mainMenuButtons.getBound()));
    }

    public void mouseReleased(MouseEvent e, Game game) {
        resumeButtons.setReleased(true);
        restartButtons.setReleased(true);
        mainMenuButtons.setReleased(true);
        if (ExtraMethods.isIn(e, restartButtons.getBound())){
            game.resetAll();
            GameState.gameState = GameState.PLAYING;
        }
        if (ExtraMethods.isIn(e, resumeButtons.getBound())) {
            GameState.gameState = GameState.PLAYING;
        }
        if (ExtraMethods.isIn(e, mainMenuButtons.getBound())){
            GameState.gameState = GameState.MENU;
        }
    }


    public void update() {
        resumeButtons.update();
        restartButtons.update();
        mainMenuButtons.update();
    }

    public void draw(Graphics g) {
        g.drawImage(pauseScreen, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        resumeButtons.draw(g);
        restartButtons.draw(g);
        mainMenuButtons.draw(g);
    }
}
