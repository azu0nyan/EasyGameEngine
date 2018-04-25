package ru.ege.examples.stuff;

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import ru.ege.engine.EGEngine;
import ru.ege.engine.Vector2D;
import ru.ege.engine.smartDO.BodyDO;
import ru.ege.engine.smartDO.Camera;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

class MyUserData{
    public MyUserData(BodyDO bodyDO, Object data) {
        this.bodyDO = bodyDO;
        this.data = data;
    }

    BodyDO bodyDO;
    Object data;
}


public class ContactsDemo {
    public static void main(String [] args){
        EGEngine.i().startDrawingThread();

        DebugObjects.addDebugObjects(EGEngine.i());
        EGEngine.i().enableCamera();
        Camera.enableControls();
        EGEngine.i().getWorld().setGravity(new Vec2(0, 100));

        //Линии
        new BodyDO(new Vec2(900, 1000), 0, BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));
        new BodyDO(new Vec2(900, 50), 0, BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));
        new BodyDO(new Vec2(50, 1000), (float) (Math.PI / 2), BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));
        new BodyDO(new Vec2(1500, 50), (float) (Math.PI / 2), BodyType.STATIC, new Vec2(-1000, 0), new Vec2( 1000, 0));

        BodyDO press = new BodyDO(new Vec2(500, 500), 0,  BodyType.DYNAMIC, 200, 10);
        press.getBody().setUserData(new MyUserData(press, "PRESS"));
        for(int i = -3 ; i < 3; i++){
            for(int j = -3 ; j < 3; j ++){
                BodyDO junk = new BodyDO(new Vec2(500 + 30 * i, 800 + 30 * j ), 0,  BodyType.DYNAMIC, 20 - j * 3, 20 + i * 3);
                junk.getBody().setUserData(new MyUserData(junk, "JUNK"));
            }
        }





        EGEngine.i().getWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                MyUserData bodyA = null;
                MyUserData bodyB = null;
                if(contact.getFixtureA().getBody().getUserData() instanceof MyUserData){
                    bodyA = (MyUserData) contact.getFixtureA().getBody().getUserData();
                }
                if(contact.getFixtureB().getBody().getUserData() instanceof MyUserData){
                    bodyB = (MyUserData) contact.getFixtureB().getBody().getUserData();
                }
                if(bodyA != null && bodyB != null){
                    BodyDO toChange = null;

                    if(("PRESS".equals(bodyA.data) && "JUNK".equals(bodyB.data))){
                        System.out.println(bodyA.data + " " + bodyB.data);
                        toChange = bodyB.bodyDO;
                    }
                    if(("PRESS".equals(bodyB.data) && "JUNK".equals(bodyA.data))){
                        System.out.println(bodyA.data + " " + bodyB.data);
                        toChange = bodyA.bodyDO;
                    }
                    System.out.println(toChange);
                    if(toChange != null){
                        toChange.setColor(new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                //contact.setEnabled(false);
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        EGEngine.i().addDrawableObject((a, b) ->{
            Vec2 dest = Camera.mouseInWorld.toVec2();//Camera.screenToWorld(new Vector2D(e.getX(), e.getY())).toVec2();
            Vec2 pos = press.getBody().getPosition();
            Vec2 force = dest.sub(pos);
            force = force.mul(200 * 10 * 200);
            press.getBody().applyForceToCenter(force);
        });

    }
}
