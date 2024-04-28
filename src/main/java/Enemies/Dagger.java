package Enemies;

import Main.Game;
import utilz.Constant;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.DAGGER.*;

public class Dagger extends Enemy {
    public static final int damage = 5;
    private final Random rnd = new Random();
    private final int attackType = rnd.nextInt(AIR_ATTACK, ATTACK + 1);
    private boolean isEvil = true;

    public Dagger(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        aniSpeed = 30;
        attackSight = (int) (3.5 * TILE_SIZE);
        sight = 15 * attackSight;
        xDrawOffset = (int) (110 * MODE);
        yDrawOffset = (int) (42 * MODE);

        maxHealth = 400;
        currentHealth = maxHealth;
        animation = new BufferedImage[8][12];
        revAnimation = new BufferedImage[8][12];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH / 2.5f, DEFAULT_HEIGHT);
        attackBox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH * 2, DEFAULT_HEIGHT);
        BufferedImage tmp = LoadSave.getImg(LoadSave.DAGGER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * DEFAULT_WIDTH, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isRight)
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.DAGGER.WIDTH, Constant.DAGGER.HEIGHT, null);
        else
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.DAGGER.WIDTH, Constant.DAGGER.HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
    }

    public void update(Game game) {
        updateAniTick();
        updateStatus();
        updatePos(game);
        updateAttackBox();
    }

    private void updateAttackBox() {
        if (isRight) {
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        } else {
            attackBox.x = hitbox.x - hitbox.width - 50 * MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        updateStatus();
        switch (state) {
            case IDLE -> handleIdleState();
            case RUN -> handleRunState();
            case HIT -> handleHitState();
            case ATTACK, AIR_ATTACK -> handleAttackState(game);
            case DEATH -> handleDeathState(game);
        }
    }

    private void handleIdleState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(attackType);
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(RUN);
    }

    private void handleRunState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(IDLE);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(attackType);
    }

    private void handleHitState() {
        if (drawIndex == getType(HIT)) setState(IDLE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex >= getType(attackType) - 1) {
            enemyAttack(game, Dagger.damage, 0);
            isAttacked = true;
            setState(IDLE);
        }
    }

    private void handleDeathState(Game game) {
        if (drawIndex >= getType(DEATH)) {
            isActive = false;
//            changeTeam(game);
        }
    }


    private void updateDir(Game game) {
        if (hitbox.x > game.getPlayer().getHitbox().x) {
            isLeft = true;
            isRight = false;
        } else {
            isRight = true;
            isLeft = false;
        }
    }

    private void updateStatus() {
        if (isAttacking) state = rnd.nextInt(AIR_ATTACK, ATTACK + 1);
        if (isHit) state = HIT;
        if (isMoving) state = RUN;
        if (isDead) state = DEATH;
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex > getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void hurt(int damage) {
        setState(HIT);
        updateHealth(-damage);
    }

    public void updateHealth(int damage) {
        currentHealth += damage;
        setState(HIT);
        if (currentHealth <= 0) {
            currentHealth = 0;
            drawIndex = 0;
            setState(DEATH);
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == DEATH) return;
        if (state == ATTACK || state == AIR_ATTACK) drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isEvil = true;
        isActive = true;
        state = IDLE;
        currentHealth = maxHealth = 400;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH / 2.5f, DEFAULT_HEIGHT);
        attackBox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH * 2, DEFAULT_HEIGHT);
    }

}
