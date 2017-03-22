package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    public static final String GAME_SETUP = "gameSetup";

    public enum gameType {
        host, client
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onHost(View v) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(GAME_SETUP, gameType.host);
        startActivity(intent);
    }

    public void onJoin(View v) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(GAME_SETUP, gameType.client);
        startActivity(intent);
    }
}
