package Upgrade;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constant.PRODUCT.*;

public class Weapons extends Product{
    public Weapons(int typePotionsX, int typePotionsY, int number){
        super(typePotionsX, typePotionsY, number);
        productBorder = new Rectangle(900, 350 + 100 * number, WIDTH, HEIGHT);
        frameBorder = productBorder;
        importSkills();
    }

    private void importSkills() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.WEAPONS);
        product = new BufferedImage[10][9];
        for (int i = 0; i < product.length; i++) {
            for (int j=0; j<product[0].length; j++) {
                product[i][j] = tmp.getSubimage(j * DEFAULT_WIDTH, DEFAULT_HEIGHT * i, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }
        }
    }
}
