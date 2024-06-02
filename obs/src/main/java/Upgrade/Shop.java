package Upgrade;

import Level.Level;
import Main.Game;
import State.Playing;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Shop {
    private boolean isShopping;
    private int xPos, yPos, width, height;
    private String[] dialogues = new String[100];
    private int currentDialogue = 0;
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final String instruction = "Press E to continue...";
    private Font newFont;
    private BufferedImage shopBackground;
    private Playing playing;
    private Level currentLevel;
    private int numberSkill = 1, numberWeapon = 1, numberPotion = 1;

    public Shop(Playing playing) {
        this.playing = playing;
        importFont();
        importShop();
        setSize();
    }

    private void importShop() {
        currentLevel = playing.getGame().getLevelManager().getLevel();
        shopBackground = LoadSave.getImg(LoadSave.SHOP_BACKGROUND);
        importWeapon();
        importPotion();
        importSkill();
    }

    private void importSkill() {
        currentLevel.products.add(new Skills(2,2,numberSkill++));
        currentLevel.products.add(new Skills(4,7, numberSkill++));
        currentLevel.products.add(new Skills(2,8,numberSkill++));
    }

    private void importPotion() {
        currentLevel.products.add(new Potions(1,4, numberPotion++));
        currentLevel.products.add(new Potions(3,5,numberPotion++));
        currentLevel.products.add(new Potions(2,5,numberPotion++));
    }

    private void importWeapon() {
        currentLevel.products.add(new Weapons(3,5,numberWeapon++));
        currentLevel.products.add(new Weapons(9,9, numberWeapon++));
        currentLevel.products.add(new Weapons(2,9,numberWeapon++));
    }

    private void importFont() {
        InputStream inputStream = getClass().getResourceAsStream("/Font/PixelFont.ttf");
        try {
            newFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            newFont = newFont.deriveFont(40f);
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
        width = (int) (SCREEN_WIDTH / 2.5f);
        height = (int) (SCREEN_HEIGHT / 1.2f);
        xPos = SCREEN_WIDTH / 2 - width / 2;
        yPos = 100;
    }

    public void draw(Graphics graphics) {
        graphics.setFont(newFont);
        graphics.setColor(Color.BLACK);
        graphics.drawImage(shopBackground, xPos, yPos, width, height, null);
        graphics.drawString("SHOP", xPos + 250, yPos + 150);
        for (Product product : currentLevel.products){
            product.draw(graphics);
        }
    }
    private void drawText(Graphics g) {
        g.setFont(newFont);
        g.drawString(dialogues[currentDialogue], xPos + 50, yPos + 50);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 15));
        g.drawString(instruction, xPos + width - 200, yPos + height - 30);
    }
    public void mouseMoved(MouseEvent e){
        for (Product product : currentLevel.products){
            if (ExtraMethods.isIn(e, product.productBorder)){
                product.setMoved(true);
            }
            else product.setMoved(false);
        }
    }
    public void mousePressed(MouseEvent e){
        for (Product product : currentLevel.products){
            if (ExtraMethods.isIn(e, product.productBorder)){
                product.setPressed(true);
            }
            else product.setPressed(false);
        }
    }
    public void mouseReleased(MouseEvent e){
        for (Product product : currentLevel.products){
            if (ExtraMethods.isIn(e, product.productBorder)){
                product.setReleased(true);
            }
            else product.setReleased(false);
        }
    }
    public void update(Game game){
        for (Product product : currentLevel.products){
            product.update(game);
        }
    }
    public void setShopping(boolean shopping){
        isShopping = shopping;
    }
    public boolean isShopping(){
        return isShopping;
    }

}

