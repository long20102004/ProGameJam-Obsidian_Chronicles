package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.mygdx.game.MyGdxGame;

public class MenuScreen implements Screen {
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private MyGdxGame game;

    public MenuScreen(MyGdxGame myGdxGame) {
        this.game = myGdxGame;
        this.camera = game.getCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        orthogonalTiledMapRenderer = game.getGameScreen().getTileMapHandler().setUpMenu();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.zoom = 2.6f;
        camera.setToOrtho(false, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
