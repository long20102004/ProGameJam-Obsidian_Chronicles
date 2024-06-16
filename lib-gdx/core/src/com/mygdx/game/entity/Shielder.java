package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class Shielder extends Entity{
    public Shielder( String playerPicture, float width, float height, float defaultWidth, float defaultHeight, Body body, int numberAllActionInSheet) {
        super(ENEMY, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setMovingAction(Constant.SHIELDER.WALK);
        setAttackingAction(Constant.SHIELDER.ATTACK);
        setDead(Constant.SHIELDER.DEAD);
        setHit(Constant.SHIELDER.HIT);
        setIdle(Constant.SHIELDER.IDLE);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.SHIELDER.getType(i);
        }
        yDrawOffset = (int) (height / 10f);
        initAnimation(playerPicture);
    }
}
