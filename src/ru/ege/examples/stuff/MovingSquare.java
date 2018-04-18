package ru.ege.examples.stuff;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Квадратик управляемый с клавиатуры
 */
public class MovingSquare implements KeyListener, DrawableObject, Coordinates {
    Vector2D cords = new Vector2D(200, 200);
    double speed = 500;
    Vector2D direction = new Vector2D(0, 0);
    int size = 50;
    Color color = Color.BLACK;



    public MovingSquare(Vector2D cords) {
        this.cords = cords;
    }

    public void drawAndUpdate(Graphics2D g, double dt) {
        cords = cords.add(direction.scale(speed * dt));
        g.setColor(color);
        g.fillRect(cords.getXInt()  - size / 2, cords.getYInt() - size / 2, size, size);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                direction = new Vector2D(-1, 0);
                break;
            case KeyEvent.VK_D:
                direction = new Vector2D(1, 0);
                break;
            case KeyEvent.VK_W:
                direction = new Vector2D(0, -1);
                break;
            case KeyEvent.VK_S:
                direction = new Vector2D(0, 1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        direction = new Vector2D(-0, 0);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public Vector2D getCoordinates() {
        return cords;
    }
}
