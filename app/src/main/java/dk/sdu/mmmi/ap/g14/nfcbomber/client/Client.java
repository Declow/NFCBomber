package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import java.net.InetAddress;
import java.net.Socket;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBackWithArg;

/**
 * Created by declow on 4/5/17.
 */

public class Client implements CallBackWithArg {
    private ConnectionToServer server;
    private Socket socket;


    public Client(InetAddress inet, int port) {
        try {
            socket = new Socket(inet, port);
            server = new ConnectionToServer(socket, this);

            Thread read = new Thread() {
                @Override
                public void run() {
                    while (true) {

                    }
                }
            };
            read.setDaemon(true);
            read.start();


        } catch (Exception e) {

        }
    }

    @Override
    public void CallBack(Object obj) {
        
    }
}
