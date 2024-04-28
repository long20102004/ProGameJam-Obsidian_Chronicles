package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static utilz.Constant.JUMP_BASE.IDLE;
import static utilz.Constant.PORTAL.*;

public class Portal extends AnimatedObject{
    private BufferedImage[][] animation;
    private ObjectLight objectLight;
    private int type = IDLE;
    private Rectangle2D.Float border;
    private boolean setType;
    private final int xDrawOffset = (int) (15 * MODE);
    private final int yDrawOffset = (int) (15 * MODE);
    public Portal(int xPos, int yPos){
        super(xPos, yPos);
        initClass();
    }

    private void initClass() {
        aniSpeed = 20;
        BufferedImage temp = LoadSave.getImg(LoadSave.BLOOD_PORTAL);
        animation = new BufferedImage[3][24];
        border = new Rectangle2D.Float(xPos + DEFAULT_WIDTH / 3, yPos, DEFAULT_WIDTH / 2, DEFAULT_HEIGHT);
        for (int i=0; i<3; i++){
            for (int j=0; j<24; j++){
                animation[i][j] = temp.getSubimage(j * DEFAULT_WIDTH, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }
        }
    }
    public void update(Game game){
        updateAniTick();
        updateStatus(game);
    }
    private void updateStatus(Game game) {
        if (game.getPlayer().getHitbox().intersects(border)){
            type = READY;
            if (game.getEnemyManager().checkWon(game.getLevelManager().getIndexLevel())){
                type = TELEPORT;
                if (!setType) {
                    drawIndex = 0;
                    setType = true;
                }
                if (drawIndex >= getType(TELEPORT) - 2) {
                    game.getLevelManager().loadNextLevel();
//                    game.getPlayer().getHitbox().x = game.getLevelManager().getLevel().getPlayerTeleport().x;
//                    game.getPlayer().getHitbox().y = game.getLevelManager().getLevel().getPlayerTeleport().y;
                }
            }
        }
    }
    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= getType(type) - 1){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[type][drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, WIDTH,HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) (border.x - xLvlOffset), (int) (border.y - yLvlOffset), (int) border.width, (int) border.height);
    }
}
