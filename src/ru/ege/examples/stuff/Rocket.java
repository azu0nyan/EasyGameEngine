package ru.ege.examples.stuff;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.Vector2D;

import java.awt.*;

public class Rocket implements DrawableObject, Coordinates {
    Vector2D cords;
    Vector2D speed;
    Coordinates target;

    double turnRate = Math.PI;

    public Rocket(Vector2D cord, Vector2D speed, Coordinates target) {
        this.cords = cord;
        this.speed = speed;
        this.target = target;
    }


    public void drawAndUpdate(Graphics2D g, double dt) {
        g.setColor(Color.RED);
        //вектор из пули в цель
        Vector2D toTarget = target.getCoordinates().sub(cords);
        double angle = speed.angle(toTarget);
        double turnValue = turnRate * dt ;
        turnValue = Math.min(turnValue, angle);
        speed = speed.rotate(turnValue);

        cords = cords.add(speed.scale(dt));
        g.fillOval(cords.getXInt() - 10, cords.getYInt() - 10, 20, 20);
        g.drawLine(cords.getXInt(), cords.getYInt(),cords.add(speed.normalize().scale(30)).getXInt(), cords.add(speed.normalize().scale(30)).getYInt());
    }

    @Override
    public Vector2D getCoordinates() {
        return cords;
    }
}
