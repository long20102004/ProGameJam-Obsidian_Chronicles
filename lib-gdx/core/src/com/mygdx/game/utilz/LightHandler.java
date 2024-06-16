package com.mygdx.game.utilz;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class LightHandler {
    public static RayHandler rayHandler;
    public static PointLight pointLight;
    public static RayHandler addLight(World world, Body body){
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.7f);
        pointLight = new PointLight(rayHandler, 200, Color.WHITE, 6, body.getPosition().x, body.getPosition().y);
        pointLight.setSoftnessLength(3f);
        pointLight.attachToBody(body,0,0);
        pointLight.setColor(new Color(1, 1, 1, 0.7f));
        return rayHandler;
    }
}
