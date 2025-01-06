package Player;

import Audio.AudioPlayer;
import Main.Game;
import UI.HealthBar;

import utilz.Constant;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class HoarderTransform extends Player {
    private BufferedImage[] transformation;
    private int transformIndex = 14;
    private final int timeTransform = 5;
    private int transformTick, transformSpeed = timeTransform * 200 / 15;
    public HoarderTransform(int x, int y, Game game) {
        super(x, y, game);
        initClass(x,y);
        xDrawOffset = (int) (50 * Game.MODE);
        yDrawOffset = (int) (50 * Game.MODE);
        isActive = true;
        aniSpeed = 20;
    }

    private void initClass(int xPos, int yPos) {
        initTransform();
        animation = new BufferedImage[13][36];
        revAnimation = new BufferedImage[13][36];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.HOARDER.DEFAULT_WIDTH - 5 * Game.MODE, Constant.HOARDER.DEFAULT_HEIGHT + 15 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.HOARDER.WIDTH / 1.5f, Constant.HOARDER.HEIGHT / 1.5f);
        BufferedImage tmp = LoadSave.getImg(LoadSave.HOARDER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * Constant.HOARDER.DEFAULT_WIDTH, i * Constant.HOARDER.DEFAULT_HEIGHT, Constant.HOARDER.DEFAULT_WIDTH, Constant.HOARDER.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        healthBar = new HealthBar(this, maxHealth, maxPower);
        speed = 2f;
    }

    private void initTransform() {
        transformation = new BufferedImage[15];
        BufferedImage tmp = LoadSave.getImg(LoadSave.POWER_BAR);
        for (int i=0; i<transformation.length; i++){
            transformation[i] = tmp.getSubimage(i * Constant.POWER_BAR.DEFAULT_WIDTH, 2 * Constant.POWER_BAR.DEFAULT_HEIGHT, Constant.POWER_BAR.DEFAULT_WIDTH, Constant.POWER_BAR.DEFAULT_HEIGHT);
        }
    }

    @Override
    public void draw(Graphics g, float xLevelOffset, float yLevelOffset) {
        drawTransform(g);
        healthBar.draw(g);
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - xDrawOffset), (int) ((int) hitbox.y - yLevelOffset - yDrawOffset), Constant.HOARDER.WIDTH, Constant.HOARDER.HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - xDrawOffset), (int) ((int) hitbox.y - yLevelOffset - yDrawOffset), Constant.HOARDER.WIDTH, Constant.HOARDER.HEIGHT, null);
//        g.setColor(Color.RED);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
//        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
    }

    private void drawTransform(Graphics g) {
        g.drawImage(transformation[transformIndex], 650, 150, Constant.POWER_BAR.WIDTH * 2, Constant.POWER_BAR.HEIGHT, null);
    }
    private void updateTransformTick(){
        transformTick++;
        if (transformTick >= transformSpeed){
            transformIndex--;
            transformTick = 0;
            if (transformIndex < 0) transformIndex = 0;
        }
    }
    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
        updateProperties();
        updateTransformTick();
    }

    public void updateAttackBox() {
        if (isRight) {
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        } else {
            attackBox.x = hitbox.x - hitbox.width + 50 * Game.MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        switch (state) {
            case Constant.HOARDER.IDLE_LOW -> handleIdleState();
            case Constant.HOARDER.MOVE -> handleMovingState();
            case Constant.HOARDER.HIT -> handleHitState();
            case Constant.HOARDER.STATIONARY_ATTACK, Constant.HOARDER.MOVE_ATTACK -> handleAttackState(game);
            case Constant.HOARDER.DEAD -> handleDeadState();
        }
    }

    private void handleIdleState() {
        updateInAir();
        setAction();
    }

    private void handleMovingState() {
        updateInAir();
        if (isAttacking || isDoubleAttack || isTripleAttack) setState(Constant.HOARDER.MOVE_ATTACK);
        if (!isMoving && !inAir) setState(Constant.HOARDER.IDLE_LOW);
    }

    private void handleHitState() {
        if (drawIndex == Constant.HOARDER.getType(Constant.HOARDER.HIT)) setState(Constant.HOARDER.IDLE_LOW);
    }

    private void handleAttackState(Game game) {
        if (isMoving){
            updateXPos(speed);
        }
        isAttacked = false;
        if (readyToAttack) {
            readyToAttack = false;
            isAttacked = true;
            game.getAudioPlayer().playEffectSound(AudioPlayer.rand.nextInt(1,4));
        }
    }

    private void handleDeadState() {
        if (drawIndex == Constant.HOARDER.getType(Constant.HOARDER.DEAD) - 1) isActive = false;
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.HOARDER.getType(state)) {
                drawIndex = 0;
                resetStatus();
                setState(Constant.HOARDER.IDLE_LOW);
            }
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == Constant.HOARDER.DEAD) return;
        drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isActive = true;
        state = Constant.HOARDER.IDLE_LOW;
        currentHealth = maxHealth = 100;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.HOARDER.DEFAULT_WIDTH - 5 * Game.MODE, Constant.HOARDER.DEFAULT_HEIGHT + 15 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.HOARDER.WIDTH / 1.5f, Constant.HOARDER.HEIGHT / 1.5f);
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
            if (ExtraMethods.isMovingPossible(hitbox, hitbox.x, hitbox.y + fallSpeed)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                if (fallSpeed > 0) {
                    hitbox.y = ExtraMethods.updateSpaceBetweenYAndWall(hitbox, fallSpeed);
                }
                setState(Constant.HOARDER.IDLE_LOW);
                resetInAir();
            }
        }
        if (isMoving){
            updateXPos(speed);
        }
    }
    public void updateProperties() {
        healthBar.update();
    }

    @Override
    public void update() {

    }

    @Override
    public void updateXPos(float speed) {
        float xspeed = speed;
        if (isLeft) xspeed *= -1;
        if (ExtraMethods.isMovingPossible(hitbox, hitbox.x + xspeed, hitbox.y)) {
            hitbox.x += xspeed;
        } else hitbox.x = ExtraMethods.updateSpaceBetweenXAndWall(hitbox, xspeed);
    }
    @Override
    public void setAction(){
        if (isHit) setState(Constant.HOARDER.HIT);
        if (isAttacking || isDoubleAttack || isTripleAttack) setState(Constant.HOARDER.STATIONARY_ATTACK);
        if (isMoving) setState(Constant.HOARDER.MOVE);
    }
}
