package ru.ege.examples.stuff;

import ru.ege.engine.Server;

/**
 * Created by Пользователь on 14.03.2017.
 */
public class ServerTest {
    public static void main(String [] args){
        Server.startServerThread();
        Server.addConnectionListener(c->{
            c.addResponseListener( r -> c.sendData(r));
        });
    }

}
