package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.SHIELDER.*;

public class Shielder extends Enemy {
    public static int damage = 20;

    public Shielder(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        initClass();
    }
    private void initClass() {
        aniSpeed = 40;
        attackSight = TILE_SIZE;
        sight = 30 * attackSight;
        xDrawOffset = (int) (25 * MODE);
        yDrawOffset = (int) (10 * MODE);

        animation = new BufferedImage[6][17];
        revAnimation = new BufferedImage[6][17];
        currentHealth = maxHealth = 400;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 20 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH, HEIGHT);
        BufferedImage tmp = LoadSave.getImg(LoadSave.SHIELDER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * DEFAULT_WIDTH, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - 2 * xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - 1.5 * xDrawOffset), (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect(((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
//        g.drawRect(((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
    }

    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
    }

    public void updateHealth(int damage) {
        currentHealth += damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            setState(DEAD);
        }
    }

    private void updateAttackBox() {
        if (isRight) {
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        } else {
            attackBox.x = hitbox.x - hitbox.width / 2;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case IDLE -> handleIdleState();
            case WALK -> handleWalkState();
            case HIT -> handleHitState();
            case ATTACK -> handleAttackState(game);
            case SHOT -> handleShotState(game);
            case DEAD -> handleDeadState();
        }
    }

    private void handleShotState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == getType(ATTACK) / 2) {
            enemyAttack(game, Dagger.damage, TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= getType(state) - 1) setState(IDLE);
    }

    private void handleIdleState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(WALK);
//        if (ExtraMethods.ableToDo(hitbox, shotSight, isFly)) setState(SHOT);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleWalkState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(IDLE);
        move();
//        if (ExtraMethods.ableToDo(hitbox, shotSight, isFly)) setState(SHOT);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleHitState() {
        if (drawIndex == getType(HIT) - 1) setState(IDLE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == getType(ATTACK) / 2) {
            enemyAttack(game, Shielder.damage * 2, TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= getType(state) - 1) setState(IDLE);
    }

    private void handleDeadState() {
        if (drawIndex == getType(DEAD) - 1) isActive = false;
    }

    private void updateDir(Game game) {
        if (game.getPlayer().getHitbox().x < hitbox.x) {
            isLeft = true;
            isRight = false;
        } else {
            isRight = true;
            isLeft = false;
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == DEAD) return;
        if (state != this.state) drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isActive = true;
        state = IDLE;
        currentHealth = maxHealth = 400;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 20 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
    }

    @Override
    public void hurt(int damage) {
        setState(HIT);
        updateHealth(-damage);
    }
}
