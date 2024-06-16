package com.mygdx.game.utilz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthHandler extends Sprite {
    private int health;
    private int power;
    String healthBar;
    String powerBar;
    public HealthHandler(int health, int power){
        super(new Texture(Gdx.files.internal("UI/HealthBar.png")));
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        this.health = health;
        this.power = power;
//        healthBar = new String("UI/HealthBar.png");
//        powerBar = new String("UI/PowerBar.png");
    }
}
