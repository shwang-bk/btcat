package studio.bluekitten.backtestingcat.core.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import studio.bluekitten.backtestingcat.core.Report;

// 繼承自 "Android 6 Tutorial 第三堂（3）使用Android內建的SQLite資料庫"
// http://www.codedata.com.tw/mobile/android-6-tutorial-3-3/

public class ReportDAO {
    public static final String TABLE_NAME = "REPORT";

    public static final String KEY_ID = "_id";

    public static final String TARGETNAME_COLUMN = "TARGETNAME";
    public static final String YEARSOFFSET_COLUMN = "YEARSOFFSET";
    public static final String TIMINGSIGNALS_COLUMN = "TIMINGSIGNALS";
    public static final String CHARTLINE_COLUMN = "CHARTLINE";
    public static final String PROFIT_COLUMN = "PROFIT";
    public static final String GROSSPROFIT_COLUMN = "GROSSPROFIT";
    public static final String GROSSLOSS_COLUMN = "GROSSLOSS";
    public static final String TRADECOUNT_COLUMN = "TRADECOUNT";
    public static final String PROFITCOUNT_COLUMN = "PROFITCOUNT";
    public static final String LOSSCOUNT_COLUMN = "LOSSCOUNT";
    public static final String MAXCONTINUEPROFIT_COLUMN = "MAXCONTINUEPROFIT";
    public static final String MAXCONTINUELOSS_COLUMN = "MAXCONTINUELOSS";
    public static final String MAXCONTINUELOSSTRADE_COLUMN = "MAXCONTINUELOSSTRADE";
    public static final String NOTE_COLUMN = "NOTE";
    public static final String TIMESTAMP_COLUMN = "TIMESTAMP";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TARGETNAME_COLUMN + " TEXT, " +
                    YEARSOFFSET_COLUMN + " REAL, " +
                    TIMINGSIGNALS_COLUMN + " TEXT, " +
                    CHARTLINE_COLUMN + " TEXT, " +
                    PROFIT_COLUMN + " INTEGER, " +
                    GROSSPROFIT_COLUMN + " INTEGER, " +
                    GROSSLOSS_COLUMN + " INTEGER, " +
                    TRADECOUNT_COLUMN + " INTEGER, " +
                    PROFITCOUNT_COLUMN + " INTEGER, " +
                    LOSSCOUNT_COLUMN + " INTEGER, " +
                    MAXCONTINUEPROFIT_COLUMN + " INTEGER, " +
                    MAXCONTINUELOSS_COLUMN + " INTEGER, " +
                    MAXCONTINUELOSSTRADE_COLUMN + " INTEGER, " +
                    TIMESTAMP_COLUMN + " TEXT, " +
                    NOTE_COLUMN + " TEXT)";

    private SQLiteDatabase db;

    public ReportDAO(Context context) {
        db = DBHelper.getDatabase(context);
    }

    public void close() {
        db.close();
    }

    public Report insert(Report report) {
        ContentValues cv = new ContentValues();

        cv.put(TARGETNAME_COLUMN, report.getTargetName());
        cv.put(YEARSOFFSET_COLUMN, report.getYearsOffset());
        cv.put(TIMINGSIGNALS_COLUMN, report.getTimingSignals());
        cv.put(CHARTLINE_COLUMN, report.getChartLine());
        cv.put(PROFIT_COLUMN, report.getProfit());
        cv.put(GROSSPROFIT_COLUMN, report.getGrossProfit());
        cv.put(GROSSLOSS_COLUMN, report.getGrossLoss());
        cv.put(TRADECOUNT_COLUMN, report.getTradeCount());
        cv.put(PROFITCOUNT_COLUMN, report.getProfitCount());
        cv.put(LOSSCOUNT_COLUMN, report.getLossCount());
        cv.put(MAXCONTINUEPROFIT_COLUMN, report.getMaxContinueProfit());
        cv.put(MAXCONTINUELOSS_COLUMN, report.getMaxContinueLoss());
        cv.put(MAXCONTINUELOSSTRADE_COLUMN, report.getMaxContinueLossTrade());
        cv.put(TIMESTAMP_COLUMN, report.getTimestamp());
        cv.put(NOTE_COLUMN, report.getNote());

        long id = db.insert(TABLE_NAME, null, cv);

        report.setId(id);
        return report;
    }

