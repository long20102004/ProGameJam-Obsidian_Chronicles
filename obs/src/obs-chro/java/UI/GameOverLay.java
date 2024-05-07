package UI;

import java.awt.*;
import java.awt.event.MouseEvent;

import Main.Game;
import State.GameState;
import utilz.ExtraMethods;

import static utilz.Constant.URM_BUTTONS.*;

public class GameOverLay {
    private final GameScreen gameScreen;
    private final URM_Buttons newGameButtons;
    private final URM_Buttons continueButtons;
    private final URM_Buttons settingButtons;
    private final URM_Buttons exitButtons;

    public GameOverLay() {
        gameScreen = new GameScreen(this);
        newGameButtons = new URM_Buttons(650, 250, NEW_GAME);
        continueButtons = new URM_Buttons(650, 350, CONTINUE);
        settingButtons = new URM_Buttons(650, 450, SETTINGS);
        exitButtons = new URM_Buttons(650, 550, EXIT);
    }

    public void mouseMoved(MouseEvent e) {
        newGameButtons.setMoved(ExtraMethods.isIn(e, newGameButtons.getBound()));
        settingButtons.setMoved(ExtraMethods.isIn(e, settingButtons.getBound()));
        continueButtons.setMoved(ExtraMethods.isIn(e, continueButtons.getBound()));
        exitButtons.setMoved(ExtraMethods.isIn(e, exitButtons.getBound()));
    }

    public void mousePressed(MouseEvent e) {
//        playing.getGame().getAudioPlayer().playEffectSound(AudioPlayer.CLICKED);
        newGameButtons.setPressed(ExtraMethods.isIn(e, newGameButtons.getBound()));
        settingButtons.setPressed(ExtraMethods.isIn(e, settingButtons.getBound()));
        continueButtons.setPressed(ExtraMethods.isIn(e, continueButtons.getBound()));
        exitButtons.setPressed(ExtraMethods.isIn(e, exitButtons.getBound()));
    }

    public void mouseReleased(MouseEvent e, Game game) {
        newGameButtons.setReleased(true);
        settingButtons.setReleased(true);
        continueButtons.setReleased(true);
        exitButtons.setReleased(true);
        if (ExtraMethods.isIn(e, newGameButtons.getBound())){
            game.resetAll();
            GameState.gameState = GameState.PLAYING;
        }
        if (ExtraMethods.isIn(e, continueButtons.getBound())) {
            GameState.gameState = GameState.PLAYING;
        }
        if (ExtraMethods.isIn(e, settingButtons.getBound())){
            GameState.gameState = GameState.SETTING;
        }
        if (ExtraMethods.isIn(e, exitButtons.getBound())){
            System.exit(0);
        }
    }
    public void mouseDragged(MouseEvent e){
    }


    public void update() {
        newGameButtons.update();
        settingButtons.update();
        continueButtons.update();
        exitButtons.update();
    }

    public void draw(Graphics g) {
        gameScreen.draw(g);
        newGameButtons.draw(g);
        settingButtons.draw(g);
        continueButtons.draw(g);
        exitButtons.draw(g);
    }
}
