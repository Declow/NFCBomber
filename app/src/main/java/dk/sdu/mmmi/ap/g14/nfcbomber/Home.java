package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
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
}
