package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import android.util.Log;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBackWithArg;
import dk.sdu.mmmi.ap.g14.nfcbomber.LobbyClient;
import dk.sdu.mmmi.ap.g14.nfcbomber.network.Com;
import dk.sdu.mmmi.ap.g14.nfcbomber.network.NetObject;

/**
 * Created by declow on 4/5/17.
 */

public class Client implements CallBackWithArg {
    private ConnectionToServer server;
    private Socket socket;
    private LinkedBlockingQueue<Object> messages;
    private LobbyClient lobby;

    private static final String TAG = "Client";


    public Client(InetAddress inet, int port, LobbyClient lobby) {
        this.lobby = lobby;
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
                            determinState(message);
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

    @Override
    public void message(Object obj) {
        try {
            Log.v(TAG, "CallBack message: " + obj);
            messages.put(obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void determinState(Object obj) {

        NetObject com = (NetObject) obj;
        switch (com.getType()) {
            case START_GAME:
                Log.wtf(TAG, "Start game plz " + (int) com.getContent());
                lobby.startGame((int) com.getContent());
                break;
            case GAME_END:
                Log.wtf(TAG, "Stop game plz");
                break;
        }
    }
}
