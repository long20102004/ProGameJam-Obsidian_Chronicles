package AnimatedObjects;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Main.Game.MODE;
import static utilz.Constant.ANIMATED_TREE.*;


public class Trees {
    private final int xPos;
    private final int yPos;
    private BufferedImage[][] tree1Animation;
    private BufferedImage[][] tree2Animation;
    private int drawIndex = 0;
    private int aniTick = 0;
    private final int aniSpeed = 40;
    private final int typeTree;
    private final int typeAction = new Random().nextInt(BLOOD, GROWN + 1);
    private final int xDrawOffset = (int) (-17 * MODE);
    private int yDrawOffset = (int) (-17 * MODE);
    public Trees(int xPos, int yPos, int typeTree){
        this.xPos = xPos;
        this.yPos = yPos;
        this.typeTree = typeTree;
        if (typeTree == 1){
            yDrawOffset += TREE1_HEIGHT;
        }
        if (typeTree == 2){
            yDrawOffset += TREE2_HEIGHT;
        }
        initClass();
    }

    private void initClass() {
        BufferedImage img1 = LoadSave.getImg(LoadSave.TREE_TYPE_1);
        BufferedImage img2 = LoadSave.getImg(LoadSave.TREE_TYPE_2);
        tree1Animation = new BufferedImage[4][16];
        tree2Animation = new BufferedImage[4][16];
        for (int i=0; i<tree1Animation.length; i++){
            for (int j=0; j<tree1Animation[0].length; j++){
                tree1Animation[i][j] = img1.getSubimage(j * TREE1_DEFAULT_WIDTH, i * TREE1_DEFAULT_HEIGHT, TREE1_DEFAULT_WIDTH, TREE1_DEFAULT_HEIGHT);
                tree2Animation[i][j] = img2.getSubimage(j * TREE2_DEFAULT_WIDTH, i * TREE2_DEFAULT_HEIGHT, TREE2_DEFAULT_WIDTH, TREE2_DEFAULT_HEIGHT);
            }
        }
    }
    public void update(){
        updateAniTick();
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= getType(typeAction)){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        if (typeTree == 1) g.drawImage(tree1Animation[typeAction][drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, TREE1_WIDTH, TREE1_HEIGHT, null);
        if (typeTree == 2) g.drawImage(tree2Animation[typeAction][drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, TREE2_WIDTH, TREE2_HEIGHT, null);
    }
}
