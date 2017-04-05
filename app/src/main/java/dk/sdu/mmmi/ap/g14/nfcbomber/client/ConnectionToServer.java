package dk.sdu.mmmi.ap.g14.nfcbomber.client;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBack;
import dk.sdu.mmmi.ap.g14.nfcbomber.CallBackWithArg;

/**
 * Created by declow on 4/5/17.
 */

public class ConnectionToServer {
    private static final String TAG = "CONNECTION_TO_SERVER";

    ObjectInputStream in;
    Socket socket;

    public ConnectionToServer(Socket socket, final CallBackWithArg callBack) {
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());

            Thread read = new Thread() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            Object obj = in.readObject();

                            callBack.CallBack(obj);

                            Log.wtf(TAG, (String) obj);
                        } catch (IOException e) {
                            Log.wtf(TAG, e.getMessage());
                        } catch (ClassNotFoundException e) {
                            Log.wtf(TAG, e.getMessage());
                        }
                    }
                }
            };
            read.setDaemon(true);
            read.start();

        } catch (Exception e) {
            Log.wtf(TAG, "Unable to connect to server :(");
        }
    }

}
