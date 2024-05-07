package Enemies;

import Main.Game;
import utilz.Constant;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Main.Game.reward;

public class Dagger extends Enemy {
    public static final int damage = 5;
    private final Random rnd = new Random();
    private final int attackType = rnd.nextInt(Constant.DAGGER.AIR_ATTACK, Constant.DAGGER.ATTACK + 1);
    private boolean isEvil = true;

    public Dagger(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        aniSpeed = 30;
        attackSight = (int) (3.5 * Game.TILE_SIZE);
        sight = 15 * attackSight;
        xDrawOffset = (int) (110 * Game.MODE);
        yDrawOffset = (int) (42 * Game.MODE);

        maxHealth = 400;
        currentHealth = maxHealth;
        animation = new BufferedImage[8][12];
        revAnimation = new BufferedImage[8][12];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.DAGGER.DEFAULT_WIDTH / 2.5f, Constant.DAGGER.DEFAULT_HEIGHT);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.DAGGER.DEFAULT_WIDTH * 2, Constant.DAGGER.DEFAULT_HEIGHT);
        BufferedImage tmp = LoadSave.getImg(LoadSave.DAGGER);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * Constant.DAGGER.DEFAULT_WIDTH, i * Constant.DAGGER.DEFAULT_HEIGHT, Constant.DAGGER.DEFAULT_WIDTH, Constant.DAGGER.DEFAULT_HEIGHT);
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
            attackBox.x = hitbox.x - hitbox.width - 50 * Game.MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        updateStatus();
        switch (state) {
            case Constant.DAGGER.IDLE -> handleIdleState();
            case Constant.DAGGER.RUN -> handleRunState();
            case Constant.DAGGER.HIT -> handleHitState();
            case Constant.DAGGER.ATTACK, Constant.DAGGER.AIR_ATTACK -> handleAttackState(game);
            case Constant.DAGGER.DEATH -> handleDeathState(game);
        }
    }

    private void handleIdleState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(attackType);
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.DAGGER.RUN);
    }

    private void handleRunState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(Constant.DAGGER.IDLE);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(attackType);
    }

    private void handleHitState() {
        if (drawIndex == Constant.DAGGER.getType(Constant.DAGGER.HIT)) setState(Constant.DAGGER.IDLE);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex >= Constant.DAGGER.getType(attackType) - 1) {
            enemyAttack(game, Dagger.damage, 0);
            isAttacked = true;
            setState(Constant.DAGGER.IDLE);
        }
    }

    private void handleDeathState(Game game) {
        if (drawIndex >= Constant.DAGGER.getType(Constant.DAGGER.DEATH)) {
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
        if (isAttacking) state = rnd.nextInt(Constant.DAGGER.AIR_ATTACK, Constant.DAGGER.ATTACK + 1);
        if (isHit) state = Constant.DAGGER.HIT;
        if (isMoving) state = Constant.DAGGER.RUN;
        if (isDead) state = Constant.DAGGER.DEATH;
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex > Constant.DAGGER.getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void hurt(int damage) {
        reward += damage;
        setState(Constant.DAGGER.HIT);
        updateHealth(-damage);
    }

    public void updateHealth(int damage) {
        currentHealth += damage;
        setState(Constant.DAGGER.HIT);
        if (currentHealth <= 0) {
            currentHealth = 0;
            drawIndex = 0;
            setState(Constant.DAGGER.DEATH);
            reward += 100;
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == Constant.DAGGER.DEATH) return;
        if (state == Constant.DAGGER.ATTACK || state == Constant.DAGGER.AIR_ATTACK) drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isEvil = true;
        isActive = true;
        state = Constant.DAGGER.IDLE;
        currentHealth = maxHealth = 400;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.DAGGER.DEFAULT_WIDTH / 2.5f, Constant.DAGGER.DEFAULT_HEIGHT);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.DAGGER.DEFAULT_WIDTH * 2, Constant.DAGGER.DEFAULT_HEIGHT);
    }

}
