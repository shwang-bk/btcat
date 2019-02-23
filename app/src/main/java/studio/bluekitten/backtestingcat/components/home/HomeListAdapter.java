package studio.bluekitten.backtestingcat.components.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import studio.bluekitten.backtestingcat.R;
import studio.bluekitten.backtestingcat.ReportActivity;
import studio.bluekitten.backtestingcat.core.Report;


public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private List<Report> reports = new ArrayList();
    private Context context;

    public HomeListAdapter(Context context, List<Report>reports){
        this.context = context;
        this.reports = new ArrayList(reports);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_items_home, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int holderPosition = holder.getAdapterPosition();

        Report report = reports.get(holderPosition);
        String profit = "" + report.getProfit();
        String percentProfitable = (int)(report.getPercentProfitable()) + "%";
        String note = report.getNote();

        int color = Color.parseColor("#6ECC75");
        if(report.getProfit() > 0)
            color = Color.parseColor("#FF8989");

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(percentProfitable, color);

        holder.textProfit.setText(profit);
        holder.textNote.setText(note);
        holder.imageView.setImageDrawable(textDrawable);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public void onItemDismiss(int adapterPosition) {
        reports.remove(adapterPosition);

        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, getItemCount());

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textProfit, textNote;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textProfit = (TextView) itemView.findViewById(R.id.homeCardProfit);
            textNote = (TextView) itemView.findViewById(R.id.homeCardNote);
            imageView = (ImageView) itemView.findViewById(R.id.homeCardImg);

            itemView.setOnClickListener(ViewHolder.this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ReportActivity.class);
            intent.putExtra(Report.FLAG_REPORT_ID, reports.get(getAdapterPosition()).getId());
            context.startActivity(intent);
        }
    }
}
