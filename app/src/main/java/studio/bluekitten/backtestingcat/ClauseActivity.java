package studio.bluekitten.backtestingcat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import studio.bluekitten.backtestingcat.components.clause.ClausePagerAdapter;
import studio.bluekitten.backtestingcat.core.StrategyController;
import studio.bluekitten.backtestingcat.core.strategies.Strategy;
import studio.bluekitten.backtestingcat.util.AdRequestGenerator;

public class ClauseActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{
    private final int IN = 0;
    private final int OUT = 1;

    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private ClausePagerAdapter pagerAdapter;
    private ArrayList<Strategy> strategies[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_clause);

        initToolBar();
        initTabViews();
        initAD();
    }

    // ToolBar 初始化 (新增，下一步)
    // 新增按鈕進入選擇策略之 ChoiceActivity
    // 下一步按鈕進入選擇標的及時間之 TargetActivity
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("進出場條件");
        toolbar.inflateMenu(R.menu.clause_menu);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId){
            case R.id.clausenexticon:
                intent = new Intent(ClauseActivity.this, TargetActivity.class);
                intent.putParcelableArrayListExtra(StrategyController.FLAG_IN_STRATEGYS, strategies[IN]);
                intent.putParcelableArrayListExtra(StrategyController.FLAG_OUT_STRATEGYS, strategies[OUT]);

                startActivity(intent);
                return true;

            case R.id.addicon:
                intent = new Intent(ClauseActivity.this, ChoiceActivity.class);
                startActivityForResult(intent, viewPager.getCurrentItem());
                return true;

            default:
                return false;
        }
    }

    // TabView(TabLayout & ViewPager) 初始化
    // 顯示已定好的策略
    private void initTabViews() {
        pagerAdapter = new ClausePagerAdapter();

        strategies = new ArrayList[2];
        strategies[IN] = new ArrayList();
        strategies[OUT] = new ArrayList();

        tabs = (TabLayout) findViewById(R.id.clauseTabs);
        viewPager = (ViewPager) findViewById(R.id.clausePages);
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        viewPager.setAdapter(pagerAdapter);
    }

    private void initAD(){
        AdView mAdView = (AdView) findViewById(R.id.clauseAdView);
        AdRequest adRequest = AdRequestGenerator.getNewAdRequest();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Strategy s = data.getParcelableExtra(StrategyController.FLAG_STRATEGY);
            strategies[requestCode].add(s);
            pagerAdapter.getChildAdapter(requestCode).add(s.descriptions());
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
