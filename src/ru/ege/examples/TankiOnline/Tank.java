package ru.ege.examples.TankiOnline;

import com.sun.security.ntlm.Server;
import ru.ege.engine.DrawableObject;
import ru.ege.engine.EGEngine;
import ru.ege.engine.Utils;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.List;

public class Tank  implements DrawableObject, KeyListener {
    String name;

    public boolean alive = true;

    int width = 30;
    int length = 60;
    int turretWidth = 27;
    int gunWidth = 5;
    int gunLength = 33;


    boolean debug = false;
    Vector2D position;
    double maxSpeed = 100;
    double moveDirection = 0; //-1  1
    double speed = 0;
    double acceleration = 30;
    double baseAngle = 1;

    double brakingSpeed = 50;
    double turretAngle = 0;
    double maxTurretRotationSpeed = Math.PI;
    long lastShot = 0;
    long shotCooldown = 500;

    double turretRotationDirection = 0;// -1 1
    double maxBaseRotationSpeed = Math.PI / 2;
    double baseRotationDirection = 0;// -1 1



    Color baseColor;
    Color turretColor;
    Color gunColor;


    public Tank(Vector2D position, Color color, String name) {
        this.position = position;
        this.name = name;
        this.baseColor = color;
        turretColor = new Color(baseColor.getRed() / 2, baseColor.getGreen() / 2, baseColor.getBlue() / 2);
        gunColor = Color.BLACK;
    }

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        updateState(dt);
        render(g);
    }


    public Shape getBaseRotated(){
        Rectangle r = new Rectangle(-length / 2, -width / 2, length, width);
        Path2D.Double path = new Path2D.Double();
        path.append(r, false);
        AffineTransform rotation = new AffineTransform();
        rotation.rotate(baseAngle);
        path.transform(rotation);
        AffineTransform move = new AffineTransform();
        move.translate(position.getXInt(), position.getYInt());
        //path.transform(move);
        Shape baseShape = path.createTransformedShape(move);
        return baseShape;
    }

    private void render(Graphics2D g) {
        //base
        g.setColor(baseColor);
        Shape baseShape = getBaseRotated();
        g.fill(baseShape);
        //turret
        g.setColor(turretColor);
        g.fillOval(position.getXInt() - turretWidth / 2, position.getYInt() - turretWidth / 2, turretWidth, turretWidth);
        g.setColor(Color.BLACK);
        //gun
        g.setStroke(new BasicStroke(gunWidth));
        Vector2D gunEnd = position.add(new Vector2D(gunLength, 0).rotate(turretAngle));
        g.drawLine(position.getXInt(), position.getYInt(), gunEnd.getXInt(), gunEnd.getYInt());
        if(debug){
            g.setColor(Color.RED);
            g.drawString(String.valueOf(speed), position.getXInt(), position.getYInt());
        }
    }

    private void updateState(double dt) {
        //backup if collided
        double oldBase = baseAngle;
        Vector2D oldPosition = position;
        //rotations
        turretAngle = turretAngle + turretRotationDirection * maxTurretRotationSpeed * dt;
        baseAngle = baseAngle + baseRotationDirection * maxBaseRotationSpeed * dt;
        //braking
        if(moveDirection != 0) {
            speed = speed + acceleration * dt * moveDirection;
        } else {
            speed = Math.signum(speed) * (Math.abs(speed) - brakingSpeed * dt);
        }
        //speed limit
        if(speed < 0 && -speed > maxSpeed){
            speed = -maxSpeed;
        }
        if(speed > 0 && speed > maxSpeed){
            speed = maxSpeed;
        }
        position = position.add(new Vector2D(speed, 0).rotate(baseAngle).scale(dt));
        if(checkCollisions()){
            baseAngle = oldBase;
            position = oldPosition;
            speed = 0;
        }
    }

    private boolean checkCollisions(){
        List<Wall> walls = EGEngine.i().getDrawableObjects(Wall.class);
        Shape baseShape = getBaseRotated();
        for(Wall wall : walls){
            if(Utils.testIntersection(wall.wallShape, baseShape)){
                return true;
            }
        }
        List<Tank> tanks = EGEngine.i().getDrawableObjects(Tank.class);
        for(Tank tank : tanks){
            if(tank !=this && Utils.testIntersection(tank.getBaseRotated(), baseShape)){
                return true;
            }
        }
        return false;
    }



    public void shotBullet() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastShot> shotCooldown) {
            Vector2D gunEnd = position.add(new Vector2D(gunLength, 0).rotate(turretAngle));
            Bullet b = new Bullet(gunEnd, new Vector2D(1, 0).rotate(turretAngle), 200, name);
            EGEngine.i().addDrawableObject(b);
            lastShot = currentTime;
        }
    }
    public boolean canShot(){
        return System.currentTimeMillis() - lastShot > shotCooldown;
    }

    public void beingShot(Bullet b){
        Leaderboard.i().add(b.ownerName, 10);
        EGEngine.i().removeDrawableObject(this);
        alive = false;
    }

    public void command(Command command){
        switch (command){
            case B_LEFT:
                baseRotationDirection = -1;
                break;
            case B_RIGHT:
                baseRotationDirection = 1;
                break;
            case B_FORWARD:
                moveDirection = 1;
                break;
            case B_BACKWARD:
                moveDirection = -1;
                break;
            case B_STOP_ROTATION:
                baseRotationDirection = 0;
                break;
            case B_STOP_MOVE:
                moveDirection = 0;
                break;
            case T_LEFT:
                turretRotationDirection = -1;
                break;
            case T_RIGHT:
                turretRotationDirection = 1;
                break;
            case T_STOP_ROTATION:
                turretRotationDirection = 0;
                break;
            case T_SHOT:
                shotBullet();
                break;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                command(Command.B_FORWARD);
                break;
            case KeyEvent.VK_S:
                command(Command.B_BACKWARD);
                break;
            case KeyEvent.VK_A:
                command(Command.B_LEFT);
                break;
            case KeyEvent.VK_D:
                command(Command.B_RIGHT);
                break;
            case KeyEvent.VK_Q:
                command(Command.T_LEFT);
                break;
            case KeyEvent.VK_E:
                command(Command.T_RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                command(Command.T_SHOT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                command(Command.B_STOP_MOVE);
                break;
            case KeyEvent.VK_S:
                command(Command.B_STOP_MOVE);
                break;
            case KeyEvent.VK_A:
                command(Command.B_STOP_ROTATION);
                break;
            case KeyEvent.VK_D:
                command(Command.B_STOP_ROTATION);
                break;
            case KeyEvent.VK_Q:
                command(Command.T_STOP_ROTATION);
                break;
            case KeyEvent.VK_E:
                command(Command.T_STOP_ROTATION);
                break;
            case KeyEvent.VK_SPACE:

                break;
        }
    }

    public String getName() {
        return name;
    }

    public Vector2D getPos() {
        return position;
    }
    public Vector2D getBaseAngleVector(){
        return new Vector2D(1,0).rotate(baseAngle);
    }
    public Vector2D getTurretAngleVector(){
        return new Vector2D(1,0).rotate(turretAngle);
    }
}
