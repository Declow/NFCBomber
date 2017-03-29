package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Game extends AppCompatActivity {

    private final int REFRESH_RATE = 100;

    private TextView timerText;
    private Handler tHandler;
    private long startTime;
    private long elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timerText = (TextView) findViewById(R.id.bomb_time);
        tHandler = new Handler();
    }

    private void updateTimer(float time) {
        float secs = (long)(time/1000);
        float mins = (long)((time/1000)/60);

        /* Convert seconds to string */
        secs = secs % 60;
        String seconds = String.valueOf(secs);
        if(secs == 0){
            seconds = "00";
        }
        if(secs <10 && secs > 0){
            seconds = "0"+seconds;
        }

        /* Convert minutes to string */
        mins = mins % 60;
        String minutes = String.valueOf(mins);
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }

        /* Convert milliseconds */
        String milliseconds = String.valueOf((long)time);
        if(milliseconds.length()==2){
            milliseconds = "0"+milliseconds;
        }
        if(milliseconds.length()<=1){
            milliseconds = "00";
        }
        milliseconds = milliseconds.substring(milliseconds.length()-3, milliseconds.length()-2);
        timerText.setText(minutes+":"+seconds+":"+milliseconds);
    }

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            tHandler.postDelayed(this, REFRESH_RATE);
        }
    };

    public void startTimer(View v) {
        startTime = System.currentTimeMillis();
        tHandler.removeCallbacks(timer);
        tHandler.post(timer);
    }

    public void stopTimer(View v) {
        tHandler.removeCallbacks(timer);
        updateTimer(System.currentTimeMillis() - startTime);
    }
}
