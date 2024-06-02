package Enemies;

import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;
import utilz.Constant;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Hive extends Enemy{
    public Hive(int xPos, int yPos) {
        initClass(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        setRawImage(LoadSave.getImg(LoadSave.HIVE));
        setAniSpeed(800);
        setAttackSight(Game.TILE_SIZE * 50);
        setXDrawOffset((int) (25 * Game.MODE));
        setYDrawOffset((int) (20 * Game.MODE));
        setMaxHealth(400);
        setCurrentHealth(maxHealth);
        setImageHeight(4);
        setImageWidth(9);
        setFly(true);
        setHitboxWidth((int) (Constant.HIVE.DEFAULT_WIDTH - 5 * Game.MODE));
        setHitBoxHeight((int) (Constant.HIVE.DEFAULT_HEIGHT + 15 * Game.MODE));
        setAttackBoxWidth((int) (Constant.HIVE.WIDTH / 1.5f));
        setAttackBoxHeight((int) (Constant.HIVE.HEIGHT / 1.5f));
        setAttackBoxChange(10 * Game.MODE);
        setDrawWidth(Constant.HIVE.WIDTH);
        setDrawHeight(Constant.HIVE.HEIGHT);
        setTileWidth(Constant.HIVE.DEFAULT_WIDTH);
        setTileHeight(Constant.HIVE.DEFAULT_HEIGHT);
        setSpeed(1f);
    }


    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
        healthBar.update();
    }

    private void updatePos(Game game) {
        switch (state) {
            case Constant.HIVE.IDLE -> handleIdleState();
            case Constant.HIVE.HIT -> handleHitState(game);
            case Constant.HIVE.PRODUCE -> handleSummonState(game);
            case Constant.HIVE.DEAD -> handleDeadState();
        }
    }

    private void handleIdleState(){
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.HIVE.PRODUCE);
    }
    private void handleHitState(Game game){
        if (drawIndex == Constant.HIVE.getType(Constant.HIVE.HIT) - 1) {
            setState(Constant.HIVE.IDLE);
        }
    }
    private void handleSummonState(Game game){
        if (drawIndex == 0){
            isAttacked = false;
        }
        if (!isAttacked && drawIndex == Constant.HIVE.getType(Constant.HIVE.PRODUCE) / 2){
            summonSpitter(game);
            isAttacked = true;
            setState(Constant.HIVE.IDLE);
        }
    }
    private void handleDeadState(){
        if (drawIndex == Constant.HIVE.getType(Constant.HIVE.DEAD) - 1) {
            isActive = false;
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.HIVE.getType(state)) {
                drawIndex = 0;
            }
        }
    }

    private void summonSpitter(Game game){
        game.getEnemyManager().extraSpitter.add(new Spitter((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height)));
    }
    @Override
    public void setState(int state) {
        if (this.state == Constant.HIVE.DEAD) return;
        drawIndex = 0;
        this.state = state;
    }

}
