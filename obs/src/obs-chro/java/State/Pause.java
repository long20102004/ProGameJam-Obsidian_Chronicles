package State;

import Main.Game;
import UI.PauseOverLay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Pause implements StateMethods{
    private final Game game;
    private int xDrawOffset, yDrawOffset;
    PauseOverLay pauseOverLay = new PauseOverLay();
    public Pause(Game game){
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
        pauseOverLay.mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pauseOverLay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pauseOverLay.mouseReleased(e,game);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void draw(Graphics g) {
        game.getLevelManager().draw(g, xDrawOffset, yDrawOffset);
        game.getObjectManager().draw(g,xDrawOffset, yDrawOffset);
        if (game.getPlayer().getActive()) game.getPlayer().draw(g, xDrawOffset, yDrawOffset);
        game.getEnemyManager().draw(g, xDrawOffset, yDrawOffset);
        game.getItemsManager().draw(g, xDrawOffset, yDrawOffset);
        game.getNpcManager().draw(g, xDrawOffset, yDrawOffset);
        pauseOverLay.draw(g);
    }

    @Override
    public void update() {
        updateDrawOffset();
        pauseOverLay.update();
    }

    private void updateDrawOffset() {
        xDrawOffset = game.getPlaying().getxDrawOffset();
        yDrawOffset = game.getPlaying().getyDrawOffset();
    }
}
