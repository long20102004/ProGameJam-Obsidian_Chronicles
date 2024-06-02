package Enemies;

import Main.Game;
import Player.Player;
import UI.EnemyHealthBar;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.reward;

public class Ghoul extends Enemy {
    public static int damage = 20;
    public Ghoul(int xPos, int yPos) {
        initClass(xPos, yPos);
        initEnemy(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        setRawImage(LoadSave.getImg(LoadSave.GHOUL));
        setAniSpeed(40);
        setAttackSight((int) (Game.TILE_SIZE * 1.5));
        setSight(15 * attackSight);
        setXDrawOffset((int) (25 * Game.MODE));
        setYDrawOffset((int) (10 * Game.MODE));
        setImageHeight(7);
        setImageWidth(11);
        setHitboxWidth((int) (Constant.GHOUL.DEFAULT_WIDTH - 5 * Game.MODE));
        setHitBoxHeight((int) (Constant.GHOUL.DEFAULT_HEIGHT + 15 * Game.MODE));
        setAttackBoxWidth((int) (Constant.GHOUL.WIDTH / 1.5f));
        setAttackBoxHeight((int) (Constant.GHOUL.HEIGHT / 1.5f));
        setTileWidth(Constant.GHOUL.DEFAULT_WIDTH);
        setTileHeight(Constant.GHOUL.DEFAULT_HEIGHT);
        setHealthBarWidth((int) (0.4 * Constant.GHOUL.WIDTH));
        setHealthBarHeight(Constant.GHOUL.WIDTH / 15);
        setSpeed(1f);
        setDrawWidth(Constant.GHOUL.WIDTH);
        setDrawHeight(Constant.GHOUL.HEIGHT);
        setAttackBoxChange(10 * Game.MODE);
        setDeadState(Constant.GHOUL.DEAD);
        setHitState(Constant.GHOUL.HIT);
        setAttackState(Constant.GHOUL.ATTACK);
        setDefaultState(Constant.GHOUL.WAKE);
    }


    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
        healthBar.update();
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
        if (drawIndex == Constant.GHOUL.getType(Constant.GHOUL.DEAD) - 1) {
            isActive = false;
            Player.coins += 2;
        }

    }

    private void handleReviveState() {
        isActive = true;
        if (drawIndex == Constant.GHOUL.getType(Constant.GHOUL.REVIVE) - 1) setState(Constant.GHOUL.WAKE);
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


    public void revive() {
        resetAll();
        setState(Constant.GHOUL.REVIVE);
    }
}
