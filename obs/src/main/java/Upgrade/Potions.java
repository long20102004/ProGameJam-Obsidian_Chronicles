package Upgrade;

import utilz.Constant;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Potions extends Product{
    public Potions(int typePotionsX, int typePotionsY, int number){
        super(typePotionsX, typePotionsY, number);
        this.type = Product.POTIONS;
        productBorder = new Rectangle(600, 350 + 100 * number, Constant.PRODUCT.WIDTH, Constant.PRODUCT.HEIGHT);
        frameBorder = productBorder;
        importPotion();
    }

    private void importPotion() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.POTIONS);
        product = new BufferedImage[4][5];
        for (int i = 0; i < product.length; i++) {
            for (int j=0; j<product[0].length; j++) {
                product[i][j] = tmp.getSubimage(j * Constant.PRODUCT.DEFAULT_WIDTH, Constant.PRODUCT.DEFAULT_HEIGHT * i, Constant.PRODUCT.DEFAULT_WIDTH, Constant.PRODUCT.DEFAULT_HEIGHT);
            }
        }
    }
}
