package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Audio.AudioPlayer;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.LoadingScreen;
import com.mygdx.game.screen.MenuScreen;

public class MyGdxGame extends Game {
    public static MyGdxGame INSTANCE;
    public int SCREEN_WIDTH, SCREEN_HEIGHT;
    private SpriteBatch batch;
    OrthographicCamera camera;
    public MyGdxGame(){
        INSTANCE = this;
    }
    private GameScreen gameScreen;
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;

    @Override
    public void create() {
//        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        System.out.println("Game is created");
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        gameScreen = new GameScreen(camera, batch);
        loadingScreen = new LoadingScreen(this);
        menuScreen = new MenuScreen(this);
        setScreen(loadingScreen);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose() {

    }

    public SpriteBatch getBatch() {
        return batch;
    }
    public OrthographicCamera getCamera(){
        return camera;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public LoadingScreen getLoadingScreen() {
        return loadingScreen;
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }
}