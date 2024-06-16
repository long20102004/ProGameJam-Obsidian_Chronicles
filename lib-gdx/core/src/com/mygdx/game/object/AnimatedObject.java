package com.mygdx.game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import static com.mygdx.game.utilz.Constant.PPM;

public class AnimatedObject extends Sprite {
    protected float stateTime;
//    public static final int TREE1 = 1;
//    public static final int TREE2 = 2;
//    public static final int BUG = 3;
//    public static final int GREEN_BUG = 4;
//    public static final int FLOWER = 5;
//    public static final int TRAP = 6;
    protected Texture image;
    protected Animation<TextureRegion>[] animations;
    protected int currentKind;
    protected Body body;

    public AnimatedObject(String objectName, int numberOfFrames, int numberOfKinds, int width, int height,
                          int defaultWidth, int defaultHeight, int currentKind, Body body) {
        super(new Texture(Gdx.files.internal(objectName)));
        this.setSize(width, height);
        this.body = body;
        this.setPosition(this.body.getPosition().x * PPM - (float) width / 2, this.body.getPosition().y * PPM - (float) width / 2);
        this.currentKind = currentKind;
        image = new Texture(objectName);
        animations = new Animation[numberOfKinds];
        TextureRegion[][] tmp = TextureRegion.split(image, defaultWidth, defaultHeight);
        for (int i = 0; i < numberOfKinds; i++) {
            TextureRegion[] animationFrames = new TextureRegion[numberOfFrames];
            System.arraycopy(tmp[i], 0, animationFrames, 0, numberOfFrames);
            animations[i] = new Animation<>(0.2f, animationFrames);
        }
    }

    public void update() {
        if (animations[currentKind - 1].isAnimationFinished(stateTime)) {
            stateTime = 0;
        }
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animations[currentKind - 1].getKeyFrame(stateTime);
        this.setRegion(currentFrame);
    }
    public Body getBody(){
        return body;
    }
}
