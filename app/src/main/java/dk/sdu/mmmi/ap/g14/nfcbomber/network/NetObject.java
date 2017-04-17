package dk.sdu.mmmi.ap.g14.nfcbomber.network;

import java.io.Serializable;

/**
 * Created by declow on 4/17/17.
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
