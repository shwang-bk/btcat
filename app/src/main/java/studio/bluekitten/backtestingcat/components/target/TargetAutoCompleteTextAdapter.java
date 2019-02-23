package studio.bluekitten.backtestingcat.components.target;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import studio.bluekitten.backtestingcat.R;

/**
 * Created by bluecat on 2017/2/11.
 */

public class TargetAutoCompleteTextAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private String[] stockMetas;
    List<String> filterList;

    public TargetAutoCompleteTextAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;

        stockMetas = context.getResources().getStringArray(R.array.stock_metas);
        filterList = new LinkedList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }
        String stockId = filterList.get(position);
        if (stockId != null) {
            TextView stockIdText = (TextView) view.findViewById(android.R.id.text1);
            if (stockIdText != null)
                stockIdText.setText(stockId);
        }
        return view;
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                List<String> startsWithList = new LinkedList<>();
                List<String> containsList = new LinkedList<>();
                if(charSequence != null) {
                    filterList.clear();
                    int i = 0;
                    for (String str : stockMetas) {
                        String listText = str.toLowerCase();
                        String queryText = charSequence.toString().toLowerCase();

                        switch (listText.indexOf(queryText)){
                            case 0:
                                filterList.add(i++, str);
                                break;

                            case -1:
                                break;

                            default:
                                filterList.add(str);
                        }
                    }
                    results.values = filterList;
                    results.count = filterList.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<String> resultList = (List<String>) filterResults.values;
                if(filterResults != null && filterResults.count > 0){
                    TargetAutoCompleteTextAdapter.this.clear();
                    TargetAutoCompleteTextAdapter.this.addAll(resultList);
                    notifyDataSetChanged();
                }
            }
        };
    }
}
