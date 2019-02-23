package studio.bluekitten.backtestingcat;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import studio.bluekitten.backtestingcat.components.DrawerDivider;
import studio.bluekitten.backtestingcat.components.home.HomeDrawerAdapter;
import studio.bluekitten.backtestingcat.components.home.HomeListAdapter;
import studio.bluekitten.backtestingcat.core.Report;
import studio.bluekitten.backtestingcat.core.sql.ReportDAO;
import studio.bluekitten.backtestingcat.util.AdRequestGenerator;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private ImageView emptyView;
    private RecyclerView recyclerView, drawerList;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private HomeListAdapter adaptor;

    private ReportDAO reportDAO;
    private List<Report> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        initToolBar();
        initRecyclerView();
        initFloatingActionButton();
        initDrawerList();
        initAD();

        // Print External MultiDex Files
//        for (String str : new MultiDexUtils().getLoadedExternalDexClasses(getApplicationContext())){
//            Log.d(TestActivity.TEST_LOG, str);
//        }
    }

    // ToolBar 初始化
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("歷史紀錄");
        setSupportActionBar(toolbar);
    }

    // RecyclerView 初始化
    // 顯示歷史資訊
    private void initRecyclerView(){
        emptyView = (ImageView) findViewById(R.id.homeEmpty);

        reportDAO = new ReportDAO(getApplicationContext());
        reports = reportDAO.getAll();

        recyclerView = (RecyclerView) findViewById(R.id.homeList);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adaptor = new HomeListAdapter(this, reports);
        recyclerView.setAdapter(adaptor);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setTouchCallback();

        if(reports.size() == 0)
            convertEmptyView();
    }

    private void setTouchCallback(){
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(dragFlags, swipeFlags) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                reportDAO.delete(reports.get(position).getId());
                reports.remove(position);

                adaptor.onItemDismiss(position);
                if(reports.size() == 0)
                    convertEmptyView();
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void convertEmptyView(){
        if(emptyView.getVisibility() != View.VISIBLE){
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    // FloatingActionButton 初始化
    // 按下按鈕即進入自訂策略之 ClauseActivity
    private void initFloatingActionButton(){
        floatingActionButton = (FloatingActionButton) findViewById(R.id.homeFAB);
        floatingActionButton.setOnClickListener(HomeActivity.this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(HomeActivity.this, ClauseActivity.class);
        startActivity(intent);
    }

    private void initDrawerList(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.syncState();
        drawer.addDrawerListener(drawerToggle);

        drawerList = (RecyclerView) findViewById(R.id.homeDrawer);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        drawerList.setLayoutManager(layoutManager);

        HomeDrawerAdapter adapter = new HomeDrawerAdapter(this);
        drawerList.setAdapter(adapter);

        DrawerDivider divider = new DrawerDivider(this);
        divider.addDividerIndex(0);
        divider.addDividerIndex(2);
        drawerList.addItemDecoration(divider);

    }

    private void initAD(){
        AdView mAdView = (AdView) findViewById(R.id.homeAdView);
        AdRequest adRequest = AdRequestGenerator.getNewAdRequest();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        reportDAO.close();
    }

}
