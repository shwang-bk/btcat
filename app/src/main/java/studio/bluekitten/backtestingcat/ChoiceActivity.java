package studio.bluekitten.backtestingcat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import studio.bluekitten.backtestingcat.components.choice.ChoiceListAdapter;
import studio.bluekitten.backtestingcat.core.StrategyController;
import studio.bluekitten.backtestingcat.util.AdRequestGenerator;

public class ChoiceActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ChoiceListAdapter adapter;
    private Toolbar toolbar;
    private StrategyController controller = StrategyController.getInstence();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choice);

        initToolBar();
        initListView();
        initAD();
    }

    // ToolBar 初始化
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("選擇策略");
    }

    // ExpandableListView 初始化
    // 顯示策略清單
    private void initListView(){
        recyclerView = (RecyclerView) findViewById(R.id.choiseList);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChoiceListAdapter(this, controller);
        recyclerView.setAdapter(adapter);
    }

    private void initAD(){
        AdView mAdView = (AdView) findViewById(R.id.choiceAdView);
        AdRequest adRequest = AdRequestGenerator.getNewAdRequest();
        mAdView.loadAd(adRequest);
    }
}
