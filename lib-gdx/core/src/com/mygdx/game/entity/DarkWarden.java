package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class DarkWarden extends Entity{
    public DarkWarden(String playerPicture, float width, float height, float defaultWidth, float defaultHeight, Body body, int numberAllActionInSheet) {
        super(ENEMY, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setMovingAction(Constant.DARK_WARDEN.MOVING);
        setAttackingAction(Constant.DARK_WARDEN.ATTACK);
        setDead(Constant.DARK_WARDEN.DEAD);
        setHit(Constant.DARK_WARDEN.HIT);
        setIdle(Constant.DARK_WARDEN.IDLE);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.DARK_WARDEN.getType(i);
        }
        yDrawOffset = (int) (height / 16f);
        initAnimation(playerPicture);
    }
}
