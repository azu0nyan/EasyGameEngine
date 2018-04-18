package ru.ege.examples.TankiOnline;

import ru.ege.engine.EGEngine;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Random;

public class Game {

    static final int spawns[][] = new int[][]{
            {300, 150},
            {600, 150},
            {900, 150},
            {1200, 150},
            {300, 750},
            {600, 750},
            {900, 750},
            {1200, 750},
            {120, 300},
            {120, 600},
            {1500, 300},
            {1500, 600}};

    static final Rectangle[] walls = new Rectangle[]{
            new Rectangle(270, 290, 20, 300),
            new Rectangle(270, 600, 200, 20),
            new Rectangle(450, 600, 20, 300),
            new Rectangle(700, 600, 20, 300),
            new Rectangle(1050, 600, 20, 300),
            new Rectangle(450, 0, 20, 250),
            new Rectangle(750, 0, 20, 250),
            new Rectangle(1050, 0, 20, 250),
            new Rectangle(0, 0, 1920, 50),
            new Rectangle(0, 0, 30, 1080),
            new Rectangle(1580, 0, 50, 1080),
            new Rectangle(0, 850, 1920, 50)
    };

    public static boolean checkLoS(Vector2D point1, Vector2D point2){
        Line2D line2D = new Line2D.Double(point1.getX(), point1.getY(), point2.getX(), point2.getY());

        for(Rectangle r : walls){
            if(line2D.intersects(r)){
                return false;
            }
        }
        return true;
    }

    public static void init() {
        //leaderboards
        EGEngine.i().addDrawableObject(Leaderboard.i(), 100);
        //info
        EGEngine.i().addDrawableObject(new ServerInfoDrawer(), -100);
        //map
        Random r = new Random();
        int screenW = EGEngine.i().getWidth();
        int screenH = EGEngine.i().getHeight();
        for (int i = 0; i < walls.length; i++) {
            Wall wall = new Wall(walls[i]);
            EGEngine.i().addDrawableObject(wall, -1);
        }
        //waypoints
        Waypoint.init();
        //targets
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(screenW);
            int y = r.nextInt(screenH);
            Target t = new Target(new Vector2D(x, y));
            EGEngine.i().addDrawableObject(t);
        }
        //bots
        if (ServerMain.bots > 0) {
            for(int i = 0; i < ServerMain.bots; i++){
                TankAI ai = new TankAI("server" + i);
                EGEngine.i().addDrawableObject(ai, 100);
            }
        }

    }

    public static Tank spawnTank(String name) {
        Random r = new Random();
        int spawnID = r.nextInt(spawns.length);
        Tank p = new Tank(new Vector2D(spawns[spawnID][0], spawns[spawnID][1]),
                new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
                name);
        EGEngine.i().addDrawableObject(p);
        return p;
    }

}
