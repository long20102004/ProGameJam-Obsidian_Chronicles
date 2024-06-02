package UI;

import java.awt.*;
import java.awt.event.MouseEvent;

import Audio.AudioPlayer;
import Main.Game;
import OnlineData.ImageSender;
import State.GameState;
import utilz.Constant;
import utilz.ExtraMethods;

public class GameOverLay {
    private final GameScreen gameScreen;
    private final URM_Buttons newGameButtons;
    private final URM_Buttons continueButtons;
    private final URM_Buttons settingButtons;
    private final URM_Buttons exitButtons;

    public GameOverLay() {
        gameScreen = new GameScreen(this);
        newGameButtons = new URM_Buttons(650, 250, Constant.URM_BUTTONS.NEW_GAME);
        continueButtons = new URM_Buttons(650, 350, Constant.URM_BUTTONS.CONTINUE);
        settingButtons = new URM_Buttons(650, 450, Constant.URM_BUTTONS.SETTINGS);
        exitButtons = new URM_Buttons(650, 550, Constant.URM_BUTTONS.EXIT);
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

    public void mouseReleased(MouseEvent e, Game game){
        game.getAudioPlayer().playEffectSound(AudioPlayer.CLICKED);
        newGameButtons.setReleased(true);
        settingButtons.setReleased(true);
        continueButtons.setReleased(true);
        exitButtons.setReleased(true);
        if (ExtraMethods.isIn(e, newGameButtons.getBound())){
            game.resetAll();
//            game.getPlaying().sendData();
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
