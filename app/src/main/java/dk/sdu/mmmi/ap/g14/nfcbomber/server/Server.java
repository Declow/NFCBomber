package dk.sdu.mmmi.ap.g14.nfcbomber.server;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import dk.sdu.mmmi.ap.g14.nfcbomber.server.ConnectionToClient;

/**
 * Created by declow on 4/5/17.
 */

public class Server {
    private static final String TAG = "SERVER";

    private ArrayList<ConnectionToClient> clientList;
    private ServerSocket serverSocket;

    public Server(final int port) {
        clientList = new ArrayList<>();

        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        clientList.add(new ConnectionToClient(clientSocket));
                    }


                } catch (IOException e) {
                    Log.wtf(TAG, "Unable To create server socket :(");
                }
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    public void sendToEveryConnectedDevice(Object obj) {
        for (ConnectionToClient client : clientList) {
            client.write(obj);
        }
    }

}
