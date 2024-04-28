package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.MODE;
import static Main.Game.TILE_SIZE;
import static utilz.Constant.SWORD_MASTER.*;

public class SwordMaster extends Enemy{
    public static int damage = 10;
    public SwordMaster(int xPos, int yPos) {
        initClass();
        this.xPos = xPos;
        this.yPos = yPos;
        hitbox = new Rectangle2D.Float(xPos, yPos, 30 * MODE, 40 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, 60 * MODE, 40 * MODE);
    }

    private void initClass() {
        attackSight = (int) (TILE_SIZE * 1.5);
        sight = 15 * attackSight;
        xDrawOffset = (int) (25 * MODE);
        yDrawOffset = (int) (10 * MODE);
        aniSpeed = 40;
        animation = new BufferedImage[4][24];
        revAnimation = new BufferedImage[4][24];
        BufferedImage tmp = LoadSave.getImg(LoadSave.SWORD_MASTER);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 24; j++) {
                animation[i][j] = tmp.getSubimage(DEFAULT_WIDTH * j, DEFAULT_HEIGHT * i, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                revAnimation[i][j] = ExtraMethods.reverseImg(animation[i][j]);
            }
        }
    }

    public void draw(Graphics g, float xLevelOffset, float yLevelOffset) {
        if (isLeft)
            g.drawImage(revAnimation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset - 60), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), WIDTH, HEIGHT, null);
        else
            g.drawImage(animation[state][drawIndex], (int) ((int) hitbox.x - xDrawOffset - xLevelOffset), (int) ((int) hitbox.y - yDrawOffset - yLevelOffset), WIDTH, HEIGHT, null);
        g.setColor(Color.RED);
        g.drawRect((int) ((int) hitbox.x - xLevelOffset), (int) ((int) hitbox.y - yLevelOffset), (int) hitbox.width, (int) hitbox.height);
//        g.drawRect((int) ((int) attackBox.x - xLevelOffset), (int) ((int) attackBox.y - yLevelOffset), (int) attackBox.width, (int) attackBox.height);
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
        if (isRight){
            attackBox.x = hitbox.x;
            attackBox.y = hitbox.y;
        }
        else{
            attackBox.x = hitbox.x - hitbox.width - 10 * MODE;
            attackBox.y = hitbox.y;
        }
    }
    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case IDLE -> handleIdleState();
            case MOVING -> handleMovingState();
            case ATTACK -> handleAttackState(game);
            case DEAD -> handleDeadState();
        }
    }

    private void handleIdleState() {
        updateInAir();
        if (ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(MOVING);
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleMovingState() {
        if (!ExtraMethods.ableToDo(hitbox, sight, isFly)) setState(IDLE);
        move();
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(ATTACK);
    }

    private void handleAttackState(Game game) {
        if (drawIndex == 0) isAttacked = false;
        if (!isAttacked && drawIndex == getType(ATTACK) / 2){
            enemyAttack(game, Ghoul.damage, TILE_SIZE);
            isAttacked = true;
            setState(IDLE);
        }
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
    public void resetAll(){
        super.resetAll();
        isActive = true;
        state = IDLE;
        currentHealth = maxHealth = 100;
        drawIndex = 0;
        hitbox = new Rectangle2D.Float(xPos, yPos, DEFAULT_WIDTH - 5 * MODE, DEFAULT_HEIGHT + 15 * MODE);
        attackBox = new Rectangle2D.Float(xPos, yPos, WIDTH / 1.5f, HEIGHT / 1.5f);
    }
    @Override
    public void hurt(int damage){
        updateHealth(-damage);
    }

}
