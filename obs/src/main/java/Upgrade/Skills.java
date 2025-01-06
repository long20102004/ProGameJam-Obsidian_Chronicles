package Upgrade;

import utilz.Constant;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Skills extends Product{
    public Skills(int typePotionsX, int typePotionsY, int number){
        super(typePotionsX, typePotionsY, number);
        this.type = Product.SKILLS;
        initFunction();
        productBorder = new Rectangle(752, 352 + 100 * number, (int) (Constant.PRODUCT.WIDTH), (int) (Constant.PRODUCT.HEIGHT));
        frameBorder = new Rectangle(750, 350 + 100 * number, Constant.PRODUCT.WIDTH, Constant.PRODUCT.HEIGHT);
        importSkills();
    }

    private void importSkills() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.SKILLS);
        product = new BufferedImage[8][9];
        for (int i = 0; i < product.length; i++) {
            for (int j=0; j<product[0].length; j++) {
                product[i][j] = tmp.getSubimage(j * Constant.PRODUCT.DEFAULT_WIDTH, Constant.PRODUCT.DEFAULT_HEIGHT * i, Constant.PRODUCT.DEFAULT_WIDTH, Constant.PRODUCT.DEFAULT_HEIGHT);
            }
        }
    }
    private void initFunction(){
        if (xPos == 1 && yPos == 1) description = "Unlock Hoarder transform";
        if (xPos == 3 && yPos == 6) description = "Increase damage by 10";
        if (xPos == 1 && yPos == 7) description = "Increase max health by 100";
    }

}
