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
import com.arpaul.geocare.dataObject.GeoFenceLocationDO;
import com.arpaul.geocare.dataObject.PrefLocationDO;
import com.arpaul.utilitieslib.CalendarUtils;

import java.util.ArrayList;

/**
 * Created by Aritra on 23-06-2016.
 */
public class TrackLocationsAdapter extends RecyclerView.Adapter<TrackLocationsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GeoFenceLocationDO> arrTours = new ArrayList<>();

    public TrackLocationsAdapter(Context context, ArrayList<GeoFenceLocationDO> arrTours) {
        this.context = context;
        this.arrTours = arrTours;
    }

    public void refresh(ArrayList<GeoFenceLocationDO> arrTours) {
        this.arrTours = arrTours;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cell_tours, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GeoFenceLocationDO objTourDO = arrTours.get(position);

        holder.tvTourName.setText(objTourDO.LocationName);

        String descrip = objTourDO.Event + " at " +
                CalendarUtils.getDateinPattern(objTourDO.OccuranceDate, CalendarUtils.DATE_FORMAT1, CalendarUtils.DATE_FORMAT_WITH_COMMA) + " " +
                CalendarUtils.getDateinPattern(objTourDO.OccuranceTime, CalendarUtils.TIME_SEC_FORMAT, CalendarUtils.TIME_HOUR_MINUTE);
        holder.tvTourDesc.setText(descrip);

        ((BaseActivity)context).applyTypeface(((BaseActivity)context).getParentView(holder.mView),((BaseActivity)context).tfRegular, Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        if(arrTours != null)
            return arrTours.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvTourName;
        public final TextView tvTourDesc;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvTourName                  = (TextView) view.findViewById(R.id.tvLocationName);
            tvTourDesc                  = (TextView) view.findViewById(R.id.tvLocationAddress);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
