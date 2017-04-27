package dk.sdu.mmmi.ap.g14.nfcbomber.database.tables;

import android.provider.BaseColumns;

/**
 * Created by declow on 4/22/17.
 */

public final class UserStatsContract {

    private UserStatsContract() {}

    public static class UserStats implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "User";

        /**
         * Column names
         */
        public static final String COLUMN_GAME_TIME = "gameTime";
        public static final String COLUMN_USER_STOP_TIME = "stopTime";
        public static final String COLUMN_DATE = "date";

        /**
         * Create table User
         */
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserStats.TABLE_NAME + " (" +
                        UserStats._ID + " INTEGER PRIMARY KEY," +
                        UserStats.COLUMN_GAME_TIME + " INTEGER," +
                        UserStats.COLUMN_DATE + " TEXT," +
                        UserStats.COLUMN_USER_STOP_TIME + " INTEGER);";
    }
}
