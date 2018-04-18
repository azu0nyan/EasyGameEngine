package ru.ege.examples.stuff;

import com.google.gson.Gson;
import ru.ege.engine.*;
import ru.ege.examples.TankiOnline.ServerState;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTest {
    public static Client c;
    public static void main(String [] args) throws UnknownHostException {
        EGEngine.i().startDrawingThread();
        Settings.NET_DEBUG = false;

        Client c = new Client(InetAddress.getByName("127.0.0.1"), 3222);
        WorldStateDrawer worldStateDrawer = new WorldStateDrawer();
        c.addResponseListener(worldStateDrawer);
        EGEngine.i().addDrawableObject(worldStateDrawer);
        Scanner s = new Scanner(System.in);
        String ss = s.nextLine();
        while (!"END".equals(ss)){
            c.sendData(ss);
            ss= s.nextLine();
        }
    }
}

class WorldStateDrawer implements ResponseListener, DrawableObject{
    ServerState serverState = null;
    @Override
    public void onResponse(String response) {
        Gson gson = new Gson();
        serverState = gson.fromJson(response, ServerState.class);

    }

    @Override
    public void drawAndUpdate(Graphics2D g, double dt) {
        if(serverState == null)return;
        serverState.tanks.stream().forEach(e->e.drawAndUpdate(g, dt));
        serverState.bullets.stream().forEach(e->e.drawAndUpdate(g, dt));
        serverState.targets.stream().forEach(e->e.drawAndUpdate(g, dt));
        serverState.walls.stream().forEach(e->e.drawAndUpdate(g, dt));
    }
}
