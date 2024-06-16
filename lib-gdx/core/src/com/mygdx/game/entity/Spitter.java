package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class Spitter extends Entity{
    public Spitter( String playerPicture, float width, float height, float defaultWidth, float defaultHeight, Body body, int numberAllActionInSheet) {
        super(Entity.ENEMY, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setMovingAction(Constant.SPITTER.MOVING);
        setAttackingAction(Constant.SPITTER.ATTACK);
        setDead(Constant.SPITTER.DEAD);
        setHit(Constant.SPITTER.HIT);
        setIdle(Constant.SPITTER.WAKE);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.SPITTER.getType(i);
        }
        yDrawOffset = (int) (height / 15);
        initAnimation(playerPicture);
    }
}
