package studio.bluekitten.backtestingcat.components.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import studio.bluekitten.backtestingcat.R;

public class HomeDrawerAdapter extends RecyclerView.Adapter<HomeDrawerAdapter.ViewHolder> {

    public static final int VIEW_ITEM_BUG_REPORT = 0;
    public static final int VIEW_ITEM_SHARE = 1;
    public static final int VIEW_ITEM_SCORE = 2;

    private Context context;
    private String[] items;
    private int[] dialogMessageId = {R.string.terms_of_service, R.string.privacy_policy, R.string.disclaimer};
    private int[] iconId = {R.drawable.ic_bug_report_black_24dp, R.drawable.ic_people_black_24dp,
             R.drawable.ic_thumb_up_black_24dp, R.drawable.ic_pets_black_24dp,
            R.drawable.ic_pets_black_24dp, R.drawable.ic_pets_black_24dp};


    public HomeDrawerAdapter(Context context){
        this.context = context;
        items = context.getResources().getStringArray(R.array.left_drawer);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_items_home, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(items[position]);
        holder.icon.setImageResource(iconId[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, DialogInterface.OnClickListener{
        ImageView icon;
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.drawerItem);
            text = (TextView)itemView.findViewById(R.id.drawerText);
            itemView.setOnClickListener(ViewHolder.this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            switch (position){
                case VIEW_ITEM_BUG_REPORT:
                    connectToBugReportSite();
                    return;

                case VIEW_ITEM_SHARE:
                    shareToFriends();
                    return;

                case VIEW_ITEM_SCORE:
                    connectToPlayStore();
                    return;

                default:
                    String message = context.getString(dialogMessageId[position - 3]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(items[position])
                            .setPositiveButton("確定", ViewHolder.this)
                            .setMessage(message);
                    builder.create().show();
            }
        }

        private void connectToBugReportSite(){
            Uri uri = Uri.parse("https://www.facebook.com/backtestingcat/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }

        private void shareToFriends(){
            String sendText = context.getResources().getString(R.string.share_text);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        }

        private void connectToPlayStore(){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=studio.bluekitten.backtestingcat");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }
}
