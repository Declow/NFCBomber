package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.InetAddress;

public class Home extends AppCompatActivity {

    public static final String GAME_SETUP = "gameSetup";
    public static final String GAME_HOST = "host";
    public static final String GAME_CLIENT = "client";
    private static final String TAG = "HOME";

    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nfc();
    }

    public void onHost(View v) {
        Intent intent = new Intent(this, Lobby.class);
        intent.putExtra(GAME_SETUP, GAME_HOST);
        startActivity(intent);
    }

    public void onClient(Intent intent) {
        startActivity(intent);
    }

    private void nfc() {
        //NFC checker
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.wtf(TAG, "NFC available");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.wtf(TAG, "onResume called");
        Log.wtf(TAG, getIntent().getAction());
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.wtf(TAG, "Got message :D");
            processIntent(getIntent());
        }
    }

    void processIntent(Intent intent) {
//        Intent myIntent = new Intent(this, Lobby.class);
//        myIntent.putExtra(GAME_SETUP, GAME_CLIENT);
//        //myIntent.putExtra();

//        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
//                NfcAdapter.EXTRA_NDEF_MESSAGES);
//        // only one message sent during the beam
//        NdefMessage msg = (NdefMessage) rawMsgs[0];
//        InetAddress inet = null;
//        // record 0 contains the MIME type, record 1 is the AAR, if present
//        byte[] bytes = msg.getRecords()[0].getPayload();
//        try {
//            ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
//            ObjectInput in = null;
//            in = new ObjectInputStream(bin);
//            Object o = in.readObject();
//            inet = (InetAddress) o;
//            Log.wtf(TAG, inet.getHostAddress());
//        } catch (Exception e) {
//            Log.wtf(TAG, "reading InetAddress failed :(");
//        }

//        onClient(myIntent);

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage)rawMsgs[0];
        String text = new String(msg.getRecords()[0].getPayload());
        Log.wtf(TAG, text);
    }
}
