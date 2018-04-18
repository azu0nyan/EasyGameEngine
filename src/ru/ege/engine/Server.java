package ru.ege.engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    static final int PORT = Settings.PORT;
    static CopyOnWriteArrayList<ClientConnectionListener> connectionListeners= new CopyOnWriteArrayList<>();

    public static void startServerThread(){
        Thread serverThread = new Thread(() -> start());
        serverThread.start();
    }
    public static void addConnectionListener(ClientConnectionListener connectionListener){
            connectionListeners.add(connectionListener);
    }

    private static void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Started server on port:" + PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
                System.out.println("New connection accepted:" + socket);
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            Client connected = new Client(socket);
            for(ClientConnectionListener listener : connectionListeners){
                listener.onClientConnected(connected);
            }

        }
    }
}
