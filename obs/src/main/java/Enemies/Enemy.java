package Enemies;

import Main.Game;
import UI.EnemyHealthBar;
import utilz.ExtraMethods;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.reward;

@Component
public class Enemy {

    protected BufferedImage[][] animation;
    protected BufferedImage[][] revAnimation;
    protected int drawIndex = 0;
    protected int aniTick = 0;
    protected int aniSpeed;
    protected int attackSight;
    protected int sight;
    protected int xDrawOffset;
    protected int yDrawOffset;
    protected int xPos, yPos;
    protected float speed = 1f;
    protected float fallSpeed = 0f, gravity = 0.04f;
    protected float fallSpeedConst = 0.5f;
    protected float jumpSpeed = -5f;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected boolean isMoving;
    protected boolean isRight;
    protected boolean isLeft;
    protected boolean inAir;
    protected boolean isAttacking;
    protected boolean isJump;
    protected boolean isRevive;
    protected boolean isHit;
    protected boolean isDead = false;
    protected boolean isActive = true;
    protected boolean isAttacked;
    protected int state;
    protected int maxHealth = 100;
    protected int maxPower = 100;
    protected int currentHealth = maxHealth;
    protected int currentPower = maxPower;
    protected boolean isFly;
    protected EnemyHealthBar healthBar;
    protected boolean canShoot = false;
    protected void update(Game game){

    }
    protected void draw(Graphics g, int xLevelOffset, int yLevelOffset){

    }
    public void move() {
        updateInAir();
        float xspeed = speed;
        if (isLeft) xspeed *= -1;
        if (ExtraMethods.isMovingPossible(hitbox, hitbox.x + xspeed, hitbox.y)) {
            hitbox.x += xspeed;
        }
    }

    protected void updateInAir() {
        if (isFly) return;
        if (!ExtraMethods.isEntityOnFloor(hitbox)) inAir = true;
        if (isJump) {
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + jumpSpeed)) {
                hitbox.y += jumpSpeed;
                jumpSpeed += gravity;
            }
        }
        if (inAir) {
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + fallSpeed)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                if (fallSpeed > 0){
                    hitbox.y = ExtraMethods.updateSpaceBetweenYAndWall(hitbox, fallSpeed);
                }
                resetInAir();
            }
        }
    }

    protected void resetInAir() {
        fallSpeedConst = 0.5f;
        fallSpeed = 0f;
        jumpSpeed = -4f;
        inAir = false;
    }

    public void enemyAttack(Game game, int damage, int knockBack) {
        if (canShoot) return;
        if (attackBox.intersects(game.getPlayer().getHitbox()))game.getPlayer().updateHealthAndPower(-damage, 0,knockBack);
    }

    public void hurt(int damage) {

    }

    public Rectangle2D.Float getAttackBox() {
        return attackBox;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void resetAll() {
        isActive = true;
        isAttacked = isHit = false;
        isLeft = false;
        isRight = true;
        isRevive = false;
        isMoving = false;
        speed = 1f;
        fallSpeed = 0.5f;
        gravity = 0.04f;
        jumpSpeed = -5f;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
