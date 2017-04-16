package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBack;
import dk.sdu.mmmi.ap.g14.nfcbomber.CallBackWithArg;

/**
 * Created by declow on 4/5/17.
 */

public class ConnectionToServer {
    private static final String TAG = "CONNECTION_TO_SERVER";

    ObjectInputStream in;
    ObjectOutputStream out;
    private final Socket socket;
    CallBackWithArg callBack;

    public ConnectionToServer(final Socket socket, final CallBackWithArg callBack) {
        this.socket = socket;
        this.callBack = callBack;
    }

    public void read() {
        Thread read = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        if (in == null) {
                            in = new ObjectInputStream(socket.getInputStream());
                            Log.wtf(TAG, "Created input stream!");
                        }
                        Object obj = in.readObject();
                        Log.wtf(TAG, "Got a message");
                        callBack.CallBack(obj);
                    } catch (IOException e) {
                        Log.wtf(TAG, "IOException is: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        read.start();
    }

    public void write(Object obj) {
        try {
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
