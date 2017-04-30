package dk.sdu.mmmi.ap.g14.nfcbomber.network;

import java.io.Serializable;

/**
 * Net-object to be sent from the server to the client om game start.
 * Usage example (as used in {@link dk.sdu.mmmi.ap.g14.nfcbomber.Lobby)}:
 *      new NetObject([BombTimer], [GameState]);
 */
public class NetObject implements Serializable {
    private Object content;
    private Com type;

    public NetObject (Object obj, Com type) {
        this.content = obj;
        this.type = type;
    }

    public NetObject(Com type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public Com getType() {
        return type;
    }
}
