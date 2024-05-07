package Upgrade;

import utilz.Constant;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Weapons extends Product{
    public Weapons(int typePotionsX, int typePotionsY, int number){
        super(typePotionsX, typePotionsY, number);
        productBorder = new Rectangle(900, 350 + 100 * number, Constant.PRODUCT.WIDTH, Constant.PRODUCT.HEIGHT);
        frameBorder = productBorder;
        importSkills();
    }

    private void importSkills() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.WEAPONS);
        product = new BufferedImage[10][9];
        for (int i = 0; i < product.length; i++) {
            for (int j=0; j<product[0].length; j++) {
                product[i][j] = tmp.getSubimage(j * Constant.PRODUCT.DEFAULT_WIDTH, Constant.PRODUCT.DEFAULT_HEIGHT * i, Constant.PRODUCT.DEFAULT_WIDTH, Constant.PRODUCT.DEFAULT_HEIGHT);
            }
        }
    }
}
