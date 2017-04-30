package dk.sdu.mmmi.ap.g14.nfcbomber;

/**
 * Interface to implement callback for messages
 * Used by the client to report when the server sends a message
 */

public interface CallBackConnectionTo {
    void message(Object obj);
}