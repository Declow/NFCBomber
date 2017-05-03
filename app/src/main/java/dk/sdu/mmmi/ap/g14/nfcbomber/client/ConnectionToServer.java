package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dk.sdu.mmmi.ap.g14.nfcbomber.interfaces.IMessage;
import dk.sdu.mmmi.ap.g14.nfcbomber.network.NetObject;

/**
 * Client-object responsible for the connection to the server.
 */
public class ConnectionToServer {
    private static final String TAG = "CONNECTION_TO_SERVER";

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Socket socket;
    private IMessage callBack;

    public ConnectionToServer(final Socket socket, final IMessage callBack) {
        this.socket = socket;
        this.callBack = callBack;
    }

    /**
     * Creates a thread which reads from the server
     * and puts the message into the message queue
     * in the client
     */
    public void read() {
        new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        if (in == null) {
                            in = new ObjectInputStream(ConnectionToServer.this.socket.getInputStream());
                            Log.v(TAG, "Created input stream!");
                        }
                        Object obj = in.readObject();
                        Log.v(TAG, "Got a message");
                        callBack.message((NetObject) obj);
                    } catch (IOException e) {
                        Log.e(TAG, "IOException is: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        Log.v(TAG, "Class not found " + e.getMessage());
                    }
                }
            }
        }.start();
    }
}
