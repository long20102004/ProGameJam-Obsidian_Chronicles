package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Trees extends AnimatedObject {
    private BufferedImage[][] tree1Animation;
    private BufferedImage[][] tree2Animation;
    private final int typeTree;
    private final int typeAction = new Random().nextInt(Constant.ANIMATED_TREE.BLOOD, Constant.ANIMATED_TREE.GROWN + 1);
    private final int xDrawOffset = (int) (-17 * Game.MODE);
    private int yDrawOffset = (int) (-17 * Game.MODE);
    public Trees(int xPos, int yPos, int typeTree){
        super(xPos, yPos);
        this.typeTree = typeTree;
        if (typeTree == 1){
            yDrawOffset += Constant.ANIMATED_TREE.TREE1_HEIGHT;
        }
        if (typeTree == 2){
            yDrawOffset += Constant.ANIMATED_TREE.TREE2_HEIGHT;
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
                tree1Animation[i][j] = img1.getSubimage(j * Constant.ANIMATED_TREE.TREE1_DEFAULT_WIDTH, i * Constant.ANIMATED_TREE.TREE1_DEFAULT_HEIGHT, Constant.ANIMATED_TREE.TREE1_DEFAULT_WIDTH, Constant.ANIMATED_TREE.TREE1_DEFAULT_HEIGHT);
                tree2Animation[i][j] = img2.getSubimage(j * Constant.ANIMATED_TREE.TREE2_DEFAULT_WIDTH, i * Constant.ANIMATED_TREE.TREE2_DEFAULT_HEIGHT, Constant.ANIMATED_TREE.TREE2_DEFAULT_WIDTH, Constant.ANIMATED_TREE.TREE2_DEFAULT_HEIGHT);
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
            if (drawIndex >= Constant.ANIMATED_TREE.getType(typeAction)){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        if (typeTree == 1) g.drawImage(tree1Animation[typeAction][drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, Constant.ANIMATED_TREE.TREE1_WIDTH, Constant.ANIMATED_TREE.TREE1_HEIGHT, null);
        if (typeTree == 2) g.drawImage(tree2Animation[typeAction][drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, Constant.ANIMATED_TREE.TREE2_WIDTH, Constant.ANIMATED_TREE.TREE2_HEIGHT, null);
    }
}
