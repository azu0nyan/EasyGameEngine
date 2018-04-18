package ru.ege.examples.TankiOnline;

import ru.ege.engine.DrawableObject;

import java.awt.*;

public class ServerInfoDrawer implements DrawableObject {
    boolean drawSpawnPos = false;
    boolean drawWaypointsNumbers = true;

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        //spawns
        g.setStroke(new BasicStroke(2));
        for (int[] spawn : Game.spawns) {
            g.setColor(new Color(128, 128, 255, 128));
            g.fillOval(spawn[0] - 10, spawn[1] - 10, 20, 20);
            if (drawSpawnPos) {
                g.setColor(Color.black);
                g.drawString(spawn[0] + ", " + spawn[1], spawn[0], spawn[1]);
            }
        }
        //waypoints
        for (Waypoint waypoint : Waypoint.waypoints) {
            g.setColor(new Color(77, 200, 77));
            g.fillOval(waypoint.getPos().getXInt() - 5, waypoint.getPos().getYInt() - 5, 10, 10);
            if(drawWaypointsNumbers) {
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(waypoint.getId()), waypoint.getPos().getXInt(), waypoint.getPos().getYInt());
            }
            for (Waypoint neighbour : waypoint.getNeighbours()) {
                g.setColor(new Color(100, 100, 100));
                g.setStroke(new BasicStroke(1));
                g.drawLine(waypoint.getPos().getXInt(), waypoint.getPos().getYInt(), neighbour.getPos().getXInt(), neighbour.getPos().getYInt());
            }
        }

    }
}
