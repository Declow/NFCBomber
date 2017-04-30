package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * The activity to implement the home screen (first screen in app).
 */
public class Home extends AppCompatActivity {

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
     * Check if NFC is available
     */
    private void nfc() {
        //NFC checker
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.v(TAG, "NFC available");
        }
    }
}
