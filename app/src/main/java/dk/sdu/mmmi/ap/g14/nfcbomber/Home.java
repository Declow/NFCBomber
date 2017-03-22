package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    public static final String GAME_SETUP = "gameSetup";
    public static final String GAME_HOST = "host";
    public static final String GAME_CLIENT = "client";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onHost(View v) {
        Intent intent = new Intent(this, Lobby.class);
        intent.putExtra(GAME_SETUP, GAME_HOST);
        startActivity(intent);
    }

    public void onClient(View v) {
        Intent intent = new Intent(this, Lobby.class);
        intent.putExtra(GAME_SETUP, GAME_CLIENT);
        startActivity(intent);
    }
}
