package studio.bluekitten.backtestingcat.components.choice;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.core.StrategyController;

public class ChoiceListAdapter extends RecyclerView.Adapter<ChoiceListAdapter.ViewHolder> {

    private Context context;
    private String[] strategyNames;
    private StrategyController handler;

    public ChoiceListAdapter(Context context, StrategyController handler){
        this.context = context;
        this.handler = handler;
        this.strategyNames = handler.getStrategyNames(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_choise, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int holderPosition = holder.getAdapterPosition();

        String[] name = strategyNames[holderPosition].split(" ");
        String category = name[0];
        String subName = name[1];

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int color = colorGenerator.getColor(category);

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRect(category, color);

        holder.textView.setText(subName);
        holder.imageView.setImageDrawable(textDrawable);
    }

    @Override
    public int getItemCount() {
        return strategyNames.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, DialogInterface.OnClickListener {
        private int dialogId;
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.choiseListImg);
            textView = (TextView) itemView.findViewById(R.id.choiseListStrategyName);

            itemView.setOnClickListener(ViewHolder.this);
        }

        @Override
        public void onClick(View view) {
            AlertDialog dialog = buildCorrespondingDialog();
            dialog.show();
        }

        // 根據點選策略不同，建構不同相對應的對話框
        // 按下確定後回傳 ClauseActivity
        private AlertDialog buildCorrespondingDialog(){
            dialogId = getLayoutPosition();
            String title = strategyNames[dialogId].split(" ")[1];

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setView(handler.getLayoutId(dialogId))
                    .setPositiveButton("確定", ViewHolder.this)
                    .setNegativeButton("取消", ViewHolder.this);
            return builder.create();
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int id) {
            if(id == DialogInterface.BUTTON_POSITIVE)
                onPositive(dialogInterface);
            else if(id == DialogInterface.BUTTON_NEGATIVE)
                onNegative(dialogInterface);
        }

        private void onPositive(DialogInterface dialogInterface){
            Intent intent = new Intent();
            Parcelable p = handler.createStrategy(dialogId , dialogInterface);
            intent.putExtra(StrategyController.FLAG_STRATEGY, p);
            ((Activity)context).setResult(0, intent);
            dialogInterface.dismiss();
            ((Activity)context).finish();
        }

        private void onNegative(DialogInterface dialogInterface){
            dialogInterface.dismiss();
        }
    }
}
