package Weapon;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Bullet {
    private float xPos,yPos;
    private BufferedImage[][] bullet;
    private BufferedImage[][] revBullet;
    private int aniTick = 0, aniSpeed = 30;
    public static final int MOVE_1 = 0;
    public static final int EXPLODE_1 = 1;
    public static final int MOVE_2 = 2;
    public static final int EXPLODE_2 = 3;
    public static final int MOVE_3 = 4;
    public static final int EXPLODE_3 = 5;
    private int drawIndex = 0;
    private int typeAction = MOVE_1;
    private Rectangle2D.Float hitbox;
    private float bulletSpeed = 3f;
    private float increaseSpeed = -0.001f;
    private boolean isLeft;
    private boolean isShot = true;
    private double rotateAngle;
    private double angleInDegrees;
    private double yDrawOffset;
    public Bullet(float xPos, float yPos, boolean isLeft, double rotateAngle){
        this.isLeft = isLeft;
        this.xPos = xPos + 25;
        this.yPos = yPos + 20;
        this.rotateAngle = rotateAngle;
        angleInDegrees = Math.toDegrees(rotateAngle);
        initClass();
    }

    private void initClass() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.BULLET);
        hitbox = new Rectangle2D.Float(xPos, yPos, 32, 32);
        bullet = new BufferedImage[6][5];
        revBullet = new BufferedImage[6][5];
        for (int i=0; i<bullet.length; i++){
            for (int j=0; j<bullet[0].length; j++){
                bullet[i][j] = tmp.getSubimage(j * 16, i * 16, 16, 16);
                revBullet[i][j] = ExtraMethods.reverseImg(bullet[i][j]);
            }
        }
        if (isLeft) {
            increaseSpeed *= -1;
            bulletSpeed *= -1;
        }
    }
    public void update(Game game){
        updateAniTick();
        updateXPos(game);
    }

    private void updateXPos(Game game) {
        yDrawOffset = rotateAngle * 20;
        hitbox.x += bulletSpeed;
        hitbox.y += (float) ((bulletSpeed) * Math.tan(rotateAngle));
        if (hitbox.x < 0 || hitbox.x > Game.GAME_WIDTH || hitbox.y < 0 || hitbox.y > Game.GAME_HEIGHT) isShot = false;
    }

    public void draw(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics.create();
        g.rotate(rotateAngle, hitbox.x, hitbox.y);
        if (isLeft) g.drawImage(revBullet[typeAction][drawIndex], (int) hitbox.x, (int) ((int) hitbox.y + yDrawOffset), (int) hitbox.width, (int) hitbox.height, null);
        else g.drawImage(bullet[typeAction][drawIndex], (int) hitbox.x, (int) ((int) hitbox.y + yDrawOffset), (int) hitbox.width, (int) hitbox.height, null);
        g.setColor(Color.RED);
//        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    private void updateAniTick(){
        aniTick++;
        if (aniTick > aniSpeed){
            drawIndex++;
            if (drawIndex >= getType(typeAction)){
                drawIndex = 0;
            }
        }
    }
    private int getType(int type){
        switch (type){
            case MOVE_1, MOVE_2, MOVE_3: return 3;
            case EXPLODE_1,EXPLODE_2: return 4;
            case EXPLODE_3: return 5;
        }
        return 1;
    }

    public boolean isShot() {
        return isShot;
    }
}
