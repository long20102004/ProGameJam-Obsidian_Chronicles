package Main;

import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static Main.Game.GAME_HEIGHT;
import static Main.Game.GAME_WIDTH;

public class GameWindow extends JFrame {
    private final JFrame jFrame;
    GamePanel gamePanel;
    public GameWindow(GamePanel gamePanel){
        jFrame = new JFrame();
        this.gamePanel = gamePanel;
        initClass();
    }

    private void initClass() {
        jFrame.add(gamePanel);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setMinimumSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        jFrame.setUndecorated(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage img = LoadSave.getImg(LoadSave.CURSOR);
        Cursor cursor = toolkit.createCustomCursor(img, new Point(0,0), "custom");
        jFrame.setCursor(cursor);
        jFrame.setVisible(true);
    }
    public void setCursor(BufferedImage img){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor cursor = toolkit.createCustomCursor(img, new Point(0,0), "Gun Point");
        jFrame.setCursor(cursor);
    }
}
