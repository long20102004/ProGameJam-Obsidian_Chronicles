package Enemies;

import Main.Game;
import UI.EnemyHealthBar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utilz.Constant;
import utilz.ExtraMethods;
import org.springframework.stereotype.Component;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Random;

import static Main.Game.reward;
@Getter
@Setter
@NoArgsConstructor
public class Enemy {
    protected BufferedImage rawImage;
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
    protected int hitboxWidth;
    protected int hitBoxHeight;
    protected int attackBoxWidth;
    protected int attackBoxHeight;
    protected int tileWidth;
    protected int tileHeight;
    protected int imageWidth;
    protected int imageHeight;
    protected int healthBarWidth;
    protected int healthBarHeight;
    protected int drawWidth;
    protected int drawHeight;
    protected float attackBoxChange;
    protected int hitState;
    protected int attackState;
    protected int deadState;
    protected int defaultState;
    private Instant lastDamageTime;
    private int damageTaken;
    protected int xDeltaRandom, yDeltaRandom;
    Random rand = new Random();
    protected void initEnemy(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        animation = new BufferedImage[imageHeight][imageWidth];
        revAnimation = new BufferedImage[imageHeight][imageWidth];
        hitbox = new Rectangle2D.Float(xPos, yPos, hitboxWidth, hitBoxHeight);
        attackBox = new Rectangle2D.Float(xPos, yPos, attackBoxWidth, attackBoxHeight);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = rawImage.getSubimage(j * tileWidth, i * tileHeight, tileWidth, tileHeight);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        setHealthBar(new EnemyHealthBar(this, healthBarWidth, healthBarHeight));
    }
    protected void update(Game game){

    }
    protected void draw(Graphics g, int xLevelOffset, int yLevelOffset){
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, drawWidth, drawHeight, null);
        else
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset,drawWidth,drawHeight, null);
        g.setColor(Color.RED);
        if (lastDamageTime != null && Instant.now().minusMillis(1000).isBefore(lastDamageTime) && damageTaken != 0){
            long elapsedTime = Instant.now().toEpochMilli() - lastDamageTime.toEpochMilli();
            int alpha = 255 - (int) ((elapsedTime / 500.0) * 255);
            if (alpha < 0) alpha = 0;
            g.setColor(new Color(255, 0, 0, alpha));
            g.drawString(String.valueOf(damageTaken), (int) hitbox.x - xLevelOffset + xDeltaRandom, (int) hitbox.y - yLevelOffset + yDeltaRandom);
        }
        healthBar.draw(g, xLevelOffset, yLevelOffset);
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
        reward += damage;
        setState(hitState);
        updateHealth(-damage);
        damageTaken = damage;
        lastDamageTime = Instant.now();
        xDeltaRandom = rand.nextInt(-50,100);
        yDeltaRandom = rand.nextInt(0, 50);
    }

    public void updateHealth(int damage) {
        currentHealth += damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            setState(deadState);
            reward += 100;
        }
    }

    public void updateAttackBox(){
        if (isRight) {
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        } else {
            attackBox.x = hitbox.x - hitbox.width - attackBoxChange;
            attackBox.y = hitbox.y;
        }
    }

    public void updateDir(Game game) {
        if (game.getPlayer().getHitbox().x < hitbox.x) {
            isLeft = true;
            isRight = false;
        } else {
            isRight = true;
            isLeft = false;
        }
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
        hitbox = new Rectangle2D.Float(xPos, yPos, hitboxWidth, hitBoxHeight);
        attackBox = new Rectangle2D.Float(xPos, yPos, attackBoxWidth, attackBoxHeight);
        drawIndex = 0;
        currentHealth = maxHealth;
    }
    protected void setState(int state){
        drawIndex = 0;
        this.state = state;
    }

}
