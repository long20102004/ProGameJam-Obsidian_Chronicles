package Weapon;

import Main.Game;
import Player.Gunslinger;
import utilz.Constant;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Gun {
    private float xPos, yPos;
    private final Gunslinger gunslinger;
    private BufferedImage[][] guns;
    private BufferedImage[][] revGuns;
    private AimShot aimShot;
    private int typeGun;
    private int drawIndex;
    private int typeAction = Constant.GUN.IDLE_GUN_1;
    private int aniTick = 0;
    private final int aniSpeed = 30;
    private float xLevelOffset, yLevelOffset;
    public double rotateAngle;
    private double currentAngle;
    private double xDrawOffset, yDrawOffset;
    private boolean isChangeDir;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    public Gun(Gunslinger gunslinger){
        this.gunslinger = gunslinger;
        this.xPos = gunslinger.getHitbox().x - 10;
        this.yPos = gunslinger.getHitbox().y + 22;
        initClass();
    }

    private void initClass() {
        BufferedImage tmp = LoadSave.getImg(LoadSave.GUN);
        guns = new BufferedImage[5][4];
        revGuns = new BufferedImage[5][4];
        for (int i=0; i<guns.length; i++){
            for (int j=0; j<guns[0].length; j++){
                guns[i][j] = tmp.getSubimage(j * Constant.GUN.DEFAULT_WIDTH, i * Constant.GUN.DEFAULT_HEIGHT, Constant.GUN.DEFAULT_WIDTH, Constant.GUN.DEFAULT_HEIGHT);
                revGuns[i][j] = ExtraMethods.reverseImg(guns[i][j]);
            }
        }
        aimShot = new AimShot();
    }

    public void draw(Graphics g, float xLevelOffset,float yLevelOffset){
        this.xLevelOffset = xLevelOffset;
        this.yLevelOffset = yLevelOffset;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.rotate(rotateAngle, xPos, yPos);
        if (gunslinger.isRight()) g2d.drawImage(guns[typeAction][drawIndex], (int) (xPos), (int) (yPos), Constant.GUN.WIDTH, Constant.GUN.HEIGHT, null);
        else g2d.drawImage(revGuns[typeAction][drawIndex], (int) (xPos), (int) (yPos), Constant.GUN.WIDTH, Constant.GUN.HEIGHT, null);
        drawBullet(g);
    }

    private void drawBullet(Graphics g) {
        for (Bullet bullet : bullets){
            if (bullet.isShot()){
                bullet.draw(g);
            }
        }
    }
    private void updateBullet(Game game){
        for (Bullet bullet : bullets){
            if (bullet.isShot()){
                bullet.update(game);
            }
        }
    }

    public void update(Game game){
        updateStatus();
        updateAniTick();
        updatePos();
        aimShot.update(game);
        updateBullet(game);
    }

    private void updateStatus() {
        if (gunslinger.isAttacked()) {
            setState(Constant.GUN.SHOOT_GUN_1);
            bullets.add(new Bullet(xPos, yPos, gunslinger.isLeft(), rotateAngle));
        }
    }
    public void updateAngle(MouseEvent e) {
        double dx, dy;
        if (gunslinger.isRight()) {
            dx = e.getX() - (double) xPos + (double) Constant.GUN.WIDTH / 2;
            dy = e.getY() - (double) yPos + (double) Constant.GUN.HEIGHT / 2;
        }
        else{
            dx = (double) xPos - e.getX() + (double) Constant.GUN.WIDTH / 2;
            dy = (double) yPos - e.getY() + (double) Constant.GUN.HEIGHT / 2;
        }
        double newAngle = Math.atan2(dy, dx);
        double angle = Math.toDegrees(newAngle); // Chỉ chuyển đổi góc mới sang độ khi cần thiết
        if (gunslinger.isRight()){
            if (angle <= -35 || angle >= 50) {
                newAngle = currentAngle;
            }
            else {
                isChangeDir = false;
                currentAngle = newAngle;
                if (angle >= -90 && angle <= 0){
                    xDrawOffset = angle / 1.3;
                    yDrawOffset = -angle / 2.5;
                }
                else if (angle >= 0){
                    xDrawOffset = angle / 1.4;
                    yDrawOffset = -angle / 10;
                }
            }
        }
        else{
            if (angle <= -30 || angle >= 35) {
                newAngle = currentAngle;
            }
            else {
                isChangeDir = false;
                currentAngle = newAngle;
                if (angle >= -90 && angle <= 0){
                    xDrawOffset = angle / 1.3;
                    yDrawOffset = -angle;
                }
                else if (angle >= 0){
                    xDrawOffset = angle / 1.4;
                    yDrawOffset = -angle / 2;
                }
            }
        }
        rotateAngle = newAngle; // Cập nhật góc quay với góc mới (được giữ dưới dạng radian)
        if (gunslinger.isChangeDir()) {
            System.out.println("valid");
            rotateAngle = 90 * Math.PI / 180;
        }
    }



    private void updatePos() {
        if (gunslinger.isRight()) {
            this.xPos = (float) (gunslinger.getHitbox().x - 10 - xLevelOffset + xDrawOffset);
            this.yPos = (float) (gunslinger.getHitbox().y + 22 - yLevelOffset + yDrawOffset);
        }
        else{
            this.xPos = (float) (gunslinger.getHitbox().x - (float) Constant.PLAYER.GUNSLINGER.WIDTH / 2 + 5 - xLevelOffset + xDrawOffset);
            this.yPos = (float) (gunslinger.getHitbox().y + 25 - yLevelOffset + yDrawOffset);
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed){
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.GUN.getType(typeAction)){
                drawIndex = 0;
                typeAction = Constant.GUN.IDLE_GUN_1;
            }
        }
    }
    private void setState(int state){
        drawIndex = 0;
        this.typeAction = state;
    }
}
