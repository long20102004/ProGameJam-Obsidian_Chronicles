package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class Hoarder extends Entity{
    public Hoarder(String playerPicture, float width, float height, float defaultWidth, float defaultHeight, Body body, int numberAllActionInSheet) {
        super(ENEMY, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setMovingAction(Constant.HOARDER.MOVE);
        setAttackingAction(Constant.HOARDER.MOVE_ATTACK);
        setDead(Constant.HOARDER.DEAD);
        setHit(Constant.HOARDER.HIT);
        setIdle(Constant.HOARDER.IDLE_LOW);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.HOARDER.getType(i);
        }
        yDrawOffset = (int) (height / 5f);
        initAnimation(playerPicture);
    }
}
