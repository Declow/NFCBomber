package dk.sdu.mmmi.ap.g14.nfcbomber.server;

import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBack;
import dk.sdu.mmmi.ap.g14.nfcbomber.server.ConnectionToClient;

/**
 * Created by declow on 4/5/17.
 */

public class Server {
    private static final String TAG = "SERVER";

    private final ArrayList<ConnectionToClient> clientList = new ArrayList<>();

    private ServerSocket serverSocket;
    private LinkedBlockingQueue<Object> messages;

    private CallBack callBack;

    public Server(final int port, final CallBack callBack) {
        this.messages = new LinkedBlockingQueue<>();
        this.callBack = callBack;
        final Server s = this;
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();

                        ConnectionToClient client = new ConnectionToClient(clientSocket, messages, s);
                        clientList.add(client);
                        client.start();
                        callBack.updateUI(clientSize());
                    }


                } catch (IOException e) {
                    Log.wtf(TAG, "Unable To create server socket :(");
                    Log.wtf(TAG, e.getMessage());
                }
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Thread handleMessages = new Thread() {
            public void run() {
                while (true) {
                    Object message = take();
                    if (message != null) {
                        Log.wtf(TAG, "Got message from Client");
                        Log.wtf(TAG, (String) message);
                    }
                }
            }
        };

        handleMessages.setDaemon(true);
        handleMessages.start();

        //testMethod();
    }

    public void sendToEveryConnectedDevice(final Object obj) {
        Thread t = new Thread() {
            @Override
            public void run() {
                synchronized (clientList) {
                    for (ConnectionToClient client : clientList) {
                        client.write(obj);
                    }
                    clientList.notify();
                }
            }
        };
        t.start();
    }

    public int clientSize() {
        synchronized (clientList) {
            return clientList.size();
        }
    }

    synchronized private Object take() {
        try {
            return messages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeClient(ConnectionToClient client) {
        synchronized (clientList) {
            clientList.remove(client);
            callBack.updateUI(clientSize());
        }

    }

}
