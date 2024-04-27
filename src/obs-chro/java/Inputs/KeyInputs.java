package Inputs;

import Main.GamePanel;
import State.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputs implements KeyListener {
    private final GamePanel gamePanel;
    public KeyInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {
//        if (GameState.gameState == GameState.PLAYING){
//            gamePanel.getGame().getPlaying().keyTyped(e);
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (GameState.gameState == GameState.PLAYING){
            gamePanel.getGame().getPlaying().keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.gameState){
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
            case SETTING -> gamePanel.getGame().getSetting().keyReleased(e);
        }
    }
}
