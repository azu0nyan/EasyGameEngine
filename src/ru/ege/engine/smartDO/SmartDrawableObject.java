package ru.ege.engine.smartDO;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.EGEngine;

import java.awt.*;



public abstract class SmartDrawableObject implements DrawableObject {
    public SmartDrawableObject() {
        EGEngine.i().addDrawableObject(this);
    }

    @Override
    public final void drawAndUpdate(Graphics2D g, double dt) {
        draw(g);
        update(dt);
    }

    protected abstract void update(double dt);

    public abstract void draw(Graphics2D g);
}
