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
    private BufferedImage[][] animationAttack;
    private BufferedImage[][] revAnimationAttack;
    private BufferedImage[][] animationBuffs;
    private BufferedImage[][] revAnimationBuffs;
    private BufferedImage[][] animationHealing;
    private BufferedImage[][] revAnimationHealing;
    private BufferedImage[][] animationExtension;
    private BufferedImage[][] revAnimationExtension;
    public SwordWoman(int x, int y, Game game) {
        super(x, y, game);
        initPlayer();
    }
    public void initPlayer() {
        animation = new BufferedImage[9][37];
        revAnimation = new BufferedImage[9][37];
        animationAttack = new BufferedImage[5][24];
        revAnimationAttack = new BufferedImage[5][24];
        animationHealing = new BufferedImage[5][25];
        revAnimationHealing = new BufferedImage[5][25];
        animationBuffs = new BufferedImage[19][16];
        revAnimationBuffs = new BufferedImage[19][16];
        animationExtension = new BufferedImage[6][30];
        revAnimationExtension = new BufferedImage[6][30];

        hitbox = new Rectangle2D.Float(xPos, yPos, 20 * MODE, 40 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, 60 * MODE, 40 * MODE);
        healthBar = new HealthBar(this, maxHealth, maxPower);
        // DEFAULT
        BufferedImage tmp = LoadSave.getImg(LoadSave.SWORD_WOMAN_DEFAULT);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(Constant.PLAYER.SWORD_WOMAN.DEFAULT_WIDTH * j, Constant.PLAYER.SWORD_WOMAN.DEFAULT_HEIGHT * i, Constant.PLAYER.SWORD_WOMAN.DEFAULT_WIDTH, Constant.PLAYER.SWORD_WOMAN.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        // ATTACK
        tmp = LoadSave.getImg(LoadSave.SWORD_WOMAN_ATTACK);
        for (int i = 0; i < animationAttack.length; i++) {
            for (int j = 0; j < animationAttack[0].length; j++) {
                animationAttack[i][j] = tmp.getSubimage(Constant.PLAYER.SWORD_WOMAN.ATTACKS.DEFAULT_WIDTH * j, Constant.PLAYER.SWORD_WOMAN.ATTACKS.DEFAULT_HEIGHT * i, Constant.PLAYER.SWORD_WOMAN.ATTACKS.DEFAULT_WIDTH, Constant.PLAYER.SWORD_WOMAN.ATTACKS.DEFAULT_HEIGHT);
                revAnimationAttack[i][j] = ExtraMethods.reverseImg(animationAttack[i][j]);
            }
        }
        // BUFFS
        tmp = LoadSave.getImg(LoadSave.SWORD_WOMAN_BUFFS);
        for (int i = 0; i < animationBuffs.length; i++) {
            for (int j = 0; j < animationBuffs[0].length; j++) {
                animationBuffs[i][j] = tmp.getSubimage(Constant.PLAYER.SWORD_WOMAN.BUFFS.DEFAULT_WIDTH * j, Constant.PLAYER.SWORD_WOMAN.BUFFS.DEFAULT_HEIGHT * i, Constant.PLAYER.SWORD_WOMAN.BUFFS.DEFAULT_WIDTH, Constant.PLAYER.SWORD_WOMAN.BUFFS.DEFAULT_HEIGHT);
                revAnimationBuffs[i][j] = ExtraMethods.reverseImg(animationBuffs[i][j]);
            }
        }
        // HEALING
        tmp = LoadSave.getImg(LoadSave.SWORD_WOMAN_HEALING);
        for (int i = 0; i < animationHealing.length; i++) {
            for (int j = 0; j < animationHealing[0].length; j++) {
                animationHealing[i][j] = tmp.getSubimage(Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.DEFAULT_WIDTH * j, Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.DEFAULT_HEIGHT * i, Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.DEFAULT_WIDTH, Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.DEFAULT_HEIGHT);
                revAnimationHealing[i][j] = ExtraMethods.reverseImg(animationHealing[i][j]);
            }
        }
        // EXTENSION ACTION
        tmp = LoadSave.getImg(LoadSave.SWORD_WOMAN_EXTENSION);
        for (int i = 0; i < animationExtension.length; i++) {
            for (int j = 0; j < animationExtension[0].length; j++) {
                animationExtension[i][j] = tmp.getSubimage(Constant.PLAYER.SWORD_WOMAN.EXTENSION_ACTIONS.DEFAULT_WIDTH * j, Constant.PLAYER.SWORD_WOMAN.EXTENSION_ACTIONS.DEFAULT_HEIGHT * i, Constant.PLAYER.SWORD_WOMAN.EXTENSION_ACTIONS.DEFAULT_WIDTH, Constant.PLAYER.SWORD_WOMAN.EXTENSION_ACTIONS.DEFAULT_HEIGHT);
                revAnimationExtension[i][j] = ExtraMethods.reverseImg(animationExtension[i][j]);
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
            if(isBuffs){
                if(lightCutBuff > 0){
                    if (drawIndex >= Constant.PLAYER.SWORD_WOMAN.BUFFS.getType(state)) {
                        if(lightCutBuff > 2){
                            lightCutBuff = 0;
                            resetStatus();
                            setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
                        }
                        else{
                            lightCutBuff++;
                        }
                        drawIndex = 0;
                    }
                }
            }
            else if(isAttacking){
                if (drawIndex >= Constant.PLAYER.SWORD_WOMAN.ATTACKS.getType(state)) {
                    drawIndex = 0;
                    resetStatus();
                    setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
                }
            }
            else if(isLedgeGrab && ExtraMethods.isEntityOnWall(hitbox, isRight)){
                if (drawIndex >= Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.getType(state)) {
                    if(isLedgeGrab) drawIndex = 8;
                    else{
                        drawIndex = 0;
                        resetStatus();
                        setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
                    }
                }
            }
            else{
                if (drawIndex >= Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.getType(state)) {
                    if(isBlock) drawIndex = 4;
                    else if(isJump) drawIndex = 8;
                    else{
                        drawIndex = 0;
                        resetStatus();
                        setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
                    }
                }
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
        if(isBuffs){
            if (isLeft)
                g.drawImage(revAnimationBuffs[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 300), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset - 65), (int)(Constant.PLAYER.SWORD_WOMAN.BUFFS.WIDTH), (int)(Constant.PLAYER.SWORD_WOMAN.BUFFS.HEIGHT), null);
            else
                g.drawImage(animationBuffs[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 40), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset - 65), (int)(Constant.PLAYER.SWORD_WOMAN.BUFFS.WIDTH), (int)(Constant.PLAYER.SWORD_WOMAN.BUFFS.HEIGHT), null);

        }
        else if(isAttacking){ // isAttacking
            if (isLeft)
                g.drawImage(revAnimationAttack[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 60), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset - 25), (int)(Constant.PLAYER.SWORD_WOMAN.WIDTH * 1.3f), (int)(Constant.PLAYER.SWORD_WOMAN.HEIGHT * 1.3f), null);
            else
                g.drawImage(animationAttack[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset - 25), (int)(Constant.PLAYER.SWORD_WOMAN.WIDTH * 1.3f), (int)(Constant.PLAYER.SWORD_WOMAN.HEIGHT * 1.3f), null);
        }
        else if(isLedgeGrab && ExtraMethods.isEntityOnWall(hitbox, isRight)){
            if (isLeft)
                g.drawImage(revAnimationHealing[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 5), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset - 60), (int)(Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.WIDTH), (int)(Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.HEIGHT), null);
            else
                g.drawImage(animationHealing[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset + 15), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset - 60), (int)(Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.WIDTH), (int)(Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.HEIGHT), null);
        }
        else{
            if (isLeft)
                g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset + 4), Constant.PLAYER.SWORD_WOMAN.WIDTH, Constant.PLAYER.SWORD_WOMAN.HEIGHT, null);
            else
                g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 15), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset + 4), Constant.PLAYER.SWORD_WOMAN.WIDTH, Constant.PLAYER.SWORD_WOMAN.HEIGHT, null);
        }
        g.setColor(Color.RED);
        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
    }
    public void updateInAir() {
        if (ExtraMethods.isEntityOnFloor(hitbox))  resetInAir();
        else  {
            if (ExtraMethods.isEntityOnWall(hitbox, isRight)) setState(Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.GRAB);
            else inAir = true;
        }
        if (isJump) {
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + jumpSpeed)) {
                hitbox.y += jumpSpeed;
                jumpSpeed += gravity;
            }
        }
        if (inAir) {
            if (!isJump && !ExtraMethods.isEntityOnWall(hitbox, isRight)) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.JUMP_AND_FALL);
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
        if (lightCutBuff > 0){
            System.out.println(lightCutBuff);
            if(lightCutBuff == 1) setState(Constant.PLAYER.SWORD_WOMAN.BUFFS.LIGHT_CUT_1);
            else if(lightCutBuff == 2) setState(Constant.PLAYER.SWORD_WOMAN.BUFFS.LIGHT_CUT_2);
            else if(lightCutBuff == 3) setState(Constant.PLAYER.SWORD_WOMAN.BUFFS.LIGHT_CUT_3);
        }
        if (isBlock) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.BLOCK);
        if (isRun) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.RUN);
