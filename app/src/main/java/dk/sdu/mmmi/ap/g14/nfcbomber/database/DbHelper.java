package dk.sdu.mmmi.ap.g14.nfcbomber.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

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
}
