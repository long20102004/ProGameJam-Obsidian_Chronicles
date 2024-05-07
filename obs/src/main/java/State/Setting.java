package State;

import Main.Game;
import UI.SettingOverLay;
import utilz.ExtraMethods;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
@Component
public class Setting implements StateMethods{
    private Game game;
    private SettingOverLay settingOverLay;
    public Setting(Game game){
        this.game = game;
        settingOverLay = new SettingOverLay();
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE -> GameState.gameState = GameState.MENU;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (ExtraMethods.isIn(e, settingOverLay.getBorder())){
            settingOverLay.mouseDragged(e);
        }
    }

    @Override
    public void draw(Graphics g) {
        settingOverLay.draw(g);
    }

    @Override
    public void update() {

    }
}
