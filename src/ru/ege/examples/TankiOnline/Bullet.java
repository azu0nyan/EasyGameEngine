package ru.ege.examples.TankiOnline;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.EGEngine;
import ru.ege.engine.Settings;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.util.List;


public class Bullet implements DrawableObject {
    public boolean debug = Settings.DEBUG_GAMEPLAY;
    public Vector2D position;
    public Vector2D direction;
    public double speed;
    public String ownerName;
    public int size = 5;

    public Bullet(Vector2D position, Vector2D direction, double speed, String ownerName) {
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.ownerName = ownerName;
    }

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        position = position.add(direction.normalize().scale(speed).scale(dt));
        List<Target> targets = EGEngine.i().getDrawableObjects(Target.class);
        for(Target target : targets){
            if(position.sub(target.position).length() < size / 2 + target.size / 2){
                if(debug) {
                    System.out.println("Bullet:Target destroyed");
                }
               target.beingShot(this);
            }
        }
        List<Wall> walls = EGEngine.i().getDrawableObjects(Wall.class);
        for(Wall wall : walls){
            if(wall.wallShape.contains(position.getX(), position.getY())){
                if(debug) {
                    System.out.println("Bullet:Hit the wall");
                }
                EGEngine.i().removeDrawableObject(this);
            }
        }
        if(position.getX() < 0 || position.getX() > EGEngine.i().getWidth() || position.getY() < 0 || position.getY() > EGEngine.i().getHeight()){
            if(debug) {
                System.out.println("Bullet:Out of screen");
            }
            EGEngine.i().removeDrawableObject(this);
        }
        List<Tank> tanks = EGEngine.i().getDrawableObjects(Tank.class);
        for(Tank t : tanks){
            if(!t.getName().equals(ownerName) && t.getBaseRotated().contains(position.getXInt(), position.getYInt())){
                t.beingShot(this);
            }
        }

        g.setColor(Color.BLUE);
        g.fillOval(position.getXInt() - size / 2, position.getYInt() - size /2 , size, size);
    }
}
