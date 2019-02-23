package studio.bluekitten.backtestingcat.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import studio.bluekitten.backtestingcat.R;

public class DrawerDivider extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private List<Integer> indexes;

    public DrawerDivider(Context context){
        indexes = new ArrayList();
        mDivider = ResourcesCompat.getDrawable(context.getResources(), R.drawable.line_divider, null);
    }

    public void addDividerIndex(int index){
        indexes.add(index);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for(int i : indexes){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0)
            return;
        outRect.top = mDivider.getIntrinsicHeight();
    }
}
