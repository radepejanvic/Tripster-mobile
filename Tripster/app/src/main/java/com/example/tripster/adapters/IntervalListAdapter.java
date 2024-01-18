package com.example.tripster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tripster.R;
import com.example.tripster.model.view.Interval;

import java.util.ArrayList;

public class IntervalListAdapter extends ArrayAdapter<Interval> {

    private ArrayList<Interval> intervals;

    public IntervalListAdapter(Context context, ArrayList<Interval> intervals){
        super(context, R.layout.interval_card, intervals);
        this.intervals = intervals;
    }

    @Override
    public int getCount() {
        return intervals.size();
    }

    @Nullable
    @Override
    public Interval getItem(int position) {
        return intervals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Interval interval = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.interval_card,
                    parent, false);
        }

        ConstraintLayout card = convertView.findViewById(R.id.interval_card);
        TextView start = convertView.findViewById(R.id.interval_start);
        TextView end = convertView.findViewById(R.id.interval_end);
        TextView price = convertView.findViewById(R.id.interval_price);

        if (interval != null) {
            start.setText(interval.getStart());
            end.setText(interval.getEnd());
            price.setText(interval.getPrice());
        }

        return convertView;
    }


}
