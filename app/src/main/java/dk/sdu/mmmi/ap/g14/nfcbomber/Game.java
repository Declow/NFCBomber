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
        setContentView(R.layout.activity_host);

        Intent intent = getIntent();
        String gameType = intent.getStringExtra(Home.GAME_SETUP);
        if (gameType.equals(Home.GAME_HOST))
            setTitle(getResources().getString(R.string.app_name) + getResources().getString(R.string.app_type_host));
        else
            setTitle(getResources().getString(R.string.app_name) + getResources().getString(R.string.app_type_client));

        Log.v(TAG, gameType);
    }
}
