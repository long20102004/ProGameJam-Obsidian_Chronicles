package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Guardian extends Enemy {
    public static final int damage = 5;
    private final Random rnd = new Random();
    private final int attackType = rnd.nextInt(Constant.GUARDIAN.LASER, Constant.GUARDIAN.AOE + 1);
    private boolean isEvil = true;

    public Guardian(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        aniSpeed = 20;
        attackSight = (int) (2 * Game.TILE_SIZE);
        sight = 15 * attackSight;
        xDrawOffset = 180;
        yDrawOffset = (int) (42 * Game.MODE);

        maxHealth = 400;
        currentHealth = maxHealth;
        animation = new BufferedImage[7][24];
        revAnimation = new BufferedImage[7][24];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.GUARDIAN.DEFAULT_WIDTH / 1.5f, Constant.GUARDIAN.DEFAULT_HEIGHT);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.GUARDIAN.DEFAULT_WIDTH * 1.2f, Constant.GUARDIAN.HEIGHT * 1.5f);
        isFly = true;
        BufferedImage tmp = LoadSave.getImg(LoadSave.GUARDIAN);
        for (int i = 0; i < animation.length; i++) {
            for (int j = 0; j < animation[0].length; j++) {
                animation[i][j] = tmp.getSubimage(j * Constant.GUARDIAN.DEFAULT_WIDTH, i * Constant.GUARDIAN.DEFAULT_HEIGHT, Constant.GUARDIAN.DEFAULT_WIDTH, Constant.GUARDIAN.DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
        speed = 1f;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (isRight)
            g.drawImage((Image) animation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - hitbox.width / 2f), (int) hitbox.y - yLevelOffset - yDrawOffset, (int) (Constant.GUARDIAN.WIDTH), (int) (Constant.GUARDIAN.HEIGHT), null);
        else
            g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - xDrawOffset ), (int) hitbox.y - yLevelOffset - yDrawOffset, (int) (Constant.GUARDIAN.WIDTH), (int) (Constant.GUARDIAN.HEIGHT), null);
        g.setColor(Color.RED);
        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);

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
            case Constant.GUARDIAN.IDLE -> handleIdleState();
            case Constant.GUARDIAN.LASER, Constant.GUARDIAN.AOE -> handleAttackState(game);
            case Constant.GUARDIAN.DEAD -> handleDeathState(game);
        }
    }

    private void handleIdleState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, (int) attackBox.width, isFly))  setState(attackType);
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) move();
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex >= Constant.GUARDIAN.getType(attackType) - 1) {
            enemyAttack(game, Dagger.damage, 0);
            isAttacked = true;
            setState(Constant.GUARDIAN.IDLE);
        }
    }

    private void handleDeathState(Game game) {
        if (drawIndex >= Constant.GUARDIAN.getType(Constant.GUARDIAN.DEAD) - 1) {
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
        if (isAttacking) state = rnd.nextInt(Constant.GUARDIAN.LASER, Constant.GUARDIAN.AOE + 1);
        if (isDead) state = Constant.GUARDIAN.DEAD;
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.GUARDIAN.getType(state)) {
                drawIndex = 0;
            }
        }
    }

    @Override
    public void hurt(int damage) {
        updateHealth(-damage);
        Game.reward += damage;
    }

    public void updateHealth(int damage) {
        currentHealth += damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            drawIndex = 0;
            setState(Constant.GUARDIAN.DEAD);
            Game.reward += 100;
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == Constant.GUARDIAN.DEAD) return;
        if ( state == Constant.GUARDIAN.LASER || state == Constant.GUARDIAN.AOE) drawIndex = 0;
        this.state = state;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        isEvil = true;
        isActive = true;
        state = Constant.GUARDIAN.IDLE;
        currentHealth = maxHealth = 400;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, Constant.GUARDIAN.DEFAULT_WIDTH / 2.5f, Constant.GUARDIAN.DEFAULT_HEIGHT);
        attackBox = new Rectangle2D.Float(xPos, yPos, Constant.GUARDIAN.DEFAULT_WIDTH * 2, Constant.GUARDIAN.DEFAULT_HEIGHT);
    }

}
