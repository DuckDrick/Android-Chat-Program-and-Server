package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map, PlayScreen scrn){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/MyGame.PPM, (rect.getY() + rect.getHeight()/2)/MyGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/MyGame.PPM , rect.getHeight()/2/MyGame.PPM );

            fdef.shape = shape;
            fdef.friction=0;
            body.createFixture(fdef).setUserData("WALL");
        }

    }


}
