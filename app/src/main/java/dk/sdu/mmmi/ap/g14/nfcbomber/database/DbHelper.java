package dk.sdu.mmmi.ap.g14.nfcbomber.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

import dk.sdu.mmmi.ap.g14.nfcbomber.database.objects.HighscoreItem;
import dk.sdu.mmmi.ap.g14.nfcbomber.database.tables.UserStatsContract;

/**
 * Created by declow on 4/22/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserStats";
    private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDb(db, 0, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDb(db, oldVersion, newVersion);
    }

    private void updateDb(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL(UserStatsContract.UserStats.SQL_CREATE_ENTRIES);
        }
    }

    /**
     * Reads every scores from the database
     *
     * @return ArrayList<HighscoreItem>
     */
    public ArrayList<HighscoreItem> readDb() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + UserStatsContract.UserStats.TABLE_NAME, null);

        ArrayList<HighscoreItem> highscoreList = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    int gameTime = c.getInt(c.getColumnIndex(UserStatsContract.UserStats.COLUMN_GAME_TIME));
                    long i = Long.parseLong(c.getString(c.getColumnIndex(UserStatsContract.UserStats.COLUMN_DATE)));
                    Date date = new Date(i);
                    int userStopTime = c.getInt(c.getColumnIndex(UserStatsContract.UserStats.COLUMN_USER_STOP_TIME));

                    highscoreList.add(new HighscoreItem(userStopTime, gameTime, date));
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();
        return highscoreList;
    }
}
