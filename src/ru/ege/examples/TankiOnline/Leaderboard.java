package ru.ege.examples.TankiOnline;

import ru.ege.engine.DrawableObject;
import ru.ege.engine.Vector2D;

import java.awt.*;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Leaderboard implements DrawableObject {
    private static Leaderboard ourInstance = new Leaderboard();

    public Map<String, Integer> players = new ConcurrentHashMap<>();
    Vector2D position = new Vector2D(50,300);
    int spacing = 20;
    public static Leaderboard i() {
        return ourInstance;
    }

    private Leaderboard() {
    }

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        g.setColor(Color.RED);
        g.drawString("Players", position.getXInt(), position.getYInt());
        int dy = spacing;
        for(String name : players.keySet()){
            if(players.get(name) <= 0){
                continue;
            }
            g.drawString(name + ":" + players.get(name), position.getXInt(), position.getYInt() + dy);
            dy += spacing;
        }
    }

    public void add(String name, int score){
        if(players.containsKey(name)){
            int x = players.get(name);
            x += score;
            players.put(name, x);
        } else {
            players.put(name, score);
        }
    }

    public void changeName(String oldName, String newName){
       Integer score = players.remove(oldName);
       score = (score!= null)?score:0;
       players.put(newName, score);
    }
}
