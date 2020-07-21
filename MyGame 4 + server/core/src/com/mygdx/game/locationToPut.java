package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;


public class locationToPut extends Sprite {

    private World world;
    private PlayScreen screen;
    private TiledMap map;
    private TextureRegion put;
    public Body body;
    private float x, y;
    public locationToPut(World world, TiledMap screen, PlayScreen scrn, Rectangle rect, int i){
            super(scrn.getAtlas().findRegion("spot"));
            put = new TextureRegion(getTexture(), 85,1,16,16);
            this.screen=scrn;





            this.world = world;
            this.map = map;

            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/MyGame.PPM, (rect.getY() + rect.getHeight()/2)/MyGame.PPM);

            body = world.createBody(bdef);

            x= body.getPosition().x;
            y=body.getPosition().y;


        setBounds(0,0, 16/MyGame.PPM, 16/MyGame.PPM);
        setRegion(put);

        setPosition(body.getPosition().x - getWidth() / 2, (body.getPosition().y - getHeight() / 2));

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
