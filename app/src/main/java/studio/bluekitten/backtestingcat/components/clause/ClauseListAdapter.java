package studio.bluekitten.backtestingcat.components.clause;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studio.bluekitten.backtestingcat.R;

public class ClauseListAdapter extends RecyclerView.Adapter<ClauseListAdapter.ViewHolder>{

    private List<String> descriptions;

    public ClauseListAdapter(){
        this.descriptions = new ArrayList();
    }

    public ClauseListAdapter(String[] descriptions){
        this.descriptions = new ArrayList();
        for(String des : descriptions){
            this.descriptions.add(des);
        }
    }

    public ClauseListAdapter(List<String> descriptions){
        this.descriptions = descriptions;
    }

    public void add(String item){
        descriptions.add(item);
        notifyItemInserted(descriptions.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_clause, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.clauseListDescription);
        }
    }
}
