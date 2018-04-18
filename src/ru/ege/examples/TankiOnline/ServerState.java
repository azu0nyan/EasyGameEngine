package ru.ege.examples.TankiOnline;

import ru.ege.engine.EGEngine;

import java.util.List;

/**
 * Created by Azu on 16.03.2017.
 */
public class ServerState {
    public List<Bullet> bullets;
    public List<Tank> tanks;
    public List<Wall> walls;
    public List<Target> targets;

    public static ServerState getState(){
        ServerState state = new ServerState();
        state.bullets = EGEngine.i().getDrawableObjects(Bullet.class);
        state.tanks = EGEngine.i().getDrawableObjects(Tank.class);
        state.walls = EGEngine.i().getDrawableObjects(Wall.class);
        state.targets = EGEngine.i().getDrawableObjects(Target.class);
        return state;
    }
}
