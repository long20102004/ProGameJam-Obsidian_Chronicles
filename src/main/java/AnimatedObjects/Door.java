package AnimatedObjects;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Main.Game.*;
import static utilz.Constant.DOOR.*;
public class Door extends AnimatedObject{
    private final boolean isReversed;
    private final int xDrawOffset = (int) (15 * MODE);
    private float WIDTH, HEIGHT;
    private final int state = IDLE;

    public Door(int xPos, int yPos, boolean isReversed) {
        super(xPos, yPos);
        this.isReversed = isReversed;
        initClass();
    }

    private void initClass() {
        initDoor();
        BufferedImage temp = LoadSave.getImg(LoadSave.DOOR);
        animation = new BufferedImage[15];
        for (int i = 0; i < animation.length; i++) {
            animation[i] = temp.getSubimage(i * DEFAULT_WIDTH, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }
    private void initDoor(){
        for (int i=yPos; i<=GAME_HEIGHT; i++){
            if (ExtraMethods.isTileSolid(xPos, i)) break;
            else{
                HEIGHT += 0.5f;
                WIDTH += 0.3f;
            }
        }
    }
    public void update(Game game) {
        if (state != IDLE) updateAniTick();
//        else{
//            int column = xPos / TILE_SIZE;
//            int startRow = yPos / TILE_SIZE, endRow = (int) ((yPos + HEIGHT) / TILE_SIZE);
//            for (int i=startRow; i<=endRow; i++){
//                game.getLevelManager().getLevel().setTileData(i, column, 14 );
//            }
//        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= TURN) {
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        if (isReversed) {
            g.drawImage(animation[drawIndex], xPos - xLvlOffset + xDrawOffset, yPos - yLvlOffset, (int) -WIDTH, (int) HEIGHT, null);
        }
        else {
            g.drawImage(animation[drawIndex], xPos - xLvlOffset, yPos - yLvlOffset, (int) WIDTH, (int) HEIGHT, null);
        }
    }
}
