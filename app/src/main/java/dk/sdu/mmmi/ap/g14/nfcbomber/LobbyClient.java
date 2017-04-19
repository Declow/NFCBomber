package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

import dk.sdu.mmmi.ap.g14.nfcbomber.client.Client;

/**
 * Created by declow on 3/29/17.
 */

public class LobbyClient extends AppCompatActivity {

    private static final String TAG = "GAME_LOBBY_CLIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lobby_client);

    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
            getIntent().setAction("");
        }
    }

    private void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        InetAddress inet = null;
        // record 0 contains the MIME type, record 1 is the AAR, if present
        byte[] bytes = msg.getRecords()[0].getPayload();
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
            ObjectInput in = null;
            in = new ObjectInputStream(bin);
            Object o = in.readObject();
            inet = (InetAddress) o;
            Log.wtf(TAG, inet.getHostAddress());
        } catch (Exception e) {
            Log.wtf(TAG, "reading InetAddress failed :(");
        }

        if (inet != null) {
            connectToHost(inet, this);
        }
    }

    private void connectToHost(final InetAddress inet, final LobbyClient lobby) {
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Client c = new Client(inet, getApplicationContext().getResources().getInteger(R.integer.port), lobby);
            }
        });

        clientThread.setDaemon(true);
        clientThread.start();
    }

    public void startGame(final int timer) {

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.putExtra(Game.BOMB_TIME_EXTRA, timer);
                startActivity(intent);
            }
        });
    }
}
