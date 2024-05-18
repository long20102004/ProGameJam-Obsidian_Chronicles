package Enemies;

import Level.Level;
import Main.Game;
import utilz.ExtraMethods;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import utilz.Constant;

public class Summoner extends Enemy{

    public Summoner(int xPos, int yPos) {
        initClass(xPos, yPos);
        initEnemy(xPos, yPos);
    }

    private void initClass(int xPos, int yPos) {
        setRawImage(LoadSave.getImg(LoadSave.SUMMONER));
        setAniSpeed(80);
        setSight(Game.TILE_SIZE * 50);
        setAttackSight(Game.TILE_SIZE * 50);
        setXDrawOffset((int) (25 * Game.MODE));
        setYDrawOffset((int) (10 * Game.MODE));
        setImageHeight(5);
        setImageWidth(9);
        setHitboxWidth((int) (Constant.SUMMONER.WIDTH / 2f));
        setHitBoxHeight((int) (Constant.SUMMONER.DEFAULT_HEIGHT * 1.5f));
        setAttackBoxWidth((int) (Constant.SUMMONER.WIDTH * 1.2f));
        setAttackBoxHeight((int) (Constant.SUMMONER.HEIGHT * 1.5f));
        setTileWidth(Constant.SUMMONER.DEFAULT_WIDTH);
        setTileHeight(Constant.SUMMONER.DEFAULT_HEIGHT);
        setHealthBarWidth((int) (0.4 * Constant.SUMMONER.WIDTH));
        setHealthBarHeight((int) (Constant.SUMMONER.WIDTH / 15));
        setSpeed(1f);
        setDrawWidth(Constant.SUMMONER.WIDTH);
        setDrawHeight(Constant.SUMMONER.HEIGHT);
        setDeadState(Constant.SUMMONER.DEAD);
        setHitState(Constant.SUMMONER.HIT);
        setDefaultState(Constant.SUMMONER.IDLE);
        setAttackState(Constant.SUMMONER.SUMMON);
        setAttackBoxChange(10 * Game.MODE);
    }



    public void update(Game game) {
        updateAniTick();
        updatePos(game);
        updateAttackBox();
    }
    private void updatePos(Game game) {
        updateDir(game);
        switch (state) {
            case Constant.SUMMONER.IDLE -> handleIdleState();
            case Constant.SUMMONER.HIT -> handleHitState(game);
            case Constant.SUMMONER.SUMMON -> handleSummonState(game);
            case Constant.SUMMONER.DEAD -> handleDeadState();
        }
    }

    private void handleIdleState(){
        aniSpeed = 40;
        if (ExtraMethods.ableToDo(hitbox, attackSight, isFly)) setState(Constant.SUMMONER.SUMMON);
    }
    private void handleHitState(Game game){
        aniSpeed = 20;
        if (!isAttacked && drawIndex == Constant.SUMMONER.getType(Constant.SUMMONER.SUMMON) / 2){
            summonGhoul(game.getLevelManager().getLevel());
            isAttacked = true;
            setState(Constant.SUMMONER.IDLE);
        }
        if (drawIndex == Constant.SUMMONER.getType(Constant.SUMMONER.HIT) - 2) {
            setState(Constant.SUMMONER.IDLE);
        }
    }
    private void handleSummonState(Game game){
        if (drawIndex == 0){
            isAttacked = false;
        }
        if (!isAttacked && drawIndex == Constant.SUMMONER.getType(Constant.SUMMONER.SUMMON) / 2){
            summonGhoul(game.getLevelManager().getLevel());
            isAttacked = true;
            setState(Constant.SUMMONER.IDLE);
        }
    }
    private void handleDeadState(){
        if (drawIndex == Constant.SUMMONER.getType(Constant.SUMMONER.DEAD) - 1) {
            isActive = false;
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick > aniSpeed) {
            aniTick = 0;
            drawIndex++;
            if (drawIndex >= Constant.SUMMONER.getType(state)) {
                drawIndex = 0;
            }
        }
    }

    private void summonGhoul(Level level){
        for (Enemy enemy : level.enemies){
            if (enemy instanceof Ghoul ghoul) {
                if (ghoul.hitbox.y > this.hitbox.y) {
                    if (!ghoul.isActive) {
                        ghoul.revive();
                        break;
                    }
                }
            }
        }
    }
    @Override
    public void setState(int state) {
        if (this.state == Constant.SUMMONER.DEAD) return;
        drawIndex = 0;
        this.state = state;
    }

    @Override
    public void move() {
        float xspeed = speed;
        if (isLeft) xspeed *= -1;
        if (ExtraMethods.isMovingPossible(hitbox, hitbox.x + xspeed, hitbox.y)) {
            hitbox.x += xspeed;
        }
    }
}
