package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Hoarder extends Enemy {
    public static int damage = 10;

    public Hoarder(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        aniSpeed = 40;
        attackSight = (int) (Game.TILE_SIZE * 2.5);
        sight = 20 * attackSight;
        xDrawOffset = (int) (50 * Game.MODE);
        yDrawOffset = (int) (50 * Game.MODE);
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
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.HOARDER.WIDTH, Constant.HOARDER.HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) hitbox.x - xLevelOffset - xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.HOARDER.WIDTH, Constant.HOARDER.HEIGHT, null);
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
            setState(Constant.HOARDER.DEAD);
        }
    }

    private void updateAttackBox() {
        if (isRight) {
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        } else {
            attackBox.x = hitbox.x - hitbox.width + 50 * Game.MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case Constant.HOARDER.IDLE_LOW -> handleWakeState();
            case Constant.HOARDER.MOVE -> handleMovingState();
            case Constant.HOARDER.HIT -> handleHitState();
            case Constant.HOARDER.STATIONARY_ATTACK, Constant.HOARDER.MOVE_ATTACK -> handleAttackState(game);
            case Constant.HOARDER.DEAD -> handleDeadState();
        }
    }

    private void handleWakeState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.HOARDER.MOVE);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.HOARDER.STATIONARY_ATTACK);
    }

    private void handleMovingState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.HOARDER.IDLE_LOW);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.HOARDER.STATIONARY_ATTACK);
    }

    private void handleHitState() {
        if (drawIndex >= Constant.HOARDER.getType(Constant.HOARDER.HIT)) setState(Constant.HOARDER.IDLE_LOW);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == Constant.HOARDER.getType(Constant.HOARDER.STATIONARY_ATTACK) / 2) {
            enemyAttack(game, Ghoul.damage, Game.TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= Constant.HOARDER.getType(Constant.HOARDER.STATIONARY_ATTACK) - 1) setState(Constant.HOARDER.IDLE_LOW);
    }

    private void handleDeadState() {
        if (drawIndex == Constant.HOARDER.getType(Constant.HOARDER.DEAD) - 1) isActive = false;
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
            if (drawIndex > Constant.HOARDER.getType(state)) {
                drawIndex = 0;
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

    @Override
    public void hurt(int damage) {
        setState(Constant.HOARDER.HIT);
        updateHealth(-damage);
    }
}

