package ru.ege.examples.TankiOnline;

import ru.ege.engine.Client;
import ru.ege.engine.EGEngine;
import ru.ege.engine.ResponseListener;

public class PlayerThread extends Thread implements ResponseListener {

    Client client;
    private String id;
    private Tank tank = null;
    public boolean interrupted = false;
    public long sleepBetweenUpdateMillis = 100;

    public PlayerThread(Client client, String id) {
        Leaderboard.i().add(id, 0);
        this.client = client;
        this.id = id;
        client.addResponseListener(this);
    }

    public void run() {
        System.out.println("Sending data to client thread started id:" + id);
        while (!interrupted) {
            //checkConnected
            if(!client.connected()){
                EGEngine.i().removeDrawableObject(tank);
            }
            //checkAlive
            if(tank == null || !tank.alive){
                tank = Game.spawnTank(id);
            }
            ///sendData
            String state = ServerMain.getServerStateJson();
            client.sendData(state);
            try {
                Thread.sleep(sleepBetweenUpdateMillis);
            } catch (InterruptedException ignore) {
            }
        }
        client.interrupt();
        System.out.println("Player id:" + id + " disconnected");
        EGEngine.i().removeDrawableObject(tank);
    }

    @Override
    public void onResponse(String response) {
        System.out.println("Client id:" + id + " data:" + response);
        try {
            if (response.startsWith("NAME")) {// ID VAsya
                String tmp[] = response.split(" ");
                id = tmp[1];
                changeName(id);
            } else {
                Command command = Command.valueOf(response);
                tank.command(command);
            }
        } catch (Exception e) {
            System.out.println("Wrong command:" + response);
        }

    }

    public void changeName(String newName){
        Leaderboard.i().changeName(id, newName);
        id = newName;
        tank.name = newName;
    }
}
