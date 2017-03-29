package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Game extends AppCompatActivity {

    private final int REFRESH_RATE = 100;

    private TextView timerText;
    private Handler tHandler;
    private long startTime;
    private long elapsedTime;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    private void updateTimer(float time) {
        float secs = (long)(time/1000);
        float mins = (long)((time/1000)/60);
        float hrs = (long)(((time/1000)/60)/60);

    }

}
