package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Hud implements Disposable {
    private static int restarts = 0;
    public static int level = 1;
    private Viewport viewport;
    public Stage stage;
    private TextField ta;

    private Label tries;
    private Label lvl;
    private Skin skin;

    public Hud(SpriteBatch sb){
        viewport = new FitViewport(1200, 624, new OrthographicCamera());
        stage = new Stage(viewport, sb);


        Table table = new Table();

table.top();

//table.debug();
        table.setFillParent(true);

        tries = new Label(String.format("Restarts: %d", restarts), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lvl = new Label(String.format("Level: %d", level), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(tries).expandX();
        table.add(lvl).expandX();
        table.add(tries).expandX();
        table.add(lvl).expandX();


        stage.addActor(table);





    }

    public void addDeath(){
        restarts++;
        tries.setText(String.format("Restarts: %d",restarts));
    }

    @Override
    public void dispose() {
        stage.dispose();

    }

    public static void resetRestart(){
        restarts=0;
    }
    public void setDeaths(int set){
        restarts=set;
    }

}
