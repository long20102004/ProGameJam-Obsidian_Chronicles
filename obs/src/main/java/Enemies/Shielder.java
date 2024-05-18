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
        initClass();
        initEnemy(xPos, yPos);
    }
    private void initClass() {
        setRawImage(LoadSave.getImg(LoadSave.SHIELDER));
        setAniSpeed(20);
        setAttackSight(Game.TILE_SIZE * 4);
        setSight(10 * attackSight);
        setXDrawOffset((int) (25 * Game.MODE));
        setYDrawOffset((int) (10 * Game.MODE));
        setImageHeight(6);
        setImageWidth(17);
        setMaxHealth(400);
        setCurrentHealth(maxHealth);
        setHitboxWidth((int) (Constant.SHIELDER.DEFAULT_WIDTH - 20 * Game.MODE));
        setHitBoxHeight((int) (Constant.SHIELDER.DEFAULT_HEIGHT + 15 * Game.MODE));
        setAttackBoxWidth((int) (Constant.SHIELDER.WIDTH));
        setAttackBoxHeight((int) (Constant.SHIELDER.HEIGHT));
        setTileWidth(Constant.SHIELDER.DEFAULT_WIDTH);
        setTileHeight(Constant.SHIELDER.DEFAULT_HEIGHT);
        setHealthBarWidth((int) (0.4 * Constant.SHIELDER.WIDTH));
        setHealthBarHeight(Constant.SHIELDER.WIDTH / 15);
        setSpeed(1f);
        setDrawWidth(Constant.SHIELDER.WIDTH);
        setDrawHeight(Constant.SHIELDER.HEIGHT);
        setAttackBoxChange(hitboxWidth / 2);
        setDeadState(Constant.SHIELDER.DEAD);
        setHitState(Constant.SHIELDER.HIT);
        setAttackState(Constant.SHIELDER.ATTACK);
        setDefaultState(Constant.SHIELDER.IDLE);
        setSpeed(1f);
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


    private void updatePos(Game game) {
        if (isLeft) setAttackSight(Game.TILE_SIZE * 2);
        else setAttackSight(Game.TILE_SIZE * 5);
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
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.SHIELDER.ATTACK);

        move();
//        if (ExtraMethods.ableToDo(hitbox, shotSight, isFly)) setState(SHOT);
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
//
//    @Override
//    public void setState(int state) {
//        if (this.state == Constant.SHIELDER.DEAD) return;
//        if (state != this.state) drawIndex = 0;
//        this.state = state;
//    }

}
