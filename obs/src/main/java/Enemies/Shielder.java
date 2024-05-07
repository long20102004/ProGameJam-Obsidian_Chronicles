package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Shielder extends Enemy {
    public static int damage = 20;

    public Shielder(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        initClass();
    }
    private void initClass() {
        aniSpeed = 40;
        attackSight = Game.TILE_SIZE;
        sight = 30 * attackSight;
        xDrawOffset = (int) (25 * Game.MODE);
        yDrawOffset = (int) (10 * Game.MODE);

        animation = new BufferedImage[6][17];
        revAnimation = new BufferedImage[6][17];
        currentHealth = maxHealth = 400;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.SHIELDER.DEFAULT_WIDTH - 20 * Game.MODE, Constant.SHIELDER.DEFAULT_HEIGHT + 15 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.SHIELDER.DEFAULT_WIDTH, Constant.SHIELDER.HEIGHT);
        BufferedImage tmp = LoadSave.getImg(LoadSave.SHIELDER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * Constant.SHIELDER.DEFAULT_WIDTH, i * Constant.SHIELDER.DEFAULT_HEIGHT, Constant.SHIELDER.DEFAULT_WIDTH, Constant.SHIELDER.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) hitbox.x - xLevelOffset - 2 * xDrawOffset, (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.SHIELDER.WIDTH, Constant.SHIELDER.HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - 1.5 * xDrawOffset), (int) hitbox.y - yLevelOffset - yDrawOffset, Constant.SHIELDER.WIDTH, Constant.SHIELDER.HEIGHT, null);
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
            setState(Constant.SHIELDER.DEAD);
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
            case Constant.SHIELDER.IDLE -> handleIdleState();
            case Constant.SHIELDER.WALK -> handleWalkState();
            case Constant.SHIELDER.HIT -> handleHitState();
            case Constant.SHIELDER.ATTACK -> handleAttackState(game);
            case Constant.SHIELDER.SHOT -> handleShotState(game);
            case Constant.SHIELDER.DEAD -> handleDeadState();
        }
    }

    private void handleShotState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == Constant.SHIELDER.getType(Constant.SHIELDER.ATTACK) / 2) {
            enemyAttack(game, Dagger.damage, Game.TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= Constant.SHIELDER.getType(state) - 1) setState(Constant.SHIELDER.IDLE);
    }

    private void handleIdleState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.SHIELDER.WALK);
//        if (ExtraMethods.ableToDo(hitbox, shotSight, isFly)) setState(SHOT);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.SHIELDER.ATTACK);
    }

    private void handleWalkState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.SHIELDER.IDLE);
        move();
//        if (ExtraMethods.ableToDo(hitbox, shotSight, isFly)) setState(SHOT);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.SHIELDER.ATTACK);
    }

    private void handleHitState() {
        if (drawIndex == Constant.SHIELDER.getType(Constant.SHIELDER.HIT) - 1) setState(Constant.SHIELDER.IDLE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == Constant.SHIELDER.getType(Constant.SHIELDER.ATTACK) / 2) {
            enemyAttack(game, Shielder.damage * 2, Game.TILE_SIZE);
            isAttacked = true;
        }
        if (drawIndex >= Constant.SHIELDER.getType(state) - 1) setState(Constant.SHIELDER.IDLE);
    }

    private void handleDeadState() {
        if (drawIndex == Constant.SHIELDER.getType(Constant.SHIELDER.DEAD) - 1) isActive = false;
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
            if (drawIndex >= Constant.SHIELDER.getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == Constant.SHIELDER.DEAD) return;
        if (state != this.state) drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isActive = true;
        state = Constant.SHIELDER.IDLE;
        currentHealth = maxHealth = 400;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.SHIELDER.DEFAULT_WIDTH - 20 * Game.MODE, Constant.SHIELDER.DEFAULT_HEIGHT + 15 * Game.MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.SHIELDER.WIDTH / 1.5f, Constant.SHIELDER.HEIGHT / 1.5f);
    }

    @Override
    public void hurt(int damage) {
        setState(Constant.SHIELDER.HIT);
        updateHealth(-damage);
    }
}
