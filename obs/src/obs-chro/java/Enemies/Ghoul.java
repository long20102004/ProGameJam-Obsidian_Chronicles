package Enemies;

import Main.Game;
import UI.EnemyHealthBar;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.GHOUL.*;

public class Ghoul extends Enemy {
    private BufferedImage[][] animation;
    private BufferedImage[][] revAnimation;
    private int drawIndex = 0;
    private int aniTick = 0;
    private final int aniSpeed = 40;
    private final int attackSight = (int) (TILE_SIZE * 1.5);
    private final int sight = 15 * attackSight;
    private final int xDrawOffset = (int) (25 * MODE);
    private final int yDrawOffset = (int) (10 * MODE);
    public static int damage = 10;

    public Ghoul(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        animation = new BufferedImage[7][11];
        revAnimation = new BufferedImage[7][11];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
        BufferedImage tmp = LoadSave.getImg(LoadSave.GHOUL);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * DEFAULT_WIDTH, i * DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        healthBar = new EnemyHealthBar(this, (int) (0.5 * WIDTH), WIDTH / 15);
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        g.setColor(Color.RED);
        healthBar.draw(g, xLevelOffset, yLevelOffset);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
//        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
    }

    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
        healthBar.update();
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
            attackBox.x = hitbox.x - hitbox.width - 10 * MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case WAKE -> handleWakeState();
            case MOVING -> handleMovingState();
            case HIT -> handleHitState();
            case ATTACK -> handleAttackState(game);
            case DEAD -> handleDeadState();
            case REVIVE -> handleReviveState();
        }
    }

    private void handleWakeState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(MOVING);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleMovingState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(WAKE);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleHitState() {
        if (drawIndex == getType(HIT)) setState(WAKE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == getType(ATTACK) / 2) {
            enemyAttack(game, Ghoul.damage, TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= getType(ATTACK) - 1) setState(WAKE);
    }

    private void handleDeadState() {
        if (drawIndex == getType(DEAD) - 1) isActive = false;
    }

    private void handleReviveState() {
        isActive = true;
        if (drawIndex == getType(REVIVE) - 1) setState(WAKE);
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
            if (drawIndex > getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == DEAD) return;
        drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isActive = true;
        state = WAKE;
        currentHealth = maxHealth = 100;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
    }

    @Override
    public void hurt(int damage) {
        setState(HIT);
        updateHealth(-damage);
    }

    public void revive() {
        resetAll();
        setState(REVIVE);
    }
}
