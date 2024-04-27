package Dialogue;

import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Dialogue {
    private int xPos, yPos, width, height;
    private String[] dialogues = new String[100];
    private int currentDialogue = 0;
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final String instruction = "Press E to continue...";
    private Font newFont;
    private BufferedImage background;
    private boolean isTalking;
    public Dialogue(){
        background = LoadSave.getImg(LoadSave.DIALOGUE);
        importFont();
        setSize();
    }

    private void importFont() {
        InputStream inputStream = getClass().getResourceAsStream("/Font/PixelWord.ttf");
        try {
            newFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            newFont = newFont.deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setText(String[] dialogues) {
        this.dialogues = dialogues;
    }

    private void setSize() {
        Dimension dimension = toolkit.getScreenSize();
        int SCREEN_WIDTH = (int) dimension.getWidth();
        int SCREEN_HEIGHT = (int) dimension.getHeight();
        width = (int) (SCREEN_WIDTH / 1.5f);
        height = (int) (SCREEN_HEIGHT / 2f);
        xPos = SCREEN_WIDTH / 2 - width / 2;
        yPos = 100;
    }

    public void draw(Graphics graphics){
        if (!isTalking) return;
        Graphics2D g = (Graphics2D) graphics;
        g.setFont(newFont);
        graphics.drawImage(background, xPos, yPos,width,height,null);
        drawText(graphics);
    }

    private void updateTalking(Game game, Rectangle2D.Float objectBorder, int length){
        if (game.getPlayer().getHitbox().intersects(objectBorder)) {
            if (game.getPlayer().countTalking() > 0) {
                isTalking = true;
                currentDialogue = game.getPlayer().countTalking() - 1;
            }
            if (game.getPlayer().countTalking() > length) {
                isTalking = false;
                game.getPlayer().setCountTalking(0);
            }
        }
        else{
            isTalking = false;
        }
    }
    private void drawText(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(newFont);
        g.drawString(dialogues[currentDialogue], xPos + 70, yPos + 150);
        g.setFont(g.getFont().deriveFont(Font. PLAIN, 15));
        g.drawString(instruction, xPos + width - 300, yPos + height - 120);
    }
    public void update(Game game, Rectangle2D.Float border, int length){
        updateTalking(game, border, length);
    }
    public int getCurrentDialogue(){
        return currentDialogue;
    }
    public void setTalking(boolean isTalking){
        this.isTalking = isTalking;
    }
}
