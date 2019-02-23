package studio.bluekitten.backtestingcat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import studio.bluekitten.backtestingcat.core.Report;
import studio.bluekitten.backtestingcat.core.sql.ReportDAO;
import studio.bluekitten.backtestingcat.util.AdRequestGenerator;

public class ReportActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private Report report;

    private Toolbar toolbar;
    private TextView profitView, percentProfitableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report);

        getExtras();
        initToolBar();
        initMainInfoTextView();
        initReportChart();
        initGridLayout();
        initAD();
    }

    // 取得由 ClauseActivity 傳入已選擇之策略
    private void getExtras(){
        Intent intent = getIntent();
        long reportId = intent.getLongExtra(Report.FLAG_REPORT_ID, -1);

        ReportDAO reportDAO = new ReportDAO(getApplicationContext());
        report = reportDAO.get(reportId);
        reportDAO.close();
    }

    // ToolBar 初始化
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("回測報告" + " (" + report.getTargetName() + ")");
        toolbar.inflateMenu(R.menu.report_menu);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.tipicon:

                // 說明各個參數
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("回測報告內容說明")
                        .setMessage(getApplicationContext().getResources().getString(R.string.report_tip))
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    private void initMainInfoTextView(){
        profitView = (TextView) findViewById(R.id.mainProfit);
        String profitStr = report.getProfit() + " 元";
        profitView.setText(profitStr);

        percentProfitableView = (TextView) findViewById(R.id.mainPercentProfitable);
        String percentTradingStr = report.getPercentProfitable() + " %";
        percentProfitableView.setText(percentTradingStr);
    }

    private void initReportChart() {
        String[] lineStr = report.getChartLine().split(",");
        LineChart lineChart = (LineChart) findViewById(R.id.reportChart);

        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < lineStr.length; i++)
            if(!"null".equals(lineStr[i]))
                entries.add(new Entry(i, Float.parseFloat(lineStr[i])));

        LineDataSet lineDataSet = new LineDataSet(entries, "資產淨值");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setEnabled(false);

        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);

        lineChart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setTextColor(Color.GRAY);

        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
    }

    // GridView 初始化
    // 顯示報告內容
    private void initGridLayout(){
        ((TextView)findViewById(R.id.reportGrossProfit)).setText(String.valueOf(report.getGrossProfit()));
        ((TextView)findViewById(R.id.reportProfitFactor)).setText(String.valueOf(report.getProfitFactor()));
        ((TextView)findViewById(R.id.reportGrossLoss)).setText(String.valueOf(report.getGrossLoss()));

        ((TextView)findViewById(R.id.reportAverageProfit)).setText(String.valueOf(report.getAverageProfit()));
        ((TextView)findViewById(R.id.reportProfitRatio)).setText(String.valueOf(report.getProfitRatio()));
        ((TextView)findViewById(R.id.reportAverageLoss)).setText(String.valueOf(report.getAverageLoss()));

        ((TextView)findViewById(R.id.reportProfitCount)).setText(String.valueOf(report.getProfitCount()));
        ((TextView)findViewById(R.id.reportTradeCount)).setText(String.valueOf(report.getTradeCount()));
        ((TextView)findViewById(R.id.reportLossCount)).setText(String.valueOf(report.getLossCount()));

        ((TextView)findViewById(R.id.reportMaxContinueProfit)).setText(String.valueOf(report.getMaxContinueProfit()));
        ((TextView)findViewById(R.id.reportMaxContinueLoss)).setText(String.valueOf(report.getMaxContinueLoss()));

        ((TextView)findViewById(R.id.reportMaxContinueLossTrade)).setText(String.valueOf(report.getMaxContinueLossTrade()));
        ((TextView)findViewById(R.id.reportPercentTrading)).setText(String.valueOf(report.getPercentTrading()) + "%");

        ((TextView)findViewById(R.id.reportNote)).setText(String.valueOf(report.getNote()));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReportActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initAD(){
        AdView mAdView = (AdView) findViewById(R.id.reportAdView);
        AdRequest adRequest = AdRequestGenerator.getNewAdRequest();
        mAdView.loadAd(adRequest);
    }

}