    public boolean update(Report report) {
        ContentValues cv = new ContentValues();

        cv.put(TARGETNAME_COLUMN, report.getTargetName());
        cv.put(YEARSOFFSET_COLUMN, report.getYearsOffset());
        cv.put(TIMINGSIGNALS_COLUMN, report.getTimingSignals());
        cv.put(CHARTLINE_COLUMN, report.getChartLine());
        cv.put(PROFIT_COLUMN, report.getProfit());
        cv.put(GROSSPROFIT_COLUMN, report.getGrossProfit());
        cv.put(GROSSLOSS_COLUMN, report.getGrossLoss());
        cv.put(TRADECOUNT_COLUMN, report.getTradeCount());
        cv.put(PROFITCOUNT_COLUMN, report.getProfitCount());
        cv.put(LOSSCOUNT_COLUMN, report.getLossCount());
        cv.put(MAXCONTINUEPROFIT_COLUMN, report.getMaxContinueProfit());
        cv.put(MAXCONTINUELOSS_COLUMN, report.getMaxContinueLoss());
        cv.put(MAXCONTINUELOSSTRADE_COLUMN, report.getMaxContinueLossTrade());
        cv.put(TIMESTAMP_COLUMN, report.getTimestamp());
        cv.put(NOTE_COLUMN, report.getNote());

        String where = KEY_ID + "=" + report.getId();

        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    public boolean delete(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    public List<Report> getAll() {
        List<Report> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public Report get(long id) {
        Report report = null;

        String where = KEY_ID + "=" + id;
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        if (result.moveToFirst()) {
            report = getRecord(result);
        }

        result.close();
        return report;
    }

    private Report getRecord(Cursor cursor) {
        Report report = new Report();

        report.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        report.setTargetName(cursor.getString(cursor.getColumnIndex(TARGETNAME_COLUMN)));
        report.setYearsOffset(cursor.getDouble(cursor.getColumnIndex(YEARSOFFSET_COLUMN)));
        report.setTimingSignals(cursor.getString(cursor.getColumnIndex(TIMINGSIGNALS_COLUMN)));
        report.setChartLine(cursor.getString(cursor.getColumnIndex(CHARTLINE_COLUMN)));
        report.setProfit(cursor.getInt(cursor.getColumnIndex(PROFIT_COLUMN)));
        report.setGrossProfit(cursor.getInt(cursor.getColumnIndex(GROSSPROFIT_COLUMN)));
        report.setGrossLoss(cursor.getInt(cursor.getColumnIndex(GROSSLOSS_COLUMN)));
        report.setTradeCount(cursor.getInt(cursor.getColumnIndex(TRADECOUNT_COLUMN)));
        report.setProfitCount(cursor.getInt(cursor.getColumnIndex(PROFITCOUNT_COLUMN)));
        report.setLossCount(cursor.getInt(cursor.getColumnIndex(LOSSCOUNT_COLUMN)));
        report.setMaxContinueProfit(cursor.getInt(cursor.getColumnIndex(MAXCONTINUEPROFIT_COLUMN)));
        report.setMaxContinueLoss(cursor.getInt(cursor.getColumnIndex(MAXCONTINUELOSS_COLUMN)));
        report.setMaxContinueLossTrade(cursor.getInt(cursor.getColumnIndex(MAXCONTINUELOSSTRADE_COLUMN)));
        report.setTimestamp(cursor.getString(cursor.getColumnIndex(TIMESTAMP_COLUMN)));
        report.setNote(cursor.getString(cursor.getColumnIndex(NOTE_COLUMN)));

        return report;
    }

    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public void clearTable(){
        for(Report report : getAll()){
            delete(report.getId());
        }
    }

    public void sample() {
        Report report1 = new Report();

        report1.setTargetName("2330 台積電");
        report1.setYearsOffset(1);
        report1.setTimingSignals("1,0,0,0,0,-1,0,1,0,-1");
        report1.setChartLine("10,20,30,40,50,60,70,80,90,100");
        report1.setProfit(20000);
        report1.setGrossProfit(80000);
        report1.setGrossLoss(60000);
        report1.setTradeCount(25);
        report1.setProfitCount(10);
        report1.setLossCount(15);
        report1.setMaxContinueProfit(3);
        report1.setMaxContinueLoss(5);
        report1.setMaxContinueLossTrade(20000);
        report1.setNote("測試一");

        Report report2 = new Report();

        report2.setTargetName("3484 松騰");
        report1.setYearsOffset(0.5);
        report1.setTimingSignals("1,0,0,0,0,-1,0,1,0,-1");
        report2.setChartLine("10,-20,30,-40,50,-60,70,-80,90,-100");
        report2.setProfit(10000);
        report2.setGrossProfit(60000);
        report2.setGrossLoss(50000);
        report2.setTradeCount(50);
        report2.setProfitCount(40);
        report2.setLossCount(10);
        report2.setMaxContinueProfit(16);
        report2.setMaxContinueLoss(8);
        report2.setMaxContinueLossTrade(30000);
        report2.setNote("測試二");

        insert(report1);
        insert(report2);

    }

}
