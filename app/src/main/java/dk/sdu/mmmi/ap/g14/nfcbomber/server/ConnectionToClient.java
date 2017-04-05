package dk.sdu.mmmi.ap.g14.nfcbomber.server;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by declow on 4/5/17.
 */

public class ConnectionToClient {
    private static final String TAG = "ConnectionToClient";
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket socket;

    ConnectionToClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

 /*       Thread read = new Thread() {
            public void run() {
                while(true) {
                    try {
                        Object obj = in.readObject();

                    } catch (IOException e) {
                        Log.wtf(TAG,e.getMessage());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        read.setDaemon(true);
        read.start();
*/


    }

    public void write(Object obj) {
        try {
            out.writeObject(obj);
        } catch (Exception e) {
            Log.wtf(TAG, "Unable to write to clients :(");
        }
    }
}
