package dk.sdu.mmmi.ap.g14.nfcbomber.server;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import dk.sdu.mmmi.ap.g14.nfcbomber.CallBack;

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

        /**
         * Start server thread and accept incoming
         * socket requests.
         */
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();

                        ConnectionToClient client = new ConnectionToClient(clientSocket, Server.this);
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

        /**
         * Thread handling message reads
         * Currenlt not in use!
         */
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
    }

    /**
     * Write msg to all clients
     *
     * @param obj
     */
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

    /**
     * Get client list size
     * @return int
     */
    public int clientSize() {
        synchronized (clientList) {
            return clientList.size();
        }
    }

    /**
     * In case the client sends data.
     * Currently not implemented
     * @return
     */
    synchronized private Object take() {
        try {
            return messages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Remove client from client list
     * @param client
     */
    public void removeClient(ConnectionToClient client) {
        synchronized (clientList) {
            clientList.remove(client);
            callBack.updateUI(clientSize());
        }

    }

}
