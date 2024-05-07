package Upgrade;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constant.PRODUCT.*;

public class Skills extends Product{
    public Skills(int typePotionsX, int typePotionsY, int number){
        super(typePotionsX, typePotionsY, number);
        initFunction();
        productBorder = new Rectangle(752, 352 + 100 * number, (int) (WIDTH), (int) (HEIGHT));
        frameBorder = new Rectangle(750, 350 + 100 * number, WIDTH, HEIGHT);
        importSkills();
    }

    private void importSkills() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.SKILLS);
        product = new BufferedImage[8][9];
        for (int i = 0; i < product.length; i++) {
            for (int j=0; j<product[0].length; j++) {
                product[i][j] = tmp.getSubimage(j * DEFAULT_WIDTH, DEFAULT_HEIGHT * i, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }
        }
    }
    private void initFunction(){
        if (xPos == 1 && yPos == 1) description += "Increase transform time";
        if (xPos == 3 && yPos == 6) description += "Increase attack range.";
        if (xPos == 1 && yPos == 7) description += "Increase defense";
    }

}
