package Player;

import AnimatedObjects.Light;
import Audio.AudioPlayer;
import Main.Game;
import UI.HealthBar;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
public class SwordWoman extends Player{
    public SwordWoman(int x, int y, Game game) {
        super(x, y, game);
        initPlayer();
    }
    public void initPlayer() {
        animation = new BufferedImage[27][10];
        revAnimation = new BufferedImage[27][10];
        hitbox = new Rectangle2D.Float(xPos, yPos, 15 * MODE, 17 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, 60 * MODE, 40 * MODE);
        healthBar = new HealthBar(this, maxHealth, maxPower);
        BufferedImage tmp = LoadSave.getImg(LoadSave.SWORD_WOMAN_3);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(Constant.PLAYER.SWORD_WOMAN.DEFAULT_WIDTH * j, Constant.PLAYER.SWORD_WOMAN.DEFAULT_HEIGHT * i, Constant.PLAYER.SWORD_WOMAN.DEFAULT_WIDTH, Constant.PLAYER.SWORD_WOMAN.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        light = new Light(this);
    }
    // FIX
    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.getType(state)) {
                drawIndex = 0;
                resetStatus();
                setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
            }
        }
    }
    @Override
    public void update() {
        updateAniTick();
        light.update();
        updatePosition();
        updateAttackBox();
        updateProperties();
    }
//    public void changeHealth(int damage, int knockback){
//        setState(HIT);
//        // WTF
//        currentHealth += damage;
//        if (currentHealth <= 0) {
//            setState(DEATH);
//        }
//        if (currentHealth >= maxHealth) {
//            currentHealth = maxHealth;
//        }
//    }
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset){
        healthBar.draw(g);
        light.draw(g, xLevelOffset, yLevelOffset);
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 80), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), Constant.PLAYER.SWORD_WOMAN.WIDTH, Constant.PLAYER.SWORD_WOMAN.HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), Constant.PLAYER.SWORD_WOMAN.WIDTH, Constant.PLAYER.SWORD_WOMAN.HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
    }
    public void updateInAir() {
        if (ExtraMethods.isEntityOnFloor(hitbox))  resetInAir();
//        else  {
//            if (ExtraMethods.isEntityOnWall(hitbox, isRight)) setState(WALL_SLIDE);
//            else inAir = true;
//        }
        if (isJump) {
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + jumpSpeed)) {
                hitbox.y += jumpSpeed;
                jumpSpeed += gravity;
            }
        }
        if (inAir) {
//            if (!isJump && !ExtraMethods.isEntityOnWall(hitbox, isRight)) setState(FALL);
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + fallSpeed)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                if (fallSpeed > 0) {
                    hitbox.y = ExtraMethods.updateSpaceBetweenYAndWall(hitbox, fallSpeed);
                }
                setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
                resetInAir();
            }
        }
        if (isMoving){
            updateXPos(speed);
        }
    }
    @Override
    public void resetAll(){
        super.resetAll();
        setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
    }
    @Override
    public void setAction() {
        if (isBlock) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.BLOCK);
        if (isRun) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.RUN);
//        if (isRunFast) setState(RUN_FAST);
        if (isRoll) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.ROLL);
        if (isDash) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.DASH);
//        if (isHit) setState(HIT);
//        if (isFallAttack) setState(FALL_ATTACK);
//        if (isFall) setState(FALL);
//        if (isJumpTeleport) setState(JUMP_TELEPORT);
//        if (isAttacking) setState(SLASH_1);
//        if (isDoubleAttack) setState(SLASH_2);
//        if (isTripleAttack) setState(SLAM_ATTACK);
//        if (isSpinAttack) setState(SPIN_ATTACK);
        if (isLedgeGrab && ExtraMethods.isEntityOnWall(hitbox, isRight)) {
//            setState(LEDGE_GRAB);
        }
//        if (isMoving) setState(RUN_FAST);
        if (isJump) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.JUMP_AND_FALL);
    }
    public void updateWallSlide() {
        if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + fallSpeedConst)) {
            hitbox.y += fallSpeedConst;
        }
        if (ExtraMethods.isEntityOnWall(hitbox, isRight)) {
            fallSpeed = 0f;
            jumpSpeed = -3f;
        }
    }
    public void updateAttackBox() {
        attackBox.x = hitbox.x;
        attackBox.y = hitbox.y;
        if (isLeft) {
            attackBox.x = hitbox.x - attackBox.width;
        }
    }
    public void updateXPos(float speed) {
        float xspeed = speed;
        if (isLeft) xspeed *= -1;
        if (ExtraMethods.isMovingPossible(hitbox, hitbox.x + xspeed, hitbox.y)) {
            hitbox.x += xspeed;
        } else hitbox.x = ExtraMethods.updateSpaceBetweenXAndWall(hitbox, xspeed);
    }
    public void updatePosition() {
        switch (state) {
            case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE -> handleIdleState();
            case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.JUMP_AND_FALL -> handleJumpState();
            case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.WALK, Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.RUN /*RUN_FAST*/ -> handleMovementState();
//            case SLASH_1, SLASH_2, SLAM_ATTACK -> handleAttackState();
            case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.ROLL -> handleRollState();
            case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.DASH -> handleDashState();
            case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.BLOCK -> handleBlockState();
//            case LEDGE_GRAB -> handleLedgeGrabState();
//            case WALL_SLIDE -> handleWallSlideState();
//            case FALL -> handleFallState();
//            case HIT -> handleHitState();
        }
    }
    public void updateProperties() {
        healthBar.update();
    }
    private void handleIdleState() {
        updateInAir();
        setAction();
    }

    private void handleJumpState() {
        updateInAir();
        game.getAudioPlayer().playEffectSound(AudioPlayer.JUMP);
        if (ExtraMethods.isEntityOnFloor(hitbox)) {
            game.getAudioPlayer().playEffectSound(AudioPlayer.LAND);
            setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
        }
        setAction();
    }
    public void setState(int state) {
//        if (state != RUN_FAST && state != this.state) drawIndex = 0;
//        this.state = state;
    }

    private void handleMovementState() {
        updateInAir();
        if (!isMoving && !inAir) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
    }

    private void handleAttackState() {
        isAttacked = false;
        if (readyToAttack) {
            readyToAttack = false;
            isAttacked = true;
            game.getAudioPlayer().playEffectSound(AudioPlayer.rand.nextInt(1,4));
        }
    }

    private void handleRollState() {
        // Handle roll state here
    }

    private void handleDashState() {
        if (drawIndex >= 2 && readyToDash) {
            ExtraMethods.updateLongMove(hitbox, 200, isRight);
            readyToDash = false;
            game.getAudioPlayer().playEffectSound(AudioPlayer.DASH);
        }
    }

    private void handleBlockState() {
        // Handle block state here
    }

    private void handleLedgeGrabState() {
        updateWallJump();
        game.getAudioPlayer().playEffectSound(AudioPlayer.CLIMB);
        setAction();
    }

    private void handleWallSlideState() {
        updateWallSlide();
        game.getAudioPlayer().playEffectSound(AudioPlayer.WALL_SLIDE);
        setAction();
    }

    private void handleFallState() {
        updateInAir();
        if (isDash) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.DASH);
    }
    public void updateWallJump() {
        if (ExtraMethods.isEntityOnWall(hitbox, isRight)) {
            fallSpeed = 0f;
            jumpSpeed = -5f;
        }
        if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + ledgeSpeed)) {
            hitbox.y += ledgeSpeed;
        }
    }

    private void handleHitState() {
//        if (drawIndex >= getType(HIT)) setState(IDLE);
    }
}
