package ru.ege.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResponseProcessor extends Thread {
    boolean connected = true;
    boolean interrupted = false;

    Socket serverSocket;
    List<ResponseListener> receivers = new CopyOnWriteArrayList<>();

    public void addResponseListener(ResponseListener receiver){
        receivers.add(receiver);
    }
    public ResponseProcessor(Socket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader( new InputStreamReader(serverSocket.getInputStream()));
            String outputFromServer="";
            while((outputFromServer=in.readLine())!= null && !interrupted){
                if(Settings.NET_DEBUG){
                    System.out.println("received:" + outputFromServer);
                }
                for(ResponseListener receiver : receivers){
                    receiver.onResponse(outputFromServer);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server output reader, disconnected from server");
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }
    public void interrupt(){
        interrupted = true;

    }
}
