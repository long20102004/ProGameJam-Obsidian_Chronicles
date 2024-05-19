package Player;

import AnimatedObjects.Light;
import Audio.AudioPlayer;
import Main.Game;
import UI.HealthBar;
import Weapon.Gun;
import utilz.Constant;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Gunslinger extends Player{
    public Gunslinger(int x, int y, Game game) {
        super(x, y, game);
        this.game = game;
        initPlayer();
    }
    public void initPlayer() {
        hitbox = new Rectangle2D.Float(xPos, yPos, 15 * Game.MODE, 40 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, 60 * Game.MODE, 40 * Game.MODE);
        gun = new Gun(this);
        animation = new BufferedImage[13][9];
        revAnimation = new BufferedImage[13][9];
        healthBar = new HealthBar(this, maxHealth, maxPower);
        BufferedImage tmp = LoadSave.getImg(LoadSave.GUNSLINGER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(Constant.PLAYER.GUNSLINGER.DEFAULT_WIDTH * j, Constant.PLAYER.GUNSLINGER.DEFAULT_HEIGHT * i, Constant.PLAYER.GUNSLINGER.DEFAULT_WIDTH, Constant.PLAYER.GUNSLINGER.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        light = new Light(this);
        yDrawOffset = (int) (3 * Game.MODE);
    }
    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.PLAYER.GUNSLINGER.getType(state)) {
                drawIndex = 0;
                resetStatus();
                setState(Constant.PLAYER.GUNSLINGER.IDLE);
            }
        }
    }
    @Override
    public void update(Game game) {
        updateAniTick();
        light.update();
        updatePosition();
        gun.update(game);
        updateAttackBox();
        updateProperties();
    }
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset){
        healthBar.draw(g);
        light.draw(g, xLevelOffset, yLevelOffset);
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), Constant.PLAYER.GUNSLINGER.WIDTH, Constant.PLAYER.GUNSLINGER.HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), Constant.PLAYER.GUNSLINGER.WIDTH, Constant.PLAYER.GUNSLINGER.HEIGHT, null);
        gun.draw(g, xLevelOffset, yLevelOffset);
        g.setColor(Color.RED);
        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
    }
    public void updateInAir() {
        if (ExtraMethods.isEntityOnFloor(hitbox))  resetInAir();
        else  {
                inAir = true;
        }
        if (isJump) {
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + jumpSpeed)) {
                hitbox.y += jumpSpeed;
                jumpSpeed += gravity;
            }
        }
        if (inAir) {
            if (!isJump && !ExtraMethods.isEntityOnWall(hitbox, isRight)) setState(Constant.PLAYER.GUNSLINGER.FALL);
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + fallSpeed)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                if (fallSpeed > 0) {
                    hitbox.y = ExtraMethods.updateSpaceBetweenYAndWall(hitbox, fallSpeed);
                }
                setState(Constant.PLAYER.GUNSLINGER.IDLE);
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
        setState(Constant.PLAYER.GUNSLINGER.IDLE);
    }
    @Override
    public void setAction() {
        if (isRoll) setState(Constant.PLAYER.GUNSLINGER.ROLL);
        if (isHit) setState(Constant.PLAYER.GUNSLINGER.HIT);
        if (isFall) setState(Constant.PLAYER.GUNSLINGER.FALL);
        if (isAttacking) setState(Constant.PLAYER.GUNSLINGER.SHOOT_1);
        if (isDoubleAttack) setState(Constant.PLAYER.GUNSLINGER.SHOOT_2);

        if (isLedgeGrab && ExtraMethods.isEntityOnWall(hitbox, isRight)) {
            setState(Constant.PLAYER.GUNSLINGER.LEDGE_GRAB);
        }
        if (isMoving) setState(Constant.PLAYER.GUNSLINGER.RUNNING);
        if (isJump) setState(Constant.PLAYER.GUNSLINGER.JUMP);
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
            case Constant.PLAYER.GUNSLINGER.IDLE -> handleIdleState();
            case Constant.PLAYER.GUNSLINGER.JUMP -> handleJumpState();
            case Constant.PLAYER.GUNSLINGER.RUNNING -> handleMovementState();
            case Constant.PLAYER.GUNSLINGER.SHOOT_1, Constant.PLAYER.GUNSLINGER.SHOOT_2 -> handleAttackState();
            case Constant.PLAYER.GUNSLINGER.ROLL -> handleRollState();
            case Constant.PLAYER.GUNSLINGER.LEDGE_GRAB -> handleLedgeGrabState();
            case Constant.PLAYER.GUNSLINGER.FALL -> handleFallState();
            case Constant.PLAYER.GUNSLINGER.HIT -> handleHitState();
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
            setState(Constant.PLAYER.GUNSLINGER.IDLE);
        }
        setAction();
    }
    public void setState(int state) {
        if (state != Constant.PLAYER.GUNSLINGER.RUNNING && state != this.state) drawIndex = 0;
        this.state = state;
    }

    private void handleMovementState() {
        updateInAir();
        if (isAttacking) handleAttackState();
        if (!isMoving && !inAir) setState(Constant.PLAYER.GUNSLINGER.IDLE);
    }

    private void handleAttackState() {
        isAttacked = false;
        if (readyToAttack) {
            readyToAttack = false;
            isAttacked = true;
            game.getAudioPlayer().playEffectSound(AudioPlayer.SHOT);
        }
    }

    private void handleRollState() {
        // Handle roll state here
    }

    private void handleLedgeGrabState() {
        updateWallJump();
        game.getAudioPlayer().playEffectSound(AudioPlayer.CLIMB);
        setAction();
    }
    private void handleFallState() {
        updateInAir();
//        if (isDash) setState(DASH);
    }

    private void handleHitState() {
        if (drawIndex >= Constant.PLAYER.GUNSLINGER.getType(Constant.PLAYER.GUNSLINGER.HIT)) setState(Constant.PLAYER.GUNSLINGER.IDLE);
    }
}
