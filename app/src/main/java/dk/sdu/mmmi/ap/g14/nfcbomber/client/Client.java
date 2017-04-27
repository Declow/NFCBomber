package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import android.util.Log;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBackConnectionTo;
import dk.sdu.mmmi.ap.g14.nfcbomber.LobbyClient;
import dk.sdu.mmmi.ap.g14.nfcbomber.network.NetObject;

/**
 * Created by declow on 4/5/17.
 */

public class Client implements CallBackConnectionTo {
    private ConnectionToServer server;
    private Socket socket;
    private LinkedBlockingQueue<Object> messages;
    private LobbyClient lobby;

    private static final String TAG = "Client";

    /**
     * Creates a new client with an inet address,
     * server port and the client activity
     *
     * @param inet
     * @param port
     * @param lobby
     */
    public Client(InetAddress inet, int port, LobbyClient lobby) {
        this.lobby = lobby;
        try {
            socket = new Socket(inet.getHostAddress(), port);
            server = new ConnectionToServer(socket, this);
            messages = new LinkedBlockingQueue<>();

            /**
             * Start a thread which reads a message
             * form the message queue
             * and calls the right method
             */
            Thread read = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Object message = messages.take();
                            determineState(message);
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

    /**
     *
     * @param obj
     */
    @Override
    public void message(Object obj) {
        try {
            Log.v(TAG, "CallBack message: " + obj);
            messages.put(obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes the object from the server
     * and uses an enum to determine
     * what to do based on the enum
     *
     * @param obj -> NetObject
     */
    private void determineState(Object obj) {

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
