package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Menu implements Screen {
    private MyGame game;
    private Viewport viewport;
    private Stage stage;
    private Texture texture;
    private Texture select;
    private int cursor = 0;
    public Menu(MyGame game){
        this.game = game;
        viewport = new FitViewport(400, 208, new OrthographicCamera());
        stage = new Stage(viewport, ((MyGame) game).batch);

        texture = new Texture (Gdx.files.internal("menu.png"));
        select = new Texture (Gdx.files.internal("select.png"));


    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            if(cursor == 0) {
                Hud.resetRestart();
                Hud.level=1;
                game.setScreen(new PlayScreen((MyGame) game));
            }else{
                game.setScreen(new levelSelect(game));
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

        game.batch.begin();
        game.batch.draw(texture, 0,0);

        if(cursor == 0){
            game.batch.draw(select, 150, 65);
        }else{
            game.batch.draw(select, 100, 35);
        }

        inputHandle(delta);

        game.batch.end();
    }

    public void inputHandle(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            cursor--;
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            cursor++;

        if(cursor > 1) cursor = 1;
        if(cursor < 0) cursor = 0;
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
