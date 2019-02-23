package studio.bluekitten.backtestingcat.components.clause;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ClausePagerAdapter extends PagerAdapter {
    private final int IN = 0;
    private final int OUT = 1;
    private final int PAGE_COUNTS = 2;

    private ClauseListAdapter adapters[];

    public ClausePagerAdapter(){
        adapters = new ClauseListAdapter[PAGE_COUNTS];
        adapters[IN] = new ClauseListAdapter();
        adapters[OUT] = new ClauseListAdapter();
    }

    @Override
    public int getCount() {
        return PAGE_COUNTS;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView recyclerView = new RecyclerView(container.getContext());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapters[position]);
        container.addView(recyclerView);

        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public ClauseListAdapter getChildAdapter(int pageId){
        return adapters[pageId];
    }
}
