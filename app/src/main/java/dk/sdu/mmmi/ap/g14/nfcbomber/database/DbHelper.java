package dk.sdu.mmmi.ap.g14.nfcbomber.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import dk.sdu.mmmi.ap.g14.nfcbomber.database.objects.HighscoreItem;
import dk.sdu.mmmi.ap.g14.nfcbomber.database.tables.UserStatsContract;

/**
 * Created by declow on 4/22/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "UserStats";
    private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserStatsContract.UserStats.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(UserStatsContract.UserStats.SQL_UPGRADE_ENTRIES);
        }
    }

    public ArrayList<HighscoreItem> readDb() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + UserStatsContract.UserStats.TABLE_NAME, null);

        ArrayList<HighscoreItem> highscoreList = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    int gameTime = c.getInt(c.getColumnIndex(UserStatsContract.UserStats.COLUMN_GAME_TIME));
                    Date d = new Date(c.getInt(c.getColumnIndex(UserStatsContract.UserStats.COLUMN_DATE)));
                    int userStopTime = c.getInt(c.getColumnIndex(UserStatsContract.UserStats.COLUMN_USER_STOP_TIME));
                    HighscoreItem hi = new HighscoreItem(userStopTime, gameTime, d);
                    highscoreList.add(hi);
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();
        return highscoreList;
    }
}
