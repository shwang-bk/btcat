package studio.bluekitten.backtestingcat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import studio.bluekitten.backtestingcat.components.target.ProgressControlTask;
import studio.bluekitten.backtestingcat.components.target.ReportCalculateTask;
import studio.bluekitten.backtestingcat.components.target.TargetAutoCompleteTextAdapter;
import studio.bluekitten.backtestingcat.core.Calculator;
import studio.bluekitten.backtestingcat.core.StrategyController;
import studio.bluekitten.backtestingcat.core.strategies.Strategy;
import studio.bluekitten.backtestingcat.util.AdRequestGenerator;

public class TargetActivity extends AppCompatActivity
        implements OnSeekBarChangeListener, Toolbar.OnMenuItemClickListener{

    private Toolbar toolbar;
    private SeekBar rangeSeek;
    private TextView rangeView;
    private EditText stopLossEditText, stopProfitEditText;
    private AutoCompleteTextView stockIdText;

    private double testRange;
    private Calculator calculator;
    private ArrayList<Strategy> inStrategys, outStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_target);

        getExtras();
        initToolBar();
        initComponent();
        initAD();
    }

    // 取得由 ClauseActivity 傳入已選擇之策略
    private void getExtras(){
        Intent intent = getIntent();
        inStrategys = intent.getParcelableArrayListExtra(StrategyController.FLAG_IN_STRATEGYS);
        outStrategy = intent.getParcelableArrayListExtra(StrategyController.FLAG_OUT_STRATEGYS);
        calculator = new Calculator(inStrategys, outStrategy);
    }

    // ToolBar 初始化
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("選擇標的");
        toolbar.inflateMenu(R.menu.target_menu);
        toolbar.setOnMenuItemClickListener(TargetActivity.this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.targetnexticon:
                if(checkStockMeta())
                    startCalculate();
        }
        return false;
    }

    private boolean checkStockMeta(){
        String inputMeta = stockIdText.getText().toString();
        String[] metas = getResources().getStringArray(R.array.stock_metas);

        for(String meta : metas){
            if(meta.equals(inputMeta))
                return true;
        }
        stockIdText.setError("未找到符合股票");

        return false;
    }

    private void startCalculate(){
        String stockMeta = stockIdText.getText().toString();
        int stopLoss = getStopVal(stopLossEditText);
        int stopProfit = getStopVal(stopProfitEditText);

        if(stopLoss > 0)
            calculator.setStopLoss(stopLoss);
        if(stopProfit > 0)
            calculator.setStopProfit(stopProfit);

        ProgressControlTask progressControlTask = new ProgressControlTask(this);
        progressControlTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        ReportCalculateTask calculateTask = new ReportCalculateTask(this, testRange, stockMeta);
        calculateTask.setProgressControlTask(progressControlTask);
        calculateTask.setCalculator(calculator);
        calculateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private int getStopVal(EditText text){
        if(text.getText().length() > 0)
            return Integer.parseInt(text.getText().toString());
        else
            return 0;
    }

    private void initComponent(){
        rangeSeek = (SeekBar) findViewById(R.id.targetRangeSeek);
        rangeSeek.setOnSeekBarChangeListener(TargetActivity.this);

        rangeView = (TextView) findViewById(R.id.targetRange);
        testRange = 0.5;

        stopLossEditText = (EditText) findViewById(R.id.targetStopLoss);
        stopProfitEditText = (EditText) findViewById(R.id.targetStopProfit);

        stockIdText = (AutoCompleteTextView) findViewById(R.id.targetStockId);
        stockIdText.setThreshold(1);
        stockIdText.setAdapter(new TargetAutoCompleteTextAdapter(this, android.R.layout.simple_dropdown_item_1line));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (progress){
            case 0:
                rangeView.setText("半年");
                testRange = 0.5;
                break;

            case 1:
                rangeView.setText("一年");
                testRange = 1;
                break;

            case 2:
                rangeView.setText("兩年");
                testRange = 2;
                break;

            case 3:
                rangeView.setText("三年");
                testRange = 3;
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    private void initAD(){
        AdView mAdView = (AdView) findViewById(R.id.targetAdView);
        AdRequest adRequest = AdRequestGenerator.getNewAdRequest();
        mAdView.loadAd(adRequest);
    }
}
