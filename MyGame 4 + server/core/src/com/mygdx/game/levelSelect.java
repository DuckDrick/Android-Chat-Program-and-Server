package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class levelSelect implements Screen {

    private MyGame game;
    private Viewport viewport;
    private Stage stage;


    public levelSelect(MyGame game){
        this.game = game;
        viewport = new FitViewport(400, 208, new OrthographicCamera());
        stage = new Stage(viewport, ((MyGame) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label select = new Label("Level select", font);
        Label playNext = new Label("press numbers 1-3 to select level", font);
        select.setFontScale(2);
        table.add(select).expandX();
        table.row();
        table.add(playNext).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) {
            Hud.level=1;
            game.setScreen(new PlayScreen((MyGame) game));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
            Hud.level=2;
            game.setScreen(new PlayScreen((MyGame) game));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3)) {
            Hud.level=3;
            game.setScreen(new PlayScreen((MyGame) game));
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
}
