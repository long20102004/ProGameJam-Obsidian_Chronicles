package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class SwordMaster extends Entity{
    public SwordMaster(String playerPicture, float width, float height, float defaultWidth, float defaultHeight, Body body, int numberAllActionInSheet) {
        super(Entity.ENEMY, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setMovingAction(Constant.SWORD_MASTER.MOVING);
        setAttackingAction(Constant.SWORD_MASTER.ATTACK);
        setDead(Constant.SWORD_MASTER.DEAD);
        setHit(Constant.SWORD_MASTER.IDLE);
        setIdle(Constant.SWORD_MASTER.IDLE);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.GHOUL.getType(i);
        }
        yDrawOffset = (int) (height / 10f);
        initAnimation(playerPicture);
    }
}
