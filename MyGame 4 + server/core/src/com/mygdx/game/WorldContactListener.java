package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {

    private PlayScreen screen;
    private Character character;
    private box[] box;

    private boolean noRight = false;
    private boolean noLeft = false;
    private boolean noUp = false;
    private boolean noDown = false;
    private Fixture pushR = null;
    private Fixture pushL = null;
    private Fixture pushU = null;
    private Fixture pushD = null;

    public WorldContactListener(PlayScreen screen, Character character, box[] box) {
        this.screen = screen;
        this.character = character;
        this.box = box;

    }


    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        if (noRight == false)
            if (fixA.getUserData() == "right") {
                for (box deze : box) {
                    if (deze.body.getFixtureList().get(0) == fixB) {
                        deze.setX(0.09f);
                    }
                }
            }

        if (noLeft == false)
            if (fixA.getUserData() == "left") {
                for (box deze : box) {
                    if (deze.body.getFixtureList().get(0) == fixB) {
                        deze.setX(-0.08f);
                    }
                }
            }

        if (noUp == false)
            if (fixA.getUserData() == "up") {
                for (box deze : box) {
                    if (deze.body.getFixtureList().get(0) == fixB) {
                        deze.setY(0.09f);
                    }
                }
            }

        if (noDown == false)
            if (fixA.getUserData() == "down") {
                for (box deze : box) {
                    if (deze.body.getFixtureList().get(0) == fixB) {
                        deze.setY(-0.08f);
                    }
                }
            }

        if ((fixA.getUserData() == "rightFar" || fixB.getUserData() == "rightFar")) {
          //  System.out.println("Begin: " + fixA.getUserData() + " " + fixB.getUserData());
            noRight = true;
            pushR = fixB;
        }
        if ((fixA.getUserData() == "leftFar" || fixB.getUserData() == "leftFar")) {
            noLeft = true;
            pushL = fixB;
        }
        if ((fixA.getUserData() == "upFar" || fixB.getUserData() == "upFar")) {
            noUp = true;
            pushU = fixB;
        }
        if ((fixA.getUserData() == "downFar" || fixB.getUserData() == "downFar")) {
            noDown = true;
            pushD = fixB;
        }
    }


    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ((fixA.getUserData() == "rightFar" || fixB.getUserData() == "rightFar") && pushR == fixB) {
           // System.out.println("end: " + fixA.getUserData() + " " + fixB.getUserData());
            noRight = false;
        }
        if ((fixA.getUserData() == "leftFar" || fixB.getUserData() == "leftFar") && pushL == fixB) {
            noLeft = false;
        }
        if ((fixA.getUserData() == "upFar" || fixB.getUserData() == "upFar") && pushU == fixB) {
            noUp = false;
        }
        if ((fixA.getUserData() == "downFar" || fixB.getUserData() == "downFar") && pushD == fixB) {
            noDown = false;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
