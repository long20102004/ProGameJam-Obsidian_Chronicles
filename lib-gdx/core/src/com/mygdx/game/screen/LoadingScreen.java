package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.screen.GameScreen;

import static com.mygdx.game.utilz.Constant.SCREEN_HEIGHT;
import static com.mygdx.game.utilz.Constant.SCREEN_WIDTH;

public class LoadingScreen implements Screen {
    private MyGdxGame game; // your main game class
    private float delaySeconds = 1f; // delay in seconds
    private Texture texture;
    private Stage stage;
    private ProgressBar progressBar;
    private float timeElapsed;

    public LoadingScreen(MyGdxGame game) {
        this.game = game;
        texture = new Texture("UI/loading.jpg");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Create a white texture for the knob
        Pixmap pixmap = new Pixmap(7, 7, Pixmap.Format.RGBA8888);
        float maxProgressBarValue = delaySeconds * 1.1f; // adjust this factor as needed
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap); // remember to dispose of this later
        pixmap.dispose();

        Texture barTexture = new Texture(Gdx.files.internal("UI/HealthBar.png"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(barTexture));
        TextureRegionDrawable inside = new TextureRegionDrawable(new TextureRegion(whiteTexture));

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = drawable;
        progressBarStyle.knob = inside;

        progressBar = new ProgressBar(0, maxProgressBarValue, 0.1f, false, progressBarStyle);
        float progressBarX = (SCREEN_WIDTH - progressBar.getWidth()) / 2.3f;
        float progressBarY = (SCREEN_HEIGHT - progressBar.getHeight()) / 2;

        // Set the position of the progress bar
        progressBar.setBounds(progressBarX, progressBarY, 200, 60);

        stage.addActor(progressBar);
    }

    @Override
    public void show() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(game.getGameScreen());
            }
        }, delaySeconds);
    }

    @Override
    public void render(float delta) {
        // clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update progress bar
        timeElapsed += delta;
        progressBar.setValue(timeElapsed);

        // display loading information
        game.getBatch().begin();
//        game.getBatch().draw(texture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        game.getBatch().end();

        // draw progress bar
        stage.act();
        stage.draw();
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

    // ... implement other Screen methods (resize, pause, resume, hide, dispose)
}