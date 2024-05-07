package Upgrade;

import Level.Level;
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
        currentLevel.skillsList.add(new Skills(2,2,numberSkill++));
        currentLevel.skillsList.add(new Skills(4,7, numberSkill++));
        currentLevel.skillsList.add(new Skills(2,8,numberSkill++));
    }

    private void importPotion() {
        currentLevel.potionsList.add(new Potions(1,4, numberPotion++));
        currentLevel.potionsList.add(new Potions(3,5,numberPotion++));
        currentLevel.potionsList.add(new Potions(2,5,numberPotion++));
    }

    private void importWeapon() {
        currentLevel.weaponsList.add(new Weapons(3,5,numberWeapon++));
        currentLevel.weaponsList.add(new Weapons(9,9, numberWeapon++));
        currentLevel.weaponsList.add(new Weapons(2,9,numberWeapon++));
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
        for (Weapons weapons : currentLevel.weaponsList){
            weapons.draw(graphics);
        }
        for (Potions potions : currentLevel.potionsList){
            potions.draw(graphics);
        }
        for (Skills skills : currentLevel.skillsList){
            skills.draw(graphics);
        }
    }
    private void drawText(Graphics g) {
        g.setFont(newFont);
        g.drawString(dialogues[currentDialogue], xPos + 50, yPos + 50);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 15));
        g.drawString(instruction, xPos + width - 200, yPos + height - 30);
    }

    public void setCurrentDialogue(int dialogue) {
        currentDialogue = dialogue;
    }

    public void mouseMoved(MouseEvent e){
        for (Skills skills : currentLevel.skillsList){
            if (ExtraMethods.isIn(e, skills.productBorder)){
                skills.setMoved(true);
            }
            else skills.setMoved(false);
        }
        for (Weapons weapons : currentLevel.weaponsList){
            if (ExtraMethods.isIn(e, weapons.productBorder)){
                weapons.setMoved(true);
            }
            else weapons.setMoved(false);
        }
        for (Potions potions : currentLevel.potionsList){
            if (ExtraMethods.isIn(e, potions.productBorder)){
                potions.setMoved(true);
            }
            else potions.setMoved(false);
        }
    }
    public void mousePressed(MouseEvent e){
        for (Skills skills : currentLevel.skillsList){
            if (ExtraMethods.isIn(e, skills.productBorder)){
                skills.setPressed(true);
            }
            else skills.setPressed(false);
        }
        for (Weapons weapons : currentLevel.weaponsList){
            if (ExtraMethods.isIn(e, weapons.productBorder)){
                weapons.setPressed(true);
            }
            else weapons.setPressed(false);
        }
        for (Potions potions : currentLevel.potionsList){
            if (ExtraMethods.isIn(e, potions.productBorder)){
                potions.setPressed(true);
            }
            else potions.setPressed(false);
        }
    }
    public void mouseReleased(MouseEvent e){
        for (Skills skills : currentLevel.skillsList){
            skills.setReleased(true);
        }
        for (Weapons weapons : currentLevel.weaponsList){
            weapons.setReleased(true);
        }
        for (Potions potions : currentLevel.potionsList){
            potions.setReleased(true);
        }
    }
    public void update(){
        for (Skills skills : currentLevel.skillsList){
            skills.update();
        }
        for (Weapons weapons : currentLevel.weaponsList){
            weapons.update();
        }
        for (Potions potions : currentLevel.potionsList){
            potions.update();
        }
    }
    public void setShopping(boolean shopping){
        isShopping = shopping;
    }
    public boolean isShopping(){
        return isShopping;
    }

}

