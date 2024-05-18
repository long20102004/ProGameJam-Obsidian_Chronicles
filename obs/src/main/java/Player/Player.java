package Player;

import AnimatedObjects.Light;
import Main.Game;
import OnlineData.ImageSender;
import UI.HealthBar;
import Weapon.Gun;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;

import static Main.Game.reward;

@Component
public abstract class Player implements PlayerMethods {
    protected BufferedImage[][] animation;
    protected BufferedImage[][] revAnimation;
    protected HealthBar healthBar;
    protected int aniTick, aniSpeed = 10;
    protected int drawIndex;
    protected Light light;
    public static final int damage = 40;
    protected int xDrawOffset = (int) (20 * Game.MODE), yDrawOffset = (int) (-7 * Game.MODE);
    protected boolean readyToAttack = true;
    protected int xPos, yPos;
    protected float speed = 3f;
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
    protected int maxHealth = 100;
    protected int maxPower = 100;
    protected int currentHealth = maxHealth;
    protected int currentPower = maxPower;
    protected int countTalking = 0;
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
        if (healthChange < 0){
            reward += (healthChange / maxHealth);
        }
        isHit = true;
        currentHealth += healthChange;
        currentPower += powerChange;
        if (currentHealth < 0) {
            currentHealth = 0;
            reward -= 100;
//            GameState.gameState = GameState.MENU;
            Game.state = 1;
            ImageSender.sendGameState();
            game.resetAll();
        }
        if (currentHealth > maxHealth) currentHealth = maxHealth;
        if (currentPower < 0) currentPower = 0;
        if (currentPower > maxPower) currentPower = maxPower;
    }

    public Rectangle2D.Float getAttackBox() {
        return attackBox;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isAttacked() {
        return isAttacked;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public void setWalk(boolean walk) {
        isWalk = walk;
    }

    public void setRunFast(boolean runFast) {
        isRunFast = runFast;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public void setRoll(boolean roll) {
        isRoll = roll;
    }

    public void setRollAttack(boolean rollAttack) {
        isRollAttack = rollAttack;
    }

    public void setDoubleAttack(boolean doubleAttack) {
        isDoubleAttack = doubleAttack;
    }

    public void setTripleAttack(boolean tripleAttack) {
        isTripleAttack = tripleAttack;
    }

    public void setSpinAttack(boolean spinAttack) {
        isSpinAttack = spinAttack;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public void setDash(boolean dash) {
        isDash = dash;
    }

    public void setJumpTeleport(boolean jumpTeleport) {
        isJumpTeleport = jumpTeleport;
    }

    public void setFallAttack(boolean fallAttack) {
        isFallAttack = fallAttack;
    }

    public void setLedgeGrab(boolean ledgeGrab) {
        isLedgeGrab = ledgeGrab;
    }

    public void setWallHold(boolean wallHold) {
        isWallHold = wallHold;
    }

    public void setWallSlide(boolean wallSlide) {
        isWallSlide = wallSlide;
    }

    public void setWallSlideStop(boolean wallSlideStop) {
        isWallSlideStop = wallSlideStop;
    }

    public void setFall(boolean fall) {
        isFall = fall;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public void setAttacked(boolean attacked) {
        isAttacked = attacked;
    }

    //    public void setTransform(boolean transform) {
//        isTransform = transform;
//        if (isTransform) setState(TRANSFORM);
//    }
    public int countTalking() {
        return countTalking;
    }

    public void increaseTalking() {
        countTalking++;
    }
    public void setCountTalking(int countTalking){
        this.countTalking = countTalking;
    }

    public Gun getGun() {
        return gun;
    }

    public boolean isRight() {
        return isRight;
    }

    public boolean isLeft() {
        return isLeft;
    }
    public Game getGame(){
        return game;
    }

    public boolean isFly() {
        return isFly;
    }
    public void setFly(boolean fly) {
        isFly = fly;
    }

    public boolean isChangeDir() {
        return changeDir;
    }

    public void setChangeDir(boolean changeDir) {
        this.changeDir = changeDir;
    }
    public void setBuffs(boolean buffs){
        isBuffs = buffs;
    }
    public void setCountAniBuffs(int countAniBuffs){
        this.countAniBuffs = countAniBuffs;
    }
    public boolean isBuffs(){
        return isBuffs;
    }
    public int getCountAniBuffs(){
        return countAniBuffs;
    }

    public BufferedImage[][] getAnimation() {
        return animation;
    }

    public void setAnimation(BufferedImage[][] animation) {
        this.animation = animation;
    }

    public boolean isCastBuff() {
        return castBuff;
    }

    public void setCastBuff(boolean castBuff) {
        this.castBuff = castBuff;
    }

    public boolean isCastShieldBuff() {
        return castShieldBuff;
    }

    public void setCastShieldBuff(boolean castShieldBuff) {
        this.castShieldBuff = castShieldBuff;
    }

    public boolean isLightCutBuff() {
        return lightCutBuff;
    }

    public void setLightCutBuff(boolean lightCutBuff) {
        this.lightCutBuff = lightCutBuff;
    }

    public boolean isHealBuff() {
        return healBuff;
    }

    public void setHealBuff(boolean healBuff) {
        this.healBuff = healBuff;
    }

    public boolean isHolySlashBuff() {
        return holySlashBuff;
    }

    public void setHolySlashBuff(boolean holySlashBuff) {
        this.holySlashBuff = holySlashBuff;
    }

    public boolean isGreatHealBuff() {
        return greatHealBuff;
    }

    public void setGreatHealBuff(boolean greatHealBuff) {
        this.greatHealBuff = greatHealBuff;
    }
}