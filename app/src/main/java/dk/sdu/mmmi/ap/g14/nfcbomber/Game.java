package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Game extends AppCompatActivity implements SensorEventListener {

    private final int REFRESH_RATE = 10;
    private final int DEFUSE_THRESHOLD = 20;
    public static final String BOMB_TIME_EXTRA = "bomb_time";
    private static final String TAG = "GAME";

    private SensorManager sensorManager;
    private double ax, ay, az;
    private TextView timerText;
    private Handler tHandler;
    private long startTime;
    private long elapsedTime;
    private int bombTime;
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        bombTime = getIntent().getIntExtra(Game.BOMB_TIME_EXTRA, 10);

        timerText = (TextView) findViewById(R.id.bomb_time);
        tHandler = new Handler();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_NORMAL);
        
        Log.wtf(TAG, "Timer val: " + bombTime);
    }

    private void updateTimer(float time) {
        float secs = (long)(time/1000);
        float mins = (long)((time/1000)/60);

        /* Convert seconds to string */
        secs = secs % 60;
        String seconds = Integer.toString(Math.round(secs));
        if(secs == 0){
            seconds = "00";
        }
        if(secs <10 && secs > 0){
            seconds = "0"+seconds;
        }


        /* Convert minutes to string */
        mins = mins % 60;
        String minutes = Integer.toString(Math.round(mins));
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }

        /* Convert milliseconds */
        String milliseconds = Integer.toString((int)(time/10) % 100);

        if(milliseconds.length() == 1) {
            milliseconds = "0"+milliseconds;
        }
        timerText.setText(minutes+":"+seconds+":"+milliseconds);
    }

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            timerRunning = true;
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            if(elapsedTime >= bombTime*1000) {
                timerRunning = false;
                bombExplode();
            } else {
                tHandler.postDelayed(this, REFRESH_RATE);
            }
        }
    };

    public void onAccuracyChanged(Sensor arg0, int arg1) { }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            double max = Math.max(Math.max(ax, ay), az);

            // int diff = (int) Math.abs(sum - oldsum);
            if(max > DEFUSE_THRESHOLD) {
                stopTimer();
            }
        }
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        tHandler.removeCallbacks(timer);
        tHandler.postDelayed(timer,10);
    }

    private void stopTimer() {
        if(timerRunning) {
            tHandler.removeCallbacks(timer);
            updateTimer(System.currentTimeMillis() - startTime);
            timerRunning = false;
        }
    }

    private void bombExplode() {
        timerText.setText("DEAD");
    }

    public void onStartTimer(View v) {
        startTimer();
    }

    public void onStopTimer(View v) {
        stopTimer();
    }
}
