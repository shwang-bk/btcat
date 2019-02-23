package studio.bluekitten.backtestingcat.components.target;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import studio.bluekitten.backtestingcat.ReportActivity;
import studio.bluekitten.backtestingcat.core.Calculator;
import studio.bluekitten.backtestingcat.core.Report;
import studio.bluekitten.backtestingcat.core.StockB;
import studio.bluekitten.backtestingcat.core.StockCrawler;
import studio.bluekitten.backtestingcat.core.sql.ReportDAO;
import studio.bluekitten.backtestingcat.util.TestActivity;

/**
 * Created by bluecat on 2017/2/11.
 */

public class ReportCalculateTask extends AsyncTask<Void, Void, Report> {
    private ProgressControlTask progressControlTask;
    private Context context;
    private StockCrawler crawler;
    private Calculator calculator;

    public ReportCalculateTask(Context context, double testRange, String stockMeta){
        this.context = context;

        int monthOffset =  (int)(12 * testRange);
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.MONTH, -monthOffset);
        crawler = new StockCrawler(stockMeta, lastYear);
    }

    public void setProgressControlTask(ProgressControlTask progressControlTask) {
        this.progressControlTask = progressControlTask;
    }

    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    @Override
    protected Report doInBackground(Void... params){
        try {
            checkInternetConnected();
            progressControlTask.setStep(30);
            StockB stock = crawler.crawlStockOnYahooFinance();
            progressControlTask.setStep(70);
            List<StockB> stocks = new ArrayList<>();
            stocks.add(stock);
            Report report = calculator.calculate(stocks);
            progressControlTask.setStep(100);
            progressControlTask.get();
            return report;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkInternetConnected(){
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connManager.getActiveNetworkInfo();

        if(info == null || !info.isConnected()) {
            progressControlTask.interrupt(true);
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "無法取得資料，請連上網路後再試",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onPostExecute(Report report){
        saveReport(report);
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(Report.FLAG_REPORT_ID, report.getId());
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    private void saveReport(Report report){
        ReportDAO reportDAO = new ReportDAO(context.getApplicationContext());
        reportDAO.insert(report);
        reportDAO.close();
    }

}
