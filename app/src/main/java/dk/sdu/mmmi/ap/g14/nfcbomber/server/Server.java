package dk.sdu.mmmi.ap.g14.nfcbomber.server;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import dk.sdu.mmmi.ap.g14.nfcbomber.server.ConnectionToClient;

/**
 * Created by declow on 4/5/17.
 */

public class Server {
    private static final String TAG = "SERVER";

    private final ArrayList<ConnectionToClient> clientList = new ArrayList<>();
    private final ArrayList<Socket> clientList2 = new ArrayList<>();

    private ServerSocket serverSocket;
    private LinkedBlockingQueue<Object> messages;

    public Server(final int port) {
        messages = new LinkedBlockingQueue<>();

        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        clientList2.add(clientSocket);
                        ConnectionToClient client = new ConnectionToClient(clientSocket, messages);
                        clientList.add(client);
                        client.start();
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

        Thread sendToClients = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                        String message = "/0 Send Message";
                        Log.wtf(TAG, "Client size: " + clientSize());
                        sendToEveryConnectedDevice(message);
                    } catch (InterruptedException e) {
                        Log.wtf(TAG, e.getMessage());
                    }
                }
            }
        };

        sendToClients.setDaemon(true);
        sendToClients.start();

    }

    public void sendToEveryConnectedDevice(Object obj) {
        synchronized (clientList) {
            for (ConnectionToClient client : clientList) {
                Log.wtf(TAG, " client size: " + Integer.toString(clientList.size()) );
                client.write(obj);
            }
            clientList.notify();
        }
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

}
