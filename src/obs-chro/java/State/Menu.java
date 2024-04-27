package State;

import Main.Game;
import UI.GameOverLay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Menu implements StateMethods{
    private final Game game;
    GameOverLay gameOverLay = new GameOverLay();
    public Menu(Game game){
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gameOverLay.mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameOverLay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gameOverLay.mouseReleased(e, game);
    }
    @Override
    public void mouseDragged(MouseEvent e){
    }

    @Override
    public void draw(Graphics g) {
        gameOverLay.draw(g);
    }

    @Override
    public void update() {
        gameOverLay.update();
    }
}
