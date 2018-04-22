package ru.ege.engine.smartDO;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.awt.event.*;

public class Camera implements KeyListener, MouseListener, MouseMotionListener {

    public static Vector2D cameraLeftTopAngle = new Vector2D(0, 0);
    public static int cameraZoom = 9;
    public static double[] zooms = new double[]{0.01, 0.025, 0.05, 0.075, 0.1, 0.2, 0.25, 0.5, 0.75, 1, 1.5, 2, 2.5, 3, 4, 5, 7.5, 10, 15, 20};

    public static Vector2D mouseInWorld = new Vector2D(0, 0);
    public static Vector2D mouseOnScreen = new Vector2D(0, 0);

    public static double getZoom() {
        return zooms[cameraZoom % zooms.length];
    }

    public static double worldToScreen(double world) {
        return world * getZoom();
    }

    public static double screenToWorld(double screen) {
        return screen * getZoom();
    }

    public static Vector2D worldToScreen(Vector2D world) {
        return world.sub(cameraLeftTopAngle).scale(getZoom());
    }

    public static Vector2D screenToWorld(Vector2D screen) {
        return screen.scale(1 / getZoom()).add(cameraLeftTopAngle);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public static void enableControls(){
        controlsEnabled = true;
    }
    public static void disableControls(){
        controlsEnabled = false;
    }

    public static boolean controlsEnabled = false;
    public static   int zoomIN = KeyEvent.VK_U;
    public static   int zoomOUT = KeyEvent.VK_O;
    public static   int moveUP = KeyEvent.VK_I;
    public static   int moveDOWN = KeyEvent.VK_K;
    public static   int moveLEFT = KeyEvent.VK_J;
    public static   int moveRIGHT = KeyEvent.VK_L;

    @Override
    public void keyPressed(KeyEvent e) {
        if(!controlsEnabled){
            return;
        }
        if(e.getKeyCode() == zoomIN){
            cameraZoom--;
        }
        if(e.getKeyCode() == zoomOUT){
            cameraZoom++;
        }
        if(e.getKeyCode() == moveUP){
            cameraLeftTopAngle = cameraLeftTopAngle.add(new Vector2D(0, -100));
        }
        if(e.getKeyCode() == moveDOWN){
            cameraLeftTopAngle = cameraLeftTopAngle.add(new Vector2D(0, 100));
        }
        if(e.getKeyCode() == moveLEFT){
            cameraLeftTopAngle = cameraLeftTopAngle.add(new Vector2D(-100, 0));
        }
        if(e.getKeyCode() == moveRIGHT){
            cameraLeftTopAngle = cameraLeftTopAngle.add(new Vector2D(100, 0));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseOnScreen = new Vector2D(e.getX(), e.getY());
        mouseInWorld = screenToWorld(mouseOnScreen);
    }



}
