package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.utilz.Constant;

public class Ghoul extends Entity{
    public Ghoul(String playerPicture, float width, float height, float defaultWidth, float defaultHeight, Body body, int numberAllActionInSheet) {
        super(Entity.ENEMY, playerPicture, width, height, defaultWidth, defaultHeight, body, numberAllActionInSheet);
        setMovingAction(Constant.GHOUL.MOVING);
        setAttackingAction(Constant.GHOUL.ATTACK);
        setDead(Constant.GHOUL.DEAD);
        setHit(Constant.GHOUL.HIT);
        setIdle(Constant.GHOUL.WAKE);
        animationLength = new int[numberAllActionInSheet];
        for (int i=0; i<animationLength.length; i++){
            animationLength[i] = Constant.GHOUL.getType(i);
        }
        yDrawOffset = (int) (height / 16);

        initAnimation(playerPicture);
    }
}
