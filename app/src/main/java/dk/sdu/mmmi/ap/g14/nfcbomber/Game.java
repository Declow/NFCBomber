package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;

import dk.sdu.mmmi.ap.g14.nfcbomber.database.DbHelper;
import dk.sdu.mmmi.ap.g14.nfcbomber.database.tables.UserStatsContract;
import dk.sdu.mmmi.ap.g14.nfcbomber.util.StringUtil;

/**
 * Main GAME class - This implements the timer and "shake to drop bomb" functionality
 */

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
    private TextView bombInfo;
    private StringUtil timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        bombTime = getIntent().getIntExtra(Game.BOMB_TIME_EXTRA, 10);

        timerText = (TextView) findViewById(R.id.bomb_time);
        tHandler = new Handler();
        bombInfo = (TextView) findViewById(R.id.bomb_info);
        timeFormatter = new StringUtil();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_NORMAL);
        
        Log.v(TAG, "Timer val: " + bombTime);
        startTimer();
    }

    /**
     * Create a new runnable for the timer
     */
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            timerRunning = true;
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            if(elapsedTime >= bombTime*1000) {
                bombExplode();
            } else {
                tHandler.postDelayed(this, REFRESH_RATE);
            }
        }
    };

    /**
     * Update UI timer
     *
     * @param time float
     */
    private void updateTimer(float time) {
        String currentTime = timeFormatter.formatTime(time);
        timerText.setText(currentTime);
    }

    /**
     * Unused
     * Required from implements {@link SensorEventListener}
     * @param arg0
     * @param arg1
     */
    public void onAccuracyChanged(Sensor arg0, int arg1) { }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            double max = Math.max(Math.max(ax, ay), az);

            // int diff = (int) Math.abs(sum - oldsum);
            if(timerRunning && max > DEFUSE_THRESHOLD) {
                stopTimer();
            }
        }
    }

    /**
     * Start timer
     */
    private void startTimer() {
        startTime = System.currentTimeMillis();
        tHandler.removeCallbacks(timer);
        tHandler.postDelayed(timer,10);
    }

    /**
     * Stop timer
     */
    private void stopTimer() {
        tHandler.removeCallbacks(timer);
        updateTimer(System.currentTimeMillis() - startTime);
        timerRunning = false;

        bombInfo.setText(getResources().getString(R.string.game_time_was) + ": " + bombTime);

        writeToDb();
    }

    /**
     * Stop timer and set text DEAD
     */
    private void bombExplode() {
        stopTimer();
        timerText.setText("DEAD");
    }

    /**
     * Write current stats to the localDB
     */
    private void writeToDb() {
        DbHelper helper = new DbHelper(this.getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        Log.wtf(TAG, "" + new Date().getTime());

        ContentValues values = new ContentValues();
        values.put(UserStatsContract.UserStats.COLUMN_GAME_TIME, bombTime);
        values.put(UserStatsContract.UserStats.COLUMN_DATE, Long.toString(new Date().getTime()));
        values.put(UserStatsContract.UserStats.COLUMN_USER_STOP_TIME, (int) elapsedTime);

        db.insert(UserStatsContract.UserStats.TABLE_NAME, null, values);

        db.close();
        helper.close();
    }
}
