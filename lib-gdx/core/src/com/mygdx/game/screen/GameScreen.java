package com.mygdx.game.screen;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Audio.AudioPlayer;
import com.mygdx.game.entity.Entity;
import com.mygdx.game.object.AnimatedObject;
import com.mygdx.game.utilz.BodyHandler;
import com.mygdx.game.utilz.MyInputProcessor;
import com.mygdx.game.utilz.HealthHandler;
import com.mygdx.game.utilz.TileMapHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.utilz.Constant.*;

public class GameScreen extends ScreenAdapter {
    AudioPlayer audioPlayer = new AudioPlayer();
    private OrthographicCamera camera;
    private OrthographicCamera backgroundCamera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHandler tileMapHandler;
    private Sprite background;
    private Entity player;
    List<Entity> enemies = new ArrayList<>();
    List<AnimatedObject> objects = new ArrayList<>();
    private List<RayHandler> rayHandlers = new ArrayList<>();
    float time = 0;
    private List<PointLight> pointLights = new ArrayList<>();
    private Vector2 previousPlayerPosition = new Vector2();
    ShapeRenderer renderer;
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_MONSTER = 0x0002;
    public static final short CATEGORY_ENVIRONMENT = 0x0003;
    public static final short CATEGORY_OBJECTS = 0x0004;

    public GameScreen(OrthographicCamera camera, SpriteBatch batch) {
        System.out.println("Screen is created");
        this.camera = camera;
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        backgroundCamera = new OrthographicCamera();
        backgroundCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        backgroundCamera.update();
        this.batch = batch;
        this.world = new World(new Vector2(0, -25f), false);
        this.tileMapHandler = new TileMapHandler(this);
        this.orthogonalTiledMapRenderer = tileMapHandler.setupMap();
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        background = new Sprite(new Texture("background/background5.png"));
        player.setGameScreen(this);
        BodyHandler.setCollisionFilter(player.getBody(), CATEGORY_PLAYER, (short) ~CATEGORY_MONSTER);
        for (Entity enemy : enemies) {
            BodyHandler.setCollisionFilter(enemy.getBody(), CATEGORY_MONSTER, (short) ~CATEGORY_MONSTER);

        }
        Gdx.input.setInputProcessor(new MyInputProcessor());
        audioPlayer.setMusic(AudioPlayer.BATTLE);
    }

    private void update() {
        audioPlayer.update();
        // Calculate the difference in the player's position
        Vector2 playerPosition = player.getBody().getPosition();
        Vector2 deltaPosition = playerPosition.cpy().sub(previousPlayerPosition);

        // Move the background by a fraction of this difference
        background.setPosition(background.getX() - deltaPosition.x * 0.1f, background.getY() - deltaPosition.y * 0.1f);

        // Update the player's previous position
        previousPlayerPosition.set(playerPosition);
        player.setScale(0.6f);
        for (Entity entity : enemies) entity.setScale(0.5f);
        camera.zoom = (0.75f);
        time += Gdx.graphics.getDeltaTime();

        // Calculate the light intensity
        float minIntensity = 0.3f;
        float maxIntensity = 1f;
        float intensity = (float)((Math.sin(time) + 1) / 2 * (maxIntensity - minIntensity) + minIntensity);

        // Set the light intensity
        for (PointLight pointLight : pointLights) {
            pointLight.setDistance(intensity);
        }

        world.step(1 / 60f, 6, 2);
        for (RayHandler rayHandler : rayHandlers){
            rayHandler.update();
            rayHandler.setCombinedMatrix(camera.combined.cpy().scl(PPM));
        }
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        player.update(this);
        for (Entity enemy : enemies) {
            enemy.update(this);
//            BodyHandler.setCollisionFilter(enemy.getBody(), CATEGORY_MONSTER, (short) ~CATEGORY_PLAYER);
        }
        for (AnimatedObject object : objects) object.update();
        if (!player.isActive()) Gdx.app.exit();
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = Math.round(player.getBody().getPosition().x * PPM * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y * PPM * 10) / 10f;
        camera.position.set(position);
        camera.update();
    }