//        if (isRunFast) setState(RUN_FAST);
        if (isRoll) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.ROLL);
        if (isDash) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.DASH);
//        if (isHit) setState(HIT);
//        if (isFallAttack) setState(FALL_ATTACK);
//        if (isFall) setState(FALL);
//        if (isJumpTeleport) setState(JUMP_TELEPORT);
        if (isAttacking) setState(Constant.PLAYER.SWORD_WOMAN.ATTACKS.FRONT_ATTACK);
        if (isDoubleAttack) setState(Constant.PLAYER.SWORD_WOMAN.ATTACKS.LIGHT_ATTACK);
        if (isTripleAttack) setState(Constant.PLAYER.SWORD_WOMAN.ATTACKS.UP_LIGHT_ATTACK);
//        if (isSpinAttack) setState(SPIN_ATTACK);
        if (isLedgeGrab && ExtraMethods.isEntityOnWall(hitbox, isRight)) {
            setState(Constant.PLAYER.SWORD_WOMAN.HEALING_AND_GRAB.GRAB);
        }
        if (isMoving) setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.WALK);
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
        if(isAttacking){
            handleAttackState();
        }
        else if(isBuffs){
            setAction();
        }
        else if(isLedgeGrab){
//            handleLedgeGrabState();
            handleWallSlideState();
        }
        else{
            switch (state) {
                case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE -> handleIdleState();
                case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.JUMP_AND_FALL -> handleJumpState();
                case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.WALK, Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.RUN /*RUN_FAST*/ -> handleMovementState();
//            case Constant.PLAYER.SWORD_WOMAN.ATTACKS.HEAVY_AIR_ATTACK -> handleAttackState();
                case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.ROLL -> handleRollState();
                case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.DASH -> handleDashState();
                case Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.BLOCK -> handleBlockState();
//            case LEDGE_GRAB -> handleLedgeGrabState();
//            case WALL_SLIDE -> handleWallSlideState();
//            case FALL -> handleFallState();
//            case HIT -> handleHitState();
            }
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
            setState(Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.IDLE);
        }
        setAction();
    }
    public void setState(int state) {
        if (state != Constant.PLAYER.SWORD_WOMAN.DEFAULT_ACTIONS.RUN && state != this.state) drawIndex = 0;
        this.state = state;
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
