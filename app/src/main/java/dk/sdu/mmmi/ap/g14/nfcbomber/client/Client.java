package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import android.util.Log;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBackWithArg;

/**
 * Created by declow on 4/5/17.
 */

public class Client implements CallBackWithArg {
    private ConnectionToServer server;
    private Socket socket;
    private LinkedBlockingQueue<Object> messages;

    private static final String TAG = "Client";


    public Client(InetAddress inet, int port) {
        try {
            socket = new Socket(inet.getHostAddress(), port);
            server = new ConnectionToServer(socket, this);
            messages = new LinkedBlockingQueue<>();

            Thread read = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Object message = messages.take();
                            Log.wtf(TAG, (String) message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            read.setDaemon(true);
            read.start();
        } catch (Exception e) {
            Log.wtf(TAG, "Client failed somehow :(");
        }
        this.server.read();
    }

    public void send(Object obj) {
        server.write(obj);
    }

    @Override
    public void CallBack(Object obj) {
        try {
            Log.wtf(TAG, "CallBack message: " + obj);
            messages.put(obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
