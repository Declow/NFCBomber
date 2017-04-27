package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    /**
     * Show host lobby activity on click
     *
     * @param v
     */
    public void onHost(View v) {
        Intent intent = new Intent(this, Lobby.class);
        intent.putExtra(GAME_SETUP, GAME_HOST);
        startActivity(intent);
    }

    /**
     * Show scoreboard activity on click
     * @param v
     */
    public void onStats(View v) {
        Intent intent = new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }

    /**
     * Check if nfc is available
     */
    private void nfc() {
        //NFC checker
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.v(TAG, "NFC available");
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

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
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
