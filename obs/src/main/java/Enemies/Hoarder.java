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
        initEnemy(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        setRawImage(LoadSave.getImg(LoadSave.HOARDER));
        setAniSpeed(40);
        setAttackSight((int) (Game.TILE_SIZE * 2.5));
        setSight(20 * attackSight);
        setXDrawOffset((int) (50 * Game.MODE));
        setYDrawOffset((int) (50 * Game.MODE));
        setImageHeight(13);
        setImageWidth(36);
        setHitboxWidth((int) (Constant.HOARDER.DEFAULT_WIDTH - 5 * Game.MODE));
        setHitBoxHeight((int) (Constant.HOARDER.DEFAULT_HEIGHT + 15 * Game.MODE));
        setAttackBoxWidth((int) (Constant.HOARDER.WIDTH / 1.5f));
        setAttackBoxHeight((int) (Constant.HOARDER.HEIGHT / 1.5f));
        setDrawWidth(Constant.HOARDER.WIDTH);
        setDrawHeight(Constant.HOARDER.HEIGHT);
        setTileWidth(Constant.HOARDER.DEFAULT_WIDTH);
        setTileHeight(Constant.HOARDER.DEFAULT_HEIGHT);
        setSpeed(1f);
        setAttackBoxChange(-50 * Game.MODE);
    }

    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
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

}

