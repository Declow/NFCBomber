package dk.sdu.mmmi.ap.g14.nfcbomber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by declow on 4/5/17.
 */

public class ConnectionToClient {
    ObjectInputStream in;
    Socket socket;

    ConnectionToClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
    }
}
