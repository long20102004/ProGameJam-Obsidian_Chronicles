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
        setRawImage(LoadSave.getImg(LoadSave.GUARDIAN));
        setAniSpeed(20);
        setAttackSight((int) (2 * Game.TILE_SIZE));
        setSight(15 * attackSight);
        setXDrawOffset(180);
        setYDrawOffset((int) (42 * Game.MODE));

        setMaxHealth(400);
        setCurrentHealth(maxHealth);
        setImageWidth(24);
        setImageHeight(7);
        setHitboxWidth((int) (Constant.GUARDIAN.DEFAULT_WIDTH / 1.5f));
        setHitBoxHeight(Constant.GUARDIAN.DEFAULT_HEIGHT);
        setAttackBoxWidth((int) (Constant.GUARDIAN.DEFAULT_WIDTH * 1.2f));
        setAttackBoxHeight((int) (Constant.GUARDIAN.HEIGHT * 1.5f));
        setFly(true);
        setAttackBoxChange(50 * Game.MODE);
        setSpeed(1f);
        setDefaultState(Constant.GUARDIAN.IDLE);
        setAttackState(Constant.GUARDIAN.LASER);
        setDeadState(Constant.GUARDIAN.DEAD);
        setDrawWidth(Constant.GUARDIAN.WIDTH);
        setDrawHeight(Constant.GUARDIAN.HEIGHT);
        setTileWidth(Constant.GUARDIAN.DEFAULT_WIDTH);
        setTileHeight(Constant.GUARDIAN.DEFAULT_HEIGHT);
    }


    public void update(Game game) {
        updateAniTick();
        updateStatus();
        updatePos(game);
        updateAttackBox();
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
    public void setState(int state) {
        if (this.state == Constant.GUARDIAN.DEAD) return;
        if ( state == Constant.GUARDIAN.LASER || state == Constant.GUARDIAN.AOE) drawIndex = 0;
        this.state = state;
    }


}
