package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.GUARDIAN.*;

public class Guardian extends Enemy {
    private static BufferedImage[][] animation;
    private static BufferedImage[][] revAnimation;
    private int drawIndex = 0;
    private int aniTick = 0;
    private final int aniSpeed = 20;
    private final int attackSight = (int) (2 * TILE_SIZE);
    private final int sight = 15 * attackSight;
    private final int xDrawOffset = 180;
    private final int yDrawOffset = (int) (42 * MODE);
    public static final int damage = 5;
    private final Random rnd = new Random();
    private final int attackType = rnd.nextInt(LASER, AOE + 1);
    private boolean isEvil = true;

    public Guardian(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        maxHealth = 400;
        currentHealth = maxHealth;
        animation = new BufferedImage[7][24];
        revAnimation = new BufferedImage[7][24];
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH / 1.5f, DEFAULT_HEIGHT);
        attackBox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH * 1.2f, HEIGHT * 1.5f);
        isFly = true;
        BufferedImage tmp = LoadSave.getImg(LoadSave.GUARDIAN);
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
            g.drawImage((Image) animation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - hitbox.width / 2f), (int) hitbox.y - yLevelOffset - yDrawOffset, (int) (WIDTH), (int) (HEIGHT), null);
        else
            g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xLevelOffset - xDrawOffset ), (int) hitbox.y - yLevelOffset - yDrawOffset, (int) (WIDTH), (int) (HEIGHT), null);
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
            attackBox.x = hitbox.x - hitbox.width - 50 * MODE;
            attackBox.y = hitbox.y;
        }
    }

    private void updatePos(Game game) {
        updateDir(game);
        updateStatus();
        switch (state) {
            case IDLE -> handleIdleState();
            case LASER, AOE -> handleAttackState(game);
            case DEAD -> handleDeathState(game);
        }
    }

    private void handleIdleState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, (int) attackBox.width, isFly))  setState(attackType);
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) move();
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
        if (drawIndex >= getType(DEAD) - 1) {
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
        if (isAttacking) state = rnd.nextInt(LASER, AOE + 1);
        if (isDead) state = DEAD;
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
    public void hurt(int damage) {
        updateHealth(-damage);
    }

    public void updateHealth(int damage) {
        currentHealth += damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            drawIndex = 0;
            setState(DEAD);
        }
    }

    @Override
    public void setState(int state) {
        if (this.state == DEAD) return;
        if ( state == LASER || state == AOE) drawIndex = 0;
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

    //    public void changeTeam(Game game) {
//        resetAll();
//        isEvil = false;
//    }
    public static BufferedImage[][] getAnimation(){
        return animation;
    }
    public static BufferedImage[][] getRevAnimation(){
        return revAnimation;
    }
}
