package ru.ege.examples.TankiOnline;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.EGEngine;
import ru.ege.engine.Vector2D;

import java.awt.*;

public class TankAI implements DrawableObject {
    boolean logAi = true;
    String name;
    Tank controlledTank = null;
    Waypoint currentWay = null;
    double epsilonDistance = 50;
    double epsilonAngle = 0.2;
    double epsilonTurretAngle = 0.05;
    double maxMoveForwardAngleDiff = Math.PI / 8;

    public TankAI(String name) {
        this.name = name;
    }

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        //checkSpawn
        if(controlledTank == null || !controlledTank.alive){
            controlledTank = Game.spawnTank(name);
            currentWay = null;
        }
        //find waypoint
        if(currentWay == null){
            Waypoint nearest = Waypoint.waypoints.stream()
                    .filter(e -> Game.checkLoS(controlledTank.getPos(), e.getPos()))
                    .sorted((x, y)->(Vector2D.distance(controlledTank.getPos(), x.getPos())< Vector2D.distance(controlledTank.getPos(), y.getPos()))?-1:1)
                    .findFirst().get();
            currentWay = nearest;
            if(logAi){
                System.out.println("Ai:" + name + " new waypoint:" + currentWay);
            }
        }
        //next waypoint
        if(Vector2D.distance(currentWay.getPos(), controlledTank.getPos())< epsilonDistance){
            currentWay = currentWay.getRandomNeighbour();
            if(logAi){
                System.out.println("Ai:" + name + " new waypoint:" + currentWay);
            }
        }
        //rotate
        double angleToTurn =controlledTank.getBaseAngleVector().angle(currentWay.getPos().sub(controlledTank.position));
        if(Math.abs(angleToTurn) > epsilonAngle){
            if(angleToTurn > 0){
                controlledTank.command(Command.B_RIGHT);
            } else {
                controlledTank.command(Command.B_LEFT);
            }
        } else {
            controlledTank.command(Command.B_STOP_ROTATION);
        }
        //move
        if(Math.abs(angleToTurn) < maxMoveForwardAngleDiff){
            controlledTank.command(Command.B_FORWARD);
        } else {
            controlledTank.command(Command.B_STOP_MOVE);
        }
        if(logAi){
            g.setColor(Color.RED);
            g.drawString(currentWay.getId() + " " + angleToTurn, controlledTank.getPos().getXInt(), controlledTank.getPos().getYInt());
        }
        //point to target
        java.util.List<Tank> tanks = EGEngine.i().getDrawableObjects(Tank.class);
        tanks.remove(controlledTank);
        if(tanks.size() > 0){
            Tank nearestTank = tanks.stream().min(
                    (x,y)->(Vector2D.distance(x.getPos(), controlledTank.getPos())) < Vector2D.distance(y.getPos(), controlledTank.getPos())?-1 :1).get();
            double angleToRotate = controlledTank.getTurretAngleVector().angle(nearestTank.getPos().sub(currentWay.getPos()));
            if(Math.abs(angleToRotate) > epsilonTurretAngle){
                if(epsilonTurretAngle > 0){
                    controlledTank.command(Command.T_RIGHT);
                } else {
                    controlledTank.command(Command.T_LEFT);
                }
            } else {
                controlledTank.command(Command.T_STOP_ROTATION);
                controlledTank.command(Command.T_SHOT);
            }
        }

    }



    public void setControlledTank(Tank controlledTank) {
        this.controlledTank = controlledTank;
    }
}
