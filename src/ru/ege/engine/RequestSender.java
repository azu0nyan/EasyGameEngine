package ru.ege.engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RequestSender extends Thread {
    Socket serverSocket;
    BlockingDeque<String> dataToSend = new LinkedBlockingDeque<>();
    boolean connected = true;
    boolean interrupted = false;

    public RequestSender(Socket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void run(){
        PrintWriter out;
        try {
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            String data;

            while (!interrupted) {
                data = dataToSend.takeFirst();
                out.println(data);
                if(Settings.NET_DEBUG){
                    System.out.println("Sended:" + data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Server request sender disconnected");
        connected = false;
    }

    public  void sendData(String data){
        if(Settings.NET_DEBUG){
            System.out.println("Adding data to queue:" + data);
        }
        dataToSend.add(data);
    }

    public boolean isConnected() {
        return connected;
    }

    public void interrrupt(){
        interrupted = true;
    }
}
