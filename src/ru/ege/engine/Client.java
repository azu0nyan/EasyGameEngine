package ru.ege.engine;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class Client {
    ResponseProcessor responseProcessorThread;
    RequestSender requestSenderThread;

    public Client(InetAddress address, int port){
        connect(address, port);
    }

    public Client(Socket socket){
        startThreads(socket);
    }

    public void connect(InetAddress address, int port){
        try {
            System.out.println("Connecting to " + address + " port:" + port);
            Socket serverSocket = new Socket(address, port);
            startThreads(serverSocket);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + address);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + address);
        }
    }

    private void startThreads(Socket socket){
        System.out.println("Starting response processing thread");
        responseProcessorThread = new ResponseProcessor(socket);
        responseProcessorThread.start();
        System.out.println("Starting request sender thread");
        requestSenderThread = new RequestSender(socket);
        requestSenderThread.start();
    }

    public void sendData(String str) {
        requestSenderThread.sendData(str);
    }

    public void addResponseListener(ResponseListener listener){
        responseProcessorThread.addResponseListener(listener);
    }

    public boolean connected(){
        return requestSenderThread.isConnected() && responseProcessorThread.isConnected();
    }
    public void interrupt(){
        requestSenderThread.interrupt();
        responseProcessorThread.interrupt();
    }
}



