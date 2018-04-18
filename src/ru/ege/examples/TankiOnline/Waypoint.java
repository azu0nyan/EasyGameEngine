package ru.ege.examples.TankiOnline;

import ru.ege.engine.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by user on 24.03.2017.
 */
public class Waypoint {
    public static List<Waypoint>  waypoints = new CopyOnWriteArrayList<>();

    int id;
    Vector2D pos;
    List<Integer> neighbours;

    public Waypoint(Vector2D pos) {
        this.pos = pos;
        waypoints.add(this);
        id = waypoints.indexOf(this);
        neighbours = new ArrayList<>();
    }

    public Waypoint(Vector2D pos, int [] neighbours) {
        this.pos = pos;
        waypoints.add(this);
        id = waypoints.indexOf(this);
        this.neighbours = new ArrayList<>();
        for(int i : neighbours){
            this.neighbours.add(i);
        }
    }

    public List<Waypoint> getNeighbours(){
        return neighbours.stream().map(e ->waypoints.get(e)).collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public Vector2D getPos() {
        return pos;
    }

    public static void init(){
        new Waypoint(new Vector2D(350, 150), new int[]{1,3});//0
        new Waypoint(new Vector2D(150, 150), new int[]{0,2});//1
        new Waypoint(new Vector2D(150, 750), new int[]{1});//2
        new Waypoint(new Vector2D(350, 550), new int[]{0,4});//3
        new Waypoint(new Vector2D(600, 550), new int[]{3, 5, 12});//4
        new Waypoint(new Vector2D(900, 550), new int[]{4, 6, 13});//5
        new Waypoint(new Vector2D(1150, 550), new int[]{7,9,11});//6
        new Waypoint(new Vector2D(1150, 150), new int[]{6,8, 9});//7
        new Waypoint(new Vector2D(1450, 150), new int[]{7,6,9,10});//8
        new Waypoint(new Vector2D(1450, 550), new int[]{6, 7, 8, 10, 11});//9
        new Waypoint(new Vector2D(1450, 750), new int[]{6, 9, 11});//10
        new Waypoint(new Vector2D(1150, 750), new int[]{6, 9, 10});//11
        new Waypoint(new Vector2D(600, 750), new int[]{4});//12
        new Waypoint(new Vector2D(900, 750), new int[]{5});//13
    }

    public Waypoint getRandomNeighbour() {
        if(neighbours == null || neighbours.size() == 0){
            System.err.println("No neighbours");
            return null;
        }
        Random r = new Random();
        return waypoints.get(neighbours.get(r.nextInt(neighbours.size())));
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "id=" + id +
                ", pos=" + pos +
                ", neighbours=" + neighbours +
                '}';
    }
}
