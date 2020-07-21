package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.PlayScreen;

import java.awt.*;

public class box extends Sprite {
    private World world;
    private TiledMap map;
    private TiledMapTile tile;
    private Rectangle bounds;
    public Body body;
    private TextureAtlas atlas;
    private Fixture fixture;
    private TextureRegion boxer;
    private TextureRegion boxer2;
    private PlayScreen screen;
    private float x=0,y=0;
    private boolean on=false;
    public box (World world, TiledMap screen, PlayScreen scrn, Rectangle rect, int i){
        super(scrn.getAtlas().findRegion("box"));
        boxer = new TextureRegion(getTexture(), 51,1,16,16);
        boxer2 = new TextureRegion(getTexture(), 67,1,16,16);
this.screen=scrn;





        this.world = world;
        this.map = map;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth()/2)/MyGame.PPM, (rect.getY() + rect.getHeight()/2)/MyGame.PPM);

        body = world.createBody(bdef);



        shape.setAsBox(8/MyGame.PPM - 3/MyGame.PPM, 8/MyGame.PPM- 3/MyGame.PPM);
        fdef.shape = shape;



        body.createFixture(fdef).setUserData("box");


        setBounds(0,0, 16/MyGame.PPM, 16/MyGame.PPM);
        setRegion(boxer);
    }

    public void update(float dt, locationToPut[] loce){

        onBox(loce);


        setPosition(body.getPosition().x - getWidth() / 2, (body.getPosition().y - getHeight() / 2));
        body.setTransform(body.getPosition().x+(16*x)/MyGame.PPM, body.getPosition().y+(16*y)/MyGame.PPM, 0);

        if(!screen.moving && (x != 0 || y != 0)) {
            x = 0;
            y = 0;

            body.setTransform(16*((int)(body.getPosition().x/0.16f * 2))/2f/MyGame.PPM ,  16*((int)(body.getPosition().y/0.16f * 2))/2f/MyGame.PPM,0);

        }
    }

    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y=y;
    }
private Body bodi=null;
    private boolean correct = false;
    public void onBox(locationToPut[] loce) {


            for (locationToPut locationas : loce) {

                if (((int) (locationas.getX() * MyGame.PPM) > (int) (body.getPosition().x * MyGame.PPM) - 5) && ((int) (locationas.getX() * MyGame.PPM) < (int) (body.getPosition().x * MyGame.PPM) + 5)
                        && ((int) (locationas.getY() * MyGame.PPM) > (int) (body.getPosition().y * MyGame.PPM) - 5) && ((int) (locationas.getY() * MyGame.PPM) < (int) (body.getPosition().y * MyGame.PPM) + 5)) {

                    bodi = body;

                   if(!correct) {
                       setRegion(boxer2);
                       correct = true;
                       MyGame.correct++;
                   }break;
                } else {
                    if(correct == true) {
                        correct= false;
                        MyGame.correct--;
                        setRegion(boxer);
                    }

                }
            }
        }

    }


