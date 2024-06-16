package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class CageShocker extends Entity{
    public CageShocker( String playerPicture, float width, float height, float defaultWidth, float defaultHeight, Body body, int numberAllActionInSheet) {
        super(ENEMY, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setMovingAction(Constant.CAGE_SHOCKER.MOVING);
        setAttackingAction(Constant.CAGE_SHOCKER.ATTACK);
        setDead(Constant.CAGE_SHOCKER.DEAD);
        setHit(Constant.SHIELDER.IDLE);
        setIdle(Constant.SHIELDER.IDLE);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.CAGE_SHOCKER.getType(i);
        }
        yDrawOffset = (int) (height / 11f);
        initAnimation(playerPicture);
    }
}
