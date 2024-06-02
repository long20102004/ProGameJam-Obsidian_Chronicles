package Upgrade;

import utilz.Constant;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Product {
    protected BufferedImage[][] product;
    protected Rectangle productBorder;
    protected Rectangle frameBorder;
    protected BufferedImage productFrame, highlighter;
    protected int xPos, yPos, number;
    protected Font numberFont, wordFont;
    protected String price = "100";
    protected String description = "Description: ...";
    protected boolean isMoved, isPressed, isReleased;
    protected int xDrawOffset, yDrawOffset;
    protected boolean isPicking;
    protected int xDescription = 600, yDescription = 300;
    public Product(int typePotionsX, int typePotionsY, int number){
        this.xPos = typePotionsX - 1;
        this.yPos = typePotionsY - 1;
        this.number = number;
        productFrame = LoadSave.getImg(LoadSave.PRODUCT_FRAME);
        highlighter = LoadSave.getImg(LoadSave.HIGHLIGHTER);
        importFont();
    }

    protected void importFont() {
        InputStream inputStream = getClass().getResourceAsStream("/Font/PixelFont.ttf");
        InputStream input = getClass().getResourceAsStream("/Font/PixelWord.ttf");
        try {
            numberFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            numberFont = numberFont.deriveFont(Font.PLAIN, 10);
            wordFont = Font.createFont(Font.TRUETYPE_FONT, input);
            wordFont = wordFont.deriveFont(Font.PLAIN, 15);
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
    }
    public void draw(Graphics g){
        g.setFont(numberFont);
        g.drawImage(productFrame, (int) (frameBorder.x - 5 + xDrawOffset), (int) (frameBorder.y + yDrawOffset), (int) (frameBorder.width + 10), (int) (frameBorder.height + 10), null);
        g.drawImage(product[xPos][yPos], (int) productBorder.x + xDrawOffset, (int) productBorder.y + yDrawOffset, (int) productBorder.width - 5, (int) productBorder.height - 5, null);
        g.drawString(price, (int) (frameBorder.x + frameBorder.width / 3), (int) (frameBorder.y + frameBorder.height + 5));
        g.setFont(wordFont);
        if (isPicking) {
            g.drawImage(highlighter, frameBorder.x - 36, frameBorder.y - 35, frameBorder.width + 70, frameBorder.height + 70, null);
            g.drawString(description, xDescription, yDescription);
        }
    }
    public void update(){
        if (isReleased){
            isMoved = isPressed = isReleased = false;
        }
        productBorder.width = Constant.PRODUCT.WIDTH;
        productBorder.height = Constant.PRODUCT.HEIGHT;
        isPicking = false;
        if (isMoved){
            isPicking = true;
            xDrawOffset = (int) (-0.05f * Constant.PRODUCT.WIDTH);
            yDrawOffset = (int) (-0.05f * Constant.PRODUCT.HEIGHT);
            productBorder.width = (int) (1.1f * Constant.PRODUCT.WIDTH);
            productBorder.height = (int) (1.1 * Constant.PRODUCT.HEIGHT);
        }
        if (isPressed){
            isPicking = true;
            xDrawOffset = (int) (0.05f * Constant.PRODUCT.WIDTH);
            yDrawOffset = (int) (0.05f * Constant.PRODUCT.HEIGHT);
            productBorder.width = (int) (0.9f * Constant.PRODUCT.WIDTH);
            productBorder.height = (int) (0.9f * Constant.PRODUCT.HEIGHT);
        }
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
