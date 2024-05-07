package UI;

import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class SettingOverLay {
    private BufferedImage[] animation;
    private int WIDTH = 140, HEIGHT = 64;
    private int soundXPos, soundYPos;
    private Rectangle border;
    private int index;

    public SettingOverLay() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        soundXPos = toolkit.getScreenSize().width / 2 - WIDTH / 2;
        soundYPos = toolkit.getScreenSize().height / 2 - HEIGHT / 2;
        border = new Rectangle(soundXPos, soundYPos, WIDTH, HEIGHT);
        BufferedImage tmp = LoadSave.getImg(LoadSave.POWER_BAR);
        animation = new BufferedImage[13];
        for (int i = 0; i < animation.length; i++) {
            animation[i] = tmp.getSubimage(i * 35, 16, 35, 16);
        }
    }

    public void mouseDragged(MouseEvent e) {
        index = (int) ((1.0 * e.getX() - soundXPos) / border.width * 12);
        if (index < 0) index = 0;
        if (index > 12) index = 12;
    }

    public void mousePressed(MouseEvent e) {

    }

    public void update() {

    }

    public void draw(Graphics g) {
        g.drawImage(animation[index], soundXPos, soundYPos, border.width, border.height, null);
    }

    public Rectangle getBorder() {
        return border;
    }
}
