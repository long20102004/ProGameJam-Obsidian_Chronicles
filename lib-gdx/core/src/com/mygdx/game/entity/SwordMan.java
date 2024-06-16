package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class SwordMan extends Entity {
    public SwordMan(String playerPicture, float width, float height, float defaultWidth, float defaultHeight,
                    Body body, int numberAllActionInSheet) {
        super(Entity.HERO, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setDamage(20);
        setIdle(Constant.PLAYER.SWORD_HERO.IDLE);
        setAttackingAction(Constant.PLAYER.SWORD_HERO.SLAM_ATTACK);
        setHit(Constant.PLAYER.SWORD_HERO.HIT);
        setJumping(Constant.PLAYER.SWORD_HERO.JUMP);
        setFalling(Constant.PLAYER.SWORD_HERO.FALL);
        setDead(Constant.PLAYER.SWORD_HERO.DEATH);
        setMovingAction(Constant.PLAYER.SWORD_HERO.RUN_FAST);
        setDashing(Constant.PLAYER.SWORD_HERO.DASH);
        yDrawOffset = (int) (height / 12f);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.PLAYER.SWORD_HERO.getType(i);
        }

        initAnimation(playerPicture);
    }
}
