package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.PlayScreen;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Character extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public boolean spacePressed;
    private World world;
    public Body b2body;
    private TextureRegion boxer;
    public boolean pushing;

    private float stateTimer;
    private boolean runningRight;
private PlayScreen screen;
private TiledMap map;
    public Character(World world, PlayScreen screen, TiledMap map){
        super(screen.getAtlas().findRegion("pixil-layer-Background (5)"));
        boxer = new TextureRegion(getTexture(), 1,1,16,16);
        this.map = map;
        this.world = world;
        this.screen = screen;

        defineChar();
        setBounds(0,0, 16/MyGame.PPM, 16/MyGame.PPM);
        setRegion(boxer);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2)-1/MyGame.PPM);
    }


    public void defineChar(){

        BodyDef bdef = new BodyDef();

        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/MyGame.PPM, (rect.getY() + rect.getHeight()/2)/MyGame.PPM);


        }


        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();



        PolygonShape shape = new PolygonShape();

        shape.setAsBox(8/MyGame.PPM - 3/MyGame.PPM, 8/MyGame.PPM- 3/MyGame.PPM);

        fdef.shape = shape;


        b2body.createFixture(fdef);


        EdgeShape sensor = new EdgeShape();
        sensor.set(new Vector2(0, 0), new Vector2(8f/MyGame.PPM, 0));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("right");

        //EdgeShape rightFar = new EdgeShape();
        sensor.set(new Vector2(32/MyGame.PPM, 0), new Vector2(32f/MyGame.PPM, 0));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("rightFar");


        //EdgeShape left = new EdgeShape();
        sensor.set(new Vector2(0, 0), new Vector2(-8f/MyGame.PPM, 0));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("left");

        sensor.set(new Vector2(-32/MyGame.PPM, 0), new Vector2(-32f/MyGame.PPM, 0));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("leftFar");


        sensor.set(new Vector2(0, 0), new Vector2(0,8f/MyGame.PPM));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("up");

        sensor.set(new Vector2(0,32/MyGame.PPM), new Vector2(0,32f/MyGame.PPM));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("upFar");



        sensor.set(new Vector2(0, 0), new Vector2(0,-8f/MyGame.PPM));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("down");

        sensor.set(new Vector2(0,-32/MyGame.PPM), new Vector2(0,-32f/MyGame.PPM));
        fdef.shape = sensor;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("downFar");



    }
    private Body bod;
    public void moveBox(FixtureDef fdef){

    }
private float posx, posy;
 private float xc, yc;
    public void move(int x, int y, PlayScreen screen){
        if(!screen.moving) {
            xc = b2body.getPosition().x + (16 / MyGame.PPM) * x;
           yc = b2body.getPosition().y + (16 / MyGame.PPM) * y;
            screen.moving = true;
            b2body.setLinearVelocity(0.8f * (float) x, 0.8f * (float) y);
            posx=b2body.getPosition().x; posy=b2body.getPosition().y;

        }


            if ((b2body.getPosition().x < xc + 0.005f && b2body.getPosition().x > xc - 0.005f)
                    && (b2body.getPosition().y < yc + 0.005f && b2body.getPosition().y > yc - 0.005f) ||
                    (b2body.getLinearVelocity().x == 0f && b2body.getLinearVelocity().y == 0f)
            ) {

                if (!(b2body.getLinearVelocity().x == 0f && b2body.getLinearVelocity().y == 0f))
                    b2body.setTransform(rounding(xc), rounding(yc), 0);
                else
                    b2body.setTransform(posx, posy, 0);

               b2body.setLinearVelocity(0, 0);

                screen.moving = false;
            } else {
                b2body.setLinearVelocity(0.8f * (float) x, 0.8f * (float) y);
            }

    }
    public int counte=0;
    private float rounding(double sk) {
        counte = 0;
        while (sk >= (double)0.08) {
            sk -= (double)0.08;
            counte++;
        }

        if (sk > (double)0.04){
             return (counte + 1) * 0.08f;
        }else{
            return counte * 0.08f;
        }
        }

    public void onContact(PlayScreen screen){
        screen.onGround = true;
    }
}
