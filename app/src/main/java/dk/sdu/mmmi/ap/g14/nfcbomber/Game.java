package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Game extends AppCompatActivity {

    private static final String TAG = "Game";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        String gameType = intent.getStringExtra(Home.GAME_SETUP);

        setTitle("NFCBomber " + gameType);

        Log.v(TAG, gameType);
    }
}
