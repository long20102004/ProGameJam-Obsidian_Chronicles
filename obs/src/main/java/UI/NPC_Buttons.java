package UI;

import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC_Buttons {
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int A = 16;
    public static final int D = 19;
    public static final int E = 20;
    public static final int F = 21;
    public static final int G = 22;
    public static final int H = 23;
    public static final int J = 25;
    public static final int K = 26;

    public static final int U = 36;
    public static final int W = 38;
    private int type;
    private int index = 0;
    private int xPos, yPos;
    private BufferedImage[][] buttons, extraButtons;
    private int aniTick = 0, aniSpeed = 60;
    public NPC_Buttons(int type, int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
        buttons = new BufferedImage[57][2];
        BufferedImage tmp = LoadSave.getImg(LoadSave.KEYBOARD_BUTTON1);
        for (int i=0; i<7; i++){
            for (int j=0; j<8; j++){
                buttons[i * 8 + j][0] = tmp.getSubimage(j * 16, i * 16, 16, 16);
            }
        }
        for (int i=0; i<7; i++){
            for (int j=0; j<8; j++){
                buttons[i * 8 + j][1] = tmp.getSubimage(j * 16, (i + 7) * 16, 16, 16);
            }
        }
    }
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset){
        g.drawImage((Image) buttons[type][index], (int) (xPos - xLevelOffset), (int) (yPos - yLevelOffset), (int) (20 * Game.zoom), (int) (20 * Game.zoom), null);
        aniTick++;
        if (aniTick >= aniSpeed){
            index = 1 - index;
            aniTick = 0;
        }
    }
}
