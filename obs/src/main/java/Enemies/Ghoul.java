package Enemies;

import Main.Game;
import UI.EnemyHealthBar;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.reward;

public class Ghoul extends Enemy {
    public static int damage = 10;
    public Ghoul(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        aniSpeed = 40;
        attackSight = (int) (Game.TILE_SIZE * 1.5);
        sight = 15 * attackSight;
        xDrawOffset = (int) (25 * Game.MODE);
        yDrawOffset = (int) (10 * Game.MODE);
        animation = new BufferedImage[7][11];
        revAnimation = new BufferedImage[7][11];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.GHOUL.DEFAULT_WIDTH - 5 * Game.MODE, Constant.GHOUL.DEFAULT_HEIGHT + 15 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.GHOUL.WIDTH / 1.5f, Constant.GHOUL.HEIGHT / 1.5f);
        BufferedImage tmp = LoadSave.getImg(LoadSave.GHOUL);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * Constant.GHOUL.DEFAULT_WIDTH, i * Constant.GHOUL.DEFAULT_HEIGHT, Constant.GHOUL.DEFAULT_WIDTH, Constant.GHOUL.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        healthBar = new EnemyHealthBar(this, (int) (0.4 * Constant.GHOUL.WIDTH), Constant.GHOUL.WIDTH / 15);
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.GHOUL.WIDTH, Constant.GHOUL.HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.GHOUL.WIDTH, Constant.GHOUL.HEIGHT, null);
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
            setState(Constant.GHOUL.DEAD);
            reward += 100;
        }
    }

    private void updateAttackBox() {
        if (isRight) {
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        } else {
            attackBox.x = hitbox.x - hitbox.width - 10 * Game.MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case Constant.GHOUL.WAKE -> handleWakeState();
            case Constant.GHOUL.MOVING -> handleMovingState();
            case Constant.GHOUL.HIT -> handleHitState();
            case Constant.GHOUL.ATTACK -> handleAttackState(game);
            case Constant.GHOUL.DEAD -> handleDeadState();
            case Constant.GHOUL.REVIVE -> handleReviveState();
        }
    }

    private void handleWakeState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.GHOUL.MOVING);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.GHOUL.ATTACK);
    }

    private void handleMovingState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.GHOUL.WAKE);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.GHOUL.ATTACK);
    }

    private void handleHitState() {
        if (drawIndex == Constant.GHOUL.getType(Constant.GHOUL.HIT)) setState(Constant.GHOUL.WAKE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == Constant.GHOUL.getType(Constant.GHOUL.ATTACK) / 2) {
            enemyAttack(game, Ghoul.damage, Game.TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= Constant.GHOUL.getType(Constant.GHOUL.ATTACK) - 1) setState(Constant.GHOUL.WAKE);
    }

    private void handleDeadState() {
        if (drawIndex == Constant.GHOUL.getType(Constant.GHOUL.DEAD) - 1) isActive = false;
    }

    private void handleReviveState() {
        isActive = true;
        if (drawIndex == Constant.GHOUL.getType(Constant.GHOUL.REVIVE) - 1) setState(Constant.GHOUL.WAKE);
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
            if (drawIndex > Constant.GHOUL.getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == Constant.GHOUL.DEAD) return;
        drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isActive = true;
        state = Constant.GHOUL.WAKE;
        currentHealth = maxHealth = 100;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.GHOUL.DEFAULT_WIDTH - 5 * Game.MODE, Constant.GHOUL.DEFAULT_HEIGHT + 15 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.GHOUL.WIDTH / 1.5f, Constant.GHOUL.HEIGHT / 1.5f);
    }

    @Override
    public void hurt(int damage) {
        reward += damage;
        setState(Constant.GHOUL.HIT);
        updateHealth(-damage);
    }

    public void revive() {
        resetAll();
        setState(Constant.GHOUL.REVIVE);
    }
}
