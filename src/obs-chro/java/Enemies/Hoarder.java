package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.HOARDER.*;

public class Hoarder extends Enemy {
    private BufferedImage[][] animation;
    private BufferedImage[][] revAnimation;
    private int drawIndex = 0;
    private int aniTick = 0;
    private final int aniSpeed = 40;
    private final int attackSight = (int) (TILE_SIZE * 2.5);
    private final int sight = 20 * attackSight;
    private final int xDrawOffset = (int) (50 * MODE);
    private final int yDrawOffset = (int) (50 * MODE);
    public static int damage = 10;

    public Hoarder(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        animation = new BufferedImage[13][36];
        revAnimation = new BufferedImage[13][36];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
        BufferedImage tmp = LoadSave.getImg(LoadSave.HOARDER);
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
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, WIDTH, HEIGHT, null);
        g.setColor(Color.RED);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
//        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
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
            attackBox.x = hitbox.x - hitbox.width + 50 * MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case IDLE_LOW -> handleWakeState();
            case MOVE -> handleMovingState();
            case HIT -> handleHitState();
            case STATIONARY_ATTACK, MOVE_ATTACK -> handleAttackState(game);
            case DEAD -> handleDeadState();
        }
    }

    private void handleWakeState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(MOVE);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(STATIONARY_ATTACK);
    }

    private void handleMovingState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(IDLE_LOW);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(STATIONARY_ATTACK);
    }

    private void handleHitState() {
        if (drawIndex >= getType(HIT)) setState(IDLE_LOW);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == getType(STATIONARY_ATTACK) / 2) {
            enemyAttack(game, Enemies.Ghoul.damage, TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= getType(STATIONARY_ATTACK) - 1) setState(IDLE_LOW);
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
        state = IDLE_LOW;
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
}

