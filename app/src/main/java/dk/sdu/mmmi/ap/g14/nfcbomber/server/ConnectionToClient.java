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
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;
    private LinkedBlockingQueue<Object> messages;

    ConnectionToClient(Socket socket, final LinkedBlockingQueue<Object> messages) throws IOException {
        this.socket = socket;
        this.messages = messages;

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
                    } catch (IOException e) {
                        Log.wtf(TAG,e.getMessage());
                    } catch (ClassNotFoundException e) {
                        Log.wtf(TAG, e.getMessage());
                    } catch (InterruptedException e) {
                        Log.wtf(TAG, e.getMessage());
                    }
                }
            }
        };

        read.setDaemon(true);
        read.start();
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
