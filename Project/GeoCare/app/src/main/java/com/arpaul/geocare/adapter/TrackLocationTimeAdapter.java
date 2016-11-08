package com.arpaul.geocare.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arpaul.geocare.BaseActivity;
import com.arpaul.geocare.R;

import java.util.ArrayList;

/**
 * Created by Aritra on 23-06-2016.
 */
public class TrackLocationTimeAdapter extends RecyclerView.Adapter<TrackLocationTimeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrTimes = new ArrayList<>();

    public TrackLocationTimeAdapter(Context context, ArrayList<String> arrTimes) {
        this.context = context;
        this.arrTimes = arrTimes;
    }

    public void refresh(ArrayList<String> arrTimes) {
        this.arrTimes = arrTimes;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cell_tourtime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String strTime = arrTimes.get(position);

        String event[] = strTime.split("]");

        holder.tvTourEvent.setText(event[0]);
        holder.tvTourTime.setText(event[1]);

        ((BaseActivity)context).applyTypeface(((BaseActivity)context).getParentView(holder.mView),((BaseActivity)context).tfRegular, Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        if(arrTimes != null)
            return arrTimes.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvTourTime;
        public final TextView tvTourEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvTourTime      = (TextView) view.findViewById(R.id.tvTourTime);
            tvTourEvent     = (TextView) view.findViewById(R.id.tvTourEvent);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
