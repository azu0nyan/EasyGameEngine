package ru.ege.examples.TankiOnline;

import com.google.gson.Gson;
import ru.ege.engine.EGEngine;
import ru.ege.engine.Server;
import ru.ege.engine.Settings;
import ru.ege.examples.stuff.DebugObjects;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class ServerMain {
    static int bots = 0;

    public static void main(String[] args) {
        Settings.NET_DEBUG = false;
        EGEngine.i().startDrawingThread();
        DebugObjects.addDebugObjects(EGEngine.i());
        Game.init();
        Server.startServerThread();
        Server.addConnectionListener(client -> {
            Random r = new Random();
            int id = r.nextInt();
            PlayerThread playerThread = new PlayerThread(client, String.valueOf(id));
            System.out.println("New player created:" + id);
            playerThread.start();
        });
        EGEngine.i().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse x:" + e.getX() + " y:" + e.getY());
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
        });
    }


    public static String getServerStateJson() {
        Gson gson = new Gson();
        ServerState serverState = ServerState.getState();
        String json = gson.toJson(serverState);
        return json;
    }

}
