package Player;

import AnimatedObjects.Light;
import Audio.AudioPlayer;
import Main.Game;
import State.Playing;
import UI.HealthBar;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class SwordHero extends Player{
    private int countAction = 0;
    private int maxAction = 1;
    public SwordHero(int x, int y, Game game) {
        super(x, y, game);
        initPlayer();
    }
    public void initPlayer() {
        animation = new BufferedImage[27][10];
        revAnimation = new BufferedImage[27][10];
        hitbox = new Rectangle2D.Float(xPos, yPos, 15 * Game.MODE, 40 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, 60 * Game.MODE, 40 * Game.MODE);
        healthBar = new HealthBar(this, maxHealth, maxPower);
        BufferedImage tmp = LoadSave.getImg(LoadSave.SWORD_HERO);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(Constant.PLAYER.SWORD_HERO.DEFAULT_WIDTH * j, Constant.PLAYER.SWORD_HERO.DEFAULT_HEIGHT * i, Constant.PLAYER.SWORD_HERO.DEFAULT_WIDTH, Constant.PLAYER.SWORD_HERO.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        light = new Light(this);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                updateHealthAndPower(0, 0, 0);
//            }
//        }, 0, 3000);
    }
    private void updateAniTick() {
        if (isMoving) maxAction = 2;
        else maxAction = 1;
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.PLAYER.SWORD_HERO.getType(state) - 1){
                countAction++;
                if (Playing.isAiMode && countAction >= maxAction){
                    Playing.index++;
                    System.out.println(Playing.index);
                    countAction = 0;
                }
            }
            if (drawIndex >= Constant.PLAYER.SWORD_HERO.getType(state)) {
                drawIndex = 0;
                resetStatus();
                setState(Constant.PLAYER.SWORD_HERO.IDLE);
            }
        }
    }
    @Override
    public void update(Game game) {
        updateAniTick();
        light.update();
        updatePosition();
        updateAttackBox();
        updateProperties();
    }
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset){
        healthBar.draw(g);
        light.draw(g, xLevelOffset, yLevelOffset);
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 80), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), Constant.PLAYER.SWORD_HERO.WIDTH, Constant.PLAYER.SWORD_HERO.HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), Constant.PLAYER.SWORD_HERO.WIDTH, Constant.PLAYER.SWORD_HERO.HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
    }
    public void updateInAir() {
        if (ExtraMethods.isEntityOnFloor(hitbox)) {
            if (inAir) Playing.index++;
            resetInAir();
        }
        else  {
            if (ExtraMethods.isEntityOnWall(hitbox, isRight)) setState(Constant.PLAYER.SWORD_HERO.WALL_SLIDE);
            else inAir = true;
        }
        if (isJump) {
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + jumpSpeed)) {
                hitbox.y += jumpSpeed;
                jumpSpeed += gravity;
            }
        }
        if (inAir) {
            if (!isJump && !ExtraMethods.isEntityOnWall(hitbox, isRight)) setState(Constant.PLAYER.SWORD_HERO.FALL);
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + fallSpeed)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                if (fallSpeed > 0) {
                    hitbox.y = ExtraMethods.updateSpaceBetweenYAndWall(hitbox, fallSpeed);
                }
                setState(Constant.PLAYER.SWORD_HERO.IDLE);
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
        setState(Constant.PLAYER.SWORD_HERO.IDLE);
    }
    @Override
    public void setAction() {
        if (isBlock) setState(Constant.PLAYER.SWORD_HERO.BLOCK);
        if (isRun) setState(Constant.PLAYER.SWORD_HERO.RUN);
        if (isRunFast) setState(Constant.PLAYER.SWORD_HERO.RUN_FAST);
        if (isRoll) setState(Constant.PLAYER.SWORD_HERO.ROLL);
        if (isDash) setState(Constant.PLAYER.SWORD_HERO.DASH);
        if (isHit) setState(Constant.PLAYER.SWORD_HERO.HIT);
        if (isFallAttack) setState(Constant.PLAYER.SWORD_HERO.FALL_ATTACK);
        if (isFall) setState(Constant.PLAYER.SWORD_HERO.FALL);
        if (isJumpTeleport) setState(Constant.PLAYER.SWORD_HERO.JUMP_TELEPORT);
        if (isAttacking) setState(Constant.PLAYER.SWORD_HERO.SLASH_1);
        if (isDoubleAttack) setState(Constant.PLAYER.SWORD_HERO.SLASH_2);
        if (isTripleAttack) setState(Constant.PLAYER.SWORD_HERO.SLAM_ATTACK);
        if (isSpinAttack) setState(Constant.PLAYER.SWORD_HERO.SPIN_ATTACK);
        if (isLedgeGrab && ExtraMethods.isEntityOnWall(hitbox, isRight)) {
            setState(Constant.PLAYER.SWORD_HERO.LEDGE_GRAB);
        }
        if (isMoving) setState(Constant.PLAYER.SWORD_HERO.RUN_FAST);
        if (isJump) setState(Constant.PLAYER.SWORD_HERO.JUMP);
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
            case Constant.PLAYER.SWORD_HERO.IDLE -> handleIdleState();
            case Constant.PLAYER.SWORD_HERO.JUMP -> handleJumpState();
            case Constant.PLAYER.SWORD_HERO.WALK, Constant.PLAYER.SWORD_HERO.RUN, Constant.PLAYER.SWORD_HERO.RUN_FAST -> handleMovementState();
            case Constant.PLAYER.SWORD_HERO.SLASH_1, Constant.PLAYER.SWORD_HERO.SLASH_2, Constant.PLAYER.SWORD_HERO.SLAM_ATTACK -> handleAttackState();
            case Constant.PLAYER.SWORD_HERO.ROLL -> handleRollState();
            case Constant.PLAYER.SWORD_HERO.DASH -> handleDashState();
            case Constant.PLAYER.SWORD_HERO.BLOCK -> handleBlockState();
            case Constant.PLAYER.SWORD_HERO.LEDGE_GRAB -> handleLedgeGrabState();
            case Constant.PLAYER.SWORD_HERO.WALL_SLIDE -> handleWallSlideState();
            case Constant.PLAYER.SWORD_HERO.FALL -> handleFallState();
            case Constant.PLAYER.SWORD_HERO.HIT -> handleHitState();
        }
    }
    public void updateProperties() {
        healthBar.update();
    }

    @Override
    public void update() {

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
            setState(Constant.PLAYER.SWORD_HERO.IDLE);
        }
        setAction();
    }
    public void setState(int state) {
        if (state != Constant.PLAYER.SWORD_HERO.RUN_FAST && state != this.state) drawIndex = 0;
        if (state == Constant.PLAYER.SWORD_HERO.DASH && currentPower <= 30) return;
        this.state = state;
    }

    private void handleMovementState() {
        updateInAir();
        if (!isMoving && !inAir) setState(Constant.PLAYER.SWORD_HERO.IDLE);
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
            currentPower -= 30;
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
        if (isDash) setState(Constant.PLAYER.SWORD_HERO.DASH);
        if (isAttacking || isDoubleAttack || isTripleAttack) setState(AudioPlayer.rand.nextInt(7,10));
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
        if (drawIndex >= Constant.PLAYER.SWORD_HERO.getType(Constant.PLAYER.SWORD_HERO.HIT)) setState(Constant.PLAYER.SWORD_HERO.IDLE);
    }
}
