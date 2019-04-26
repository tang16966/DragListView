package com.example.dragsortlistview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DragSortAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;

    public DragSortAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context,R.layout.item_drag_sort,null);

        TextView tvTxt;
        TextView tvOverhead;
        TextView tvDelete;

        tvTxt = view.findViewById(R.id.tv_txt);
        tvOverhead = view.findViewById(R.id.tv_overhead);
        tvDelete = view.findViewById(R.id.tv_delete);

        tvTxt.setText(data.get(position));
        tvOverhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void dragItem(int start,int end){
        if (start > end){
            data.add(end,data.get(start));
            data.remove(start + 1);
        }else {
            data.add(end+1,data.get(start));
            data.remove(start);
        }
        notifyDataSetChanged();
    }
}
