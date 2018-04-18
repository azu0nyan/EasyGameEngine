package ru.ege.examples.TankiOnline;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.EGEngine;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.util.Random;

/**
 * Created by Azu on 16.03.2017.
 */
public class Target implements DrawableObject {
    public Vector2D position;
    public int size = 50;

    public Target(Vector2D position) {
        this.position = position;
    }

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.red);
        g.drawOval(position.getXInt() - size / 2, position.getYInt() - size / 2, size, size);
        g.drawLine(position.getXInt(), position.getYInt() - size /2, position.getXInt(), position.getYInt() + size /2);
        g.drawLine(position.getXInt()- size /2, position.getYInt(), position.getXInt() + size / 2, position.getYInt());
    }

    public void beingShot(Bullet b){
        Random r = new Random();
        int x = r.nextInt(EGEngine.i().getWidth() - 100) + 50;
        int y = r.nextInt(EGEngine.i().getHeight() -100) + 50;
        position = new Vector2D(x, y);
        Leaderboard.i().add(b.ownerName, 1);
    }
}
