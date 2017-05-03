package dk.sdu.mmmi.ap.g14.nfcbomber.interfaces;

import dk.sdu.mmmi.ap.g14.nfcbomber.network.NetObject;

/**
 * Interface to implement callback for messages
 * Used by the client to report when the server sends a message
 */

public interface IMessage {
    void message(NetObject obj);
}