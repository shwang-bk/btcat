package studio.bluekitten.backtestingcat.core.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import studio.bluekitten.backtestingcat.util.TestActivity;

// 繼承自 "Android 6 Tutorial 第三堂（3）使用Android內建的SQLite資料庫"
// http://www.codedata.com.tw/mobile/android-6-tutorial-3-3/

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "backtesting.db";
    public static final int VERSION = 2;
    private static SQLiteDatabase database;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ReportDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion == 1){
            sqLiteDatabase.execSQL(
                    "ALTER TABLE "+ReportDAO.TABLE_NAME+" ADD "+ReportDAO.TARGETNAME_COLUMN+" TEXT");
            sqLiteDatabase.execSQL(
                    "ALTER TABLE "+ReportDAO.TABLE_NAME+" ADD "+ReportDAO.YEARSOFFSET_COLUMN+" REAL");
            sqLiteDatabase.execSQL(
                    "ALTER TABLE "+ReportDAO.TABLE_NAME+" ADD "+ReportDAO.TIMINGSIGNALS_COLUMN+" TEXT");
            sqLiteDatabase.execSQL(
                    "ALTER TABLE "+ReportDAO.TABLE_NAME+" ADD "+ReportDAO.TIMESTAMP_COLUMN+" TEXT");
        }
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DBHelper(context, DATABASE_NAME,
                    null, VERSION).getWritableDatabase();
        }

        return database;
    }
}
