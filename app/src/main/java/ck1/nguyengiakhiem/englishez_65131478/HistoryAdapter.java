package ck1.nguyengiakhiem.englishez_65131478;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<HistoryItem> list;

    public HistoryAdapter(Context context, ArrayList<HistoryItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_history, parent, false);
        }

        HistoryItem item = list.get(i);

        TextView tvDate = view.findViewById(R.id.tv_date);
        TextView tvScore = view.findViewById(R.id.tv_score);
        TextView tvPercent = view.findViewById(R.id.tv_percent);

        tvDate.setText(item.getDate());
        tvScore.setText("Score: " + item.getScore() + " / " + item.getTotal());
        tvPercent.setText("Time: 75:00 → " + item.getTime());

        return view;
    }
}
