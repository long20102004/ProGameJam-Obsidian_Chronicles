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
        initEnemy(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        setAniSpeed(30);
        setAttackSight((int) (3.5 * Game.TILE_SIZE));
        setSight(15 * attackSight);
        setXDrawOffset((int) (110 * Game.MODE));
        setYDrawOffset((int) (42 * Game.MODE));
        setMaxHealth(400);
        setCurrentHealth(maxHealth);
        setSpeed(1f);
        setImageHeight(8);
        setImageWidth(12);
        setRawImage(LoadSave.getImg(LoadSave.DAGGER));
        setTileWidth(Constant.DAGGER.DEFAULT_WIDTH);
        setTileHeight(Constant.DAGGER.DEFAULT_HEIGHT);
        setAttackBoxChange(50 * Game.MODE);
    }

    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
    }


    private void updatePos(Game game) {
        updateDir(game);
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
