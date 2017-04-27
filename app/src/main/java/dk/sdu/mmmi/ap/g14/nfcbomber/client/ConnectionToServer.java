package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBackConnectionTo;

/**
 * Created by declow on 4/5/17.
 */

public class ConnectionToServer {
    private static final String TAG = "CONNECTION_TO_SERVER";

    ObjectInputStream in;
    ObjectOutputStream out;
    private final Socket socket;
    CallBackConnectionTo callBack;

    public ConnectionToServer(final Socket socket, final CallBackConnectionTo callBack) {
        this.socket = socket;
        this.callBack = callBack;
    }

    /**
     * Creates a thread which reads from the server
     * and puts the message into the message queue
     * in the client
     */
    public void read() {
        Thread read = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        if (in == null) {
                            in = new ObjectInputStream(socket.getInputStream());
                            Log.v(TAG, "Created input stream!");
                        }
                        Object obj = in.readObject();
                        Log.v(TAG, "Got a message");
                        callBack.message(obj);
                    } catch (IOException e) {
                        Log.e(TAG, "IOException is: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        read.start();
    }
}
