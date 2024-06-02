package Player;

import AnimatedObjects.Light;
import Audio.AudioPlayer;
import Main.Game;
import OnlineData.ImageSender;
import State.GameState;
import State.Playing;
import UI.HealthBar;
import Weapon.Gun;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Random;
import java.util.Timer;

import static Main.Game.reward;

@Getter
@Setter
public abstract class Player implements PlayerMethods {
    public static boolean isLocked = false;
    protected BufferedImage[][] animation;
    protected BufferedImage[][] revAnimation;
    protected HealthBar healthBar;
    protected int aniTick, aniSpeed = 10;
    protected int drawIndex;
    protected Light light;
    public static int damage = 40;
    protected int xDrawOffset = (int) (20 * Game.MODE), yDrawOffset = (int) (-7 * Game.MODE);
    protected boolean readyToAttack = true;
    protected int xPos, yPos;
    protected float speed = 1.5f;
    protected float fallSpeed = 0, gravity = 0.04f;
    protected float wallFallSpeed = 0.5f;
    protected float constGravity = 0.03f;
    protected float fallSpeedConst = 0.5f;
    protected float jumpSpeedConst = -5f;
    protected float jumpSpeed = -5f;
    protected float ledgeSpeed = -0.5f;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected boolean isMoving;
    protected boolean isRight;
    protected boolean isLeft;
    protected boolean isAttacking;
    protected boolean isJump;
    protected boolean isHit;
    protected boolean isWalk;
    protected boolean isRunFast;
    protected boolean isRun;
    protected boolean isRoll;
    protected boolean isRollAttack;
    protected boolean isDoubleAttack;
    protected boolean isTripleAttack;
    protected boolean isSpinAttack;
    protected boolean isBlock;
    protected boolean isDash;
    protected boolean isJumpTeleport;
    protected boolean isFallAttack;
    protected boolean isLedgeGrab;
    protected boolean isWallHold;
    protected boolean isWallSlide;
    protected boolean isWallSlideStop;
    protected boolean isFall;
    protected boolean inAir;
    protected boolean isActive = true;
    protected boolean isAttacked;
    // SWORD WOMAN
    protected boolean isBuffs;
    protected int countAniBuffs;
    protected boolean castBuff;
    protected boolean castShieldBuff;
    protected boolean lightCutBuff;
    protected boolean healBuff;
    protected boolean holySlashBuff;
    protected boolean greatHealBuff;

    //

    protected boolean readyToDash = true;
    protected Game game;
    protected int state;
    protected int maxHealth = 500;
    protected int maxPower = 100;
    protected int currentHealth = maxHealth;
    protected int currentPower = maxPower;
    public int countTalking = -1;
    protected Gun gun;

    protected boolean isFly;
    public static final int NOT_CHANGE = 0;
    public static final int SWORD_HERO = 1;
    public static final int GUN_SLINGER = 2;
    public static final int HOARDER = 3;
    public static final int SWORD_WOMAN = 4;
    public static int currentHero = NOT_CHANGE;
    public static int coins;
    protected boolean changeDir;
    Instant lastDamageTime;
    int damageTaken;
    Random random = new Random();
    int xDeltaRandom, yDeltaRandom;
    protected Timer timer = new Timer();
    public Player(int x, int y, Game game) {
        this.game = game;
        this.xPos = x;
        this.yPos = y;
    }
    protected void updateWallJump() {
    }

    public void updateWallSlide() {
    }

    public void updateXPos(float speed) {
    }

    @Override
    public void updateInAir() {
    }


    public void setAction() {

    }

    public void updateAttackBox() {
    }

    public void updateProperties() {
    }

    public abstract void update();

    public void draw(Graphics g, float xLevelOffset, float yLevelOffset) {
        if (lastDamageTime != null && Instant.now().minusMillis(1000).isBefore(lastDamageTime) && damageTaken != 0){
            long elapsedTime = Instant.now().toEpochMilli() - lastDamageTime.toEpochMilli();
            int alpha = 255 - (int) ((elapsedTime / 500.0) * 255);
            if (alpha < 0) alpha = 0;
            if (damageTaken < 0) g.setColor(new Color(255, 255, 255, alpha));
            else g.setColor(new Color(0, 255, 0, alpha));
            g.drawString(String.valueOf(damageTaken), (int) (hitbox.x - xLevelOffset + xDeltaRandom), (int) (hitbox.y - yLevelOffset + yDeltaRandom));
        }
    }


    public void setState(int state) {
    }

    protected void resetInAir() {
        fallSpeedConst = 0.5f;
        fallSpeed = 0f;
        jumpSpeed = -4f;
        inAir = false;
    }

    public void resetAll() {
        isActive = true;
        hitbox = new Rectangle2D.Float(xPos, yPos, 15 * Game.MODE, 40 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, 60 * Game.MODE, 40 * Game.MODE);
        currentHealth = maxHealth;
        currentPower = maxPower;
        isLeft = false;
        isRight = true;
        isMoving = false;
        speed = 1.5f;
        fallSpeed = 0f;
        gravity = 0.04f;
        jumpSpeed = -5f;
        fallSpeedConst = 0.5f;
    }

    protected void resetStatus() {
        isAttacked = isHit = false;
        isAttacking = isDash = isBlock = false;
        isDoubleAttack = isFallAttack = isRollAttack = false;
        isSpinAttack = isTripleAttack = isJumpTeleport = false;
        isJump = isRoll = false;
        readyToDash = true;
        readyToAttack = true;
        aniSpeed = 20;
        isBuffs = false;
        countAniBuffs = 0;
        castBuff = false;
        castShieldBuff = false;
        lightCutBuff = false;
        healBuff = false;
        holySlashBuff = false;
        greatHealBuff = false;

    }
    public void update(Game game){

    }
    public void updateHealthAndPower(int healthChange, int powerChange, int knockBack){
        lastDamageTime = Instant.now();
        damageTaken = healthChange;
        isHit = true;
        currentHealth += healthChange;
        xDeltaRandom = random.nextInt(-50, 50);
        yDeltaRandom = random.nextInt(0, 50);
        currentPower += powerChange;
        if (currentHealth <= 0) {
            game.getAudioPlayer().playEffectSound(AudioPlayer.DEAD);
            currentHealth = 0;
            reward -= 100;
            Game.state = 1;
            GameState.gameState = GameState.MENU;
        }
        if (currentHealth > maxHealth) currentHealth = maxHealth;
        if (currentPower < 0) currentPower = 0;
        if (currentPower > maxPower) currentPower = maxPower;
    }

    //    public void setTransform(boolean transform) {
//        isTransform = transform;
//        if (isTransform) setState(TRANSFORM);
//    }
    public void increaseTalking(){
        this.countTalking++;
    }

    public void setHitboxX(Float x) {
        this.hitbox.x = x;
    }
    public void setHitboxY(Float y) {
        this.hitbox.y = y;
    }
}