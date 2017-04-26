package dk.sdu.mmmi.ap.g14.nfcbomber;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import dk.sdu.mmmi.ap.g14.nfcbomber.database.DbHelper;
import dk.sdu.mmmi.ap.g14.nfcbomber.database.tables.UserStatsContract;
import dk.sdu.mmmi.ap.g14.nfcbomber.util.StringUtil;

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
        
        Log.wtf(TAG, "Timer val: " + bombTime);
        startTimer();
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

    private void updateTimer(float time) {
        String currentTime = timeFormatter.formatTime(time);
        timerText.setText(currentTime);
    }

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

        bombInfo.setText(getResources().getString(R.string.game_time_was) + ": " + bombTime);

        writeToDb();
    }

    private void bombExplode() {
        timerText.setText("DEAD");
    }

    private void writeToDb() {
        DbHelper helper = new DbHelper(this.getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserStatsContract.UserStats.COLUMN_GAME_TIME, bombTime);
        values.put(UserStatsContract.UserStats.COLUMN_USER_STOP_TIME, (int) elapsedTime);

        db.insert(UserStatsContract.UserStats.TABLE_NAME, null, values);

        db.close();
        helper.close();
    }
}
