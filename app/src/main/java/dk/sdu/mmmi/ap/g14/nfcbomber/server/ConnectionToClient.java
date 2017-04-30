package dk.sdu.mmmi.ap.g14.nfcbomber.server;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Server-object that holds the connection from the server to the client.
 */

public class ConnectionToClient {

    private static final String TAG = "ConnectionToClient";
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private Server server;

    ConnectionToClient(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

        /**
         * In case we want to read from the client
         * Currently not in use!
         */
    public void start() {
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        if (in == null) {
                            in = new ObjectInputStream(socket.getInputStream());
                        }
                        Object obj = in.readObject();
                    } catch (Exception e) {
                        Log.e(TAG, "Client DC");
                        remove();
                        break;
                    }
                }
            }
        }.start();
    }

    private void remove() {
        server.removeClient(this);
    }

    /**
     * Write object to client
     *
     * @param obj
     */
    public void write(Object obj) {
        try {
            if (out == null) {
                out = new ObjectOutputStream(socket.getOutputStream());
            }
            out.writeObject(obj);
        } catch (Exception e) {
            Log.e(TAG, "Unable to write to clients :(");
        }
    }
}
