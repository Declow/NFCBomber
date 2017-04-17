package dk.sdu.mmmi.ap.g14.nfcbomber.server;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by declow on 4/5/17.
 */

public class ConnectionToClient {

    private static final String TAG = "ConnectionToClient";
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private LinkedBlockingQueue<Object> messages;
    private Server server;

    ConnectionToClient(Socket socket, final LinkedBlockingQueue<Object> messages, final Server server) throws IOException {
        this.socket = socket;
        this.messages = messages;
        this.server = server;

        Log.wtf(TAG, "Object created");
    }

    public void start() {
        Thread read = new Thread() {
            public void run() {
                while(true) {
                    try {
                        if (in == null) {
                            in = new ObjectInputStream(socket.getInputStream());
                        }
                        Object obj = in.readObject();
                        Log.wtf(TAG, "Got message");
                        messages.put(obj);
                    } catch (Exception e) {
                        Log.wtf(TAG, e.getMessage());
                        remove();
                        break;
                    }
                }
            }
        };

        read.setDaemon(true);
        read.start();
    }

    private void remove() {
        server.removeClient(this);
    }

    public void write(Object obj) {
        try {
            if (out == null) {
                out = new ObjectOutputStream(socket.getOutputStream());
                Log.wtf(TAG, "Created new outputstream!");
            }
            out.writeObject(obj);
        } catch (Exception e) {
            Log.wtf(TAG, "Unable to write to clients :(");
        }
    }
}
