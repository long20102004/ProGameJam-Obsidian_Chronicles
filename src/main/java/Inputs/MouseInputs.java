package Inputs;

import Main.GamePanel;
import State.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;
    public MouseInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.gameState){
            case PLAYING :
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PAUSE:
                gamePanel.getGame().getPause().mousePressed(e);
                break;
            case SETTING:
                gamePanel.getGame().getSetting().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.gameState){
            case PLAYING :
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PAUSE:
                gamePanel.getGame().getPause().mouseReleased(e);
                break;
            case SETTING:
                gamePanel.getGame().getSetting().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.gameState){
            case PLAYING :
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseDragged(e);
                break;
            case PAUSE:
                gamePanel.getGame().getPause().mouseDragged(e);
                break;
            case SETTING:
                gamePanel.getGame().getSetting().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.gameState){
            case PLAYING :
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PAUSE:
                gamePanel.getGame().getPause().mouseMoved(e);
            case SETTING:
                gamePanel.getGame().getSetting().mouseMoved(e);
                break;
        }
    }
}
