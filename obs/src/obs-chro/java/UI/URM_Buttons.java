package UI;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constant.URM_BUTTONS.*;

public class URM_Buttons {
    private final int xPos;
    private final int yPos;
    private BufferedImage[] URM_Buttons;

    private boolean isMoved, isPressed, isReleased;
    private final Rectangle bound;
    private final int type;
    private int drawWidth = WIDTH, drawHeight = HEIGHT;
    private int xDrawOffset, yDrawOffset;
    public URM_Buttons( int xPos, int yPos, int type){
        this.xPos = xPos;
        this.yPos = yPos;
        bound = new Rectangle(xPos, yPos, WIDTH, HEIGHT);
        this.type = type;
        initClass();
    }
    private void initClass() {
        URM_Buttons = new BufferedImage[14];
        BufferedImage tmp = LoadSave.getImg(LoadSave.URM_BUTTONS);
        for (int i=0; i<URM_Buttons.length; i++){
            URM_Buttons[i] = tmp.getSubimage(0, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }
    public void update(){
        updateStatus();
    }

    private void updateStatus() {
        if (isReleased) resetStatus();
        drawWidth = WIDTH;
        drawHeight = HEIGHT;
        xDrawOffset = 0;
        yDrawOffset = 0;
        if (isMoved){
            drawWidth = (int) (WIDTH * 1.2);
            drawHeight = (int) (HEIGHT * 1.2);
            xDrawOffset = (int) (-0.1 * WIDTH);
            yDrawOffset = (int) (-0.1 * WIDTH);
        }
        if (isPressed){
            drawWidth = (int) (WIDTH * 0.8);
            drawHeight = (int) (HEIGHT * 0.8);
            xDrawOffset = (int) (0.1 * WIDTH);
            yDrawOffset = (int) (0.1 * WIDTH);
        }
    }

    private void resetStatus() {
        isReleased = false;
        isMoved = false;
        isPressed = false;
    }


    public void draw(Graphics g){
        g.drawImage(URM_Buttons[type], xPos + xDrawOffset, yPos + yDrawOffset, drawWidth, drawHeight, null);
    }

    public Rectangle getBound() {
        return bound;
    }
    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public void setReleased(boolean released) {
        isReleased = released;
    }
}
