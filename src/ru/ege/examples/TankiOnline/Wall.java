package ru.ege.examples.TankiOnline;

import ru.ege.engine.DrawableObject;

import java.awt.*;

public class Wall implements DrawableObject {
    public Rectangle wallShape;
    public Color c = Color.BLACK;

    public Wall(int x, int y, int w, int h) {
        wallShape = new Rectangle(x,  y, w, h);
    }

    public Wall(Rectangle wall) {
        wallShape = wall;
    }

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        g.setColor(c);
        g.fill(wallShape);
    }
}
