package com.mygdx.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.game.MyGame;

public class Input implements Disposable {
    private Skin skin;

    private Viewport viewport;
    public Stage stage;
private Table table;
    private TextButton button;
    private Input t = this;
    public Input(SpriteBatch sb){


        viewport = new FitViewport(1200, 624, new OrthographicCamera());
        stage = new Stage(viewport, sb);


        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        button = new TextButton("Send", skin, "default");
        final TextArea TF = new TextArea("", skin);

        TF.setWidth(600);
        TF.setHeight(570);
        TF.setPosition(0,54);

        button.setWidth(600);
        button.setHeight(54);
        button.setPosition(0, 0);
       // TF.debug();
       // //System.out.println(TF.getDebug());
       // button.debug();
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               // SplitString s1 = new SplitString(TF.getText(), t);
               TF.setText("");
              //  s1.start();
                button.setTouchable(Touchable.disabled);
            }
        });

        stage.addActor(TF);
        stage.addActor(button);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(stage);

    }

    public void setButtonTouchable(){
        button.setTouchable(Touchable.enabled);
    }

    @Override
    public void dispose() {
stage.dispose();
    }
}
