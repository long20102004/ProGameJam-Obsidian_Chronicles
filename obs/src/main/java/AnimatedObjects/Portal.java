package AnimatedObjects;

import Main.Game;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class Portal extends AnimatedObject{
    private BufferedImage[][] animation;
    private ObjectLight objectLight;
    private int type = Constant.JUMP_BASE.IDLE;
    private Rectangle2D.Float border;
    private boolean setType;
    private final int xDrawOffset = (int) (15 * Game.MODE);
    private final int yDrawOffset = (int) (15 * Game.MODE);
    public Portal(int xPos, int yPos){
        super(xPos, yPos);
        initClass();
    }

    private void initClass() {
        aniSpeed = 20;
        BufferedImage temp = LoadSave.getImg(LoadSave.BLOOD_PORTAL);
        animation = new BufferedImage[3][24];
        border = new Rectangle2D.Float(xPos + (float) Constant.PORTAL.DEFAULT_WIDTH / 3, yPos, (float) Constant.PORTAL.DEFAULT_WIDTH / 2, Constant.PORTAL.DEFAULT_HEIGHT);
        for (int i=0; i<3; i++){
            for (int j=0; j<24; j++){
                animation[i][j] = temp.getSubimage(j * Constant.PORTAL.DEFAULT_WIDTH, i * Constant.PORTAL.DEFAULT_HEIGHT, Constant.PORTAL.DEFAULT_WIDTH, Constant.PORTAL.DEFAULT_HEIGHT);
            }
        }
    }
    public void update(Game game){
        updateAniTick();
        updateStatus(game);
    }
    private void updateStatus(Game game) {
        if (game.getPlayer().getHitbox().intersects(border)){
            type = Constant.PORTAL.READY;
            if (game.getEnemyManager().checkWon(game.getLevelManager().getIndexLevel())){
                type = Constant.PORTAL.TELEPORT;
                if (!setType) {
                    drawIndex = 0;
                    setType = true;
                }
                if (drawIndex >= Constant.PORTAL.getType(Constant.PORTAL.TELEPORT) - 2) {
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
            if (drawIndex >= Constant.PORTAL.getType(type) - 1){
                drawIndex = 0;
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset){
        g.drawImage(animation[type][drawIndex], xPos - xLvlOffset - xDrawOffset, yPos - yLvlOffset - yDrawOffset, Constant.PORTAL.WIDTH, Constant.PORTAL.HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) (border.x - xLvlOffset), (int) (border.y - yLvlOffset), (int) border.width, (int) border.height);
    }
}