    @Override
    public void render(float delta) {
        this.update();
        Gdx.gl.glClearColor(25f / 255, 25 / 255f, 25f / 255, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        orthogonalTiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(backgroundCamera.combined);
        batch.begin();
        batch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();

//        batch.draw(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        orthogonalTiledMapRenderer.render();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1); // Màu đỏ
        pixmap.fill();
        Texture redTexture = new Texture(pixmap);
        pixmap.dispose();

        batch.begin();
        batch.draw(redTexture, 27,436,player.getCurrentHealth(),11);
        batch.draw(new Texture("UI/HealthBar.png"), 20, 430, 120, 20);

        TextureRegion tmp = new TextureRegion(new Texture("UI/PowerBar.png"), Math.max(35*(player.getCurrentPower()/10 - 1),0), 0, 105/3, 16);
        batch.draw(tmp, 25, 420, 80, 20);
        batch.setProjectionMatrix(camera.combined);
        batch.end();
        batch.begin();
        for (Entity enemy : enemies) {
            if (enemy.isActive()) enemy.draw(batch);
        }
        for (AnimatedObject object : objects) {
            object.draw(batch);
        }
//        box2DDebugRenderer.render(world, camera.combined.cpy().scl(PPM));
//
        player.draw(batch);

//        player.drawHitbox();
        batch.end();
        for (RayHandler rayHandler : rayHandlers){
            rayHandler.render();
        }
//        renderer.setProjectionMatrix(camera.combined);
//
//        renderer.begin(ShapeRenderer.ShapeType.Line);
//        renderer.setColor(Color.RED);
//        for (Entity entity : enemies){
//            renderer.rect(entity.getHitBox().getX(), entity.getHitBox().getY(), entity.getHitBox().getWidth(), entity.getHitBox().getHeight());
//            renderer.rect(entity.getAttackBox().getX(), entity.getAttackBox().getY(), entity.getAttackBox().getWidth(), entity.getAttackBox().getHeight());
//        }
//        renderer.rect(player.getAttackBox().getX(), player.getAttackBox().getY(), player.getAttackBox().width, player.getAttackBox().getHeight());
//        renderer.rect(player.getHitBox().getX(), player.getHitBox().getY(), player.getHitBox().getWidth(), player.getHitBox().getHeight());
//        renderer.end();
    }

    @Override
    public void dispose() {
        for (RayHandler rayHandler : rayHandlers) rayHandler.dispose();
        renderer.dispose();
    }

    public World getWorld() {
        return world;
    }
    public void addRayHandler(Body body, boolean isStrong){
        RayHandler rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(1f);
        if (!isStrong) {
            PointLight pointLight = new PointLight(rayHandler, 200, Color.WHITE, 5, body.getPosition().x + 3, body.getPosition().y);
            pointLight.setSoftnessLength(3f);
            pointLight.attachToBody(body, 0, 0);
            pointLight.setColor(new Color(1, 1, 1, 0.8f));
        }
        else{
            PointLight pointLight = new PointLight(rayHandler, 200, Color.WHITE, 1, body.getPosition().x + 3, body.getPosition().y);
            addPointLight(pointLight);
            pointLight.setSoftnessLength(0f);
            pointLight.attachToBody(body, 0, 0);
            pointLight.setColor(new Color(1, 1, 1, 1f));
        }
        rayHandlers.add(rayHandler);
    }
    public void setPlayer(Entity entity) {
        this.player = entity;
        addRayHandler(entity.getBody(), false);
    }

    public void addEnemy(Entity enemy) {
        enemies.add(enemy);
    }

    public void addObject(AnimatedObject object) {
        objects.add(object);
    }

    public Entity getPlayer() {
        return player;
    }
    public List<Entity> getEnemies(){
        return enemies;
    }
    public void addPointLight(PointLight pointLight){
        pointLights.add(pointLight);
    }

    public TileMapHandler getTileMapHandler() {
        return tileMapHandler;
    }

}
