package com.arpaul.geocare.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arpaul.geocare.BaseActivity;
import com.arpaul.geocare.DashboardActivity;
import com.arpaul.geocare.R;
import com.arpaul.geocare.dataObject.GeoFenceLocationDO;
import com.arpaul.utilitieslib.CalendarUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Aritra on 23-06-2016.
 */
public class TrackLocationsAdapter extends RecyclerView.Adapter<TrackLocationsAdapter.ParentViewHolder> {

    private Context context;
    private LinkedHashMap<String, GeoFenceLocationDO> hashGeoLocs = new LinkedHashMap<>();
    private ArrayList<String> arrLocationNames = new ArrayList<>();
    private TrackLocationTimeAdapter childAdapter = null;

    public TrackLocationsAdapter(Context context, LinkedHashMap<String, GeoFenceLocationDO> hashGeoLocs) {
        this.context = context;
        this.hashGeoLocs = hashGeoLocs;
        childAdapter = new TrackLocationTimeAdapter(context, new ArrayList<String>());
    }

    public void refresh(LinkedHashMap<String, GeoFenceLocationDO> arrTours) {
        this.hashGeoLocs = arrTours;
        notifyDataSetChanged();
    }

    @Override
    public ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cell_track, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ParentViewHolder holder, final int position) {
        final GeoFenceLocationDO objGeoFenceLocDO = hashGeoLocs.get(arrLocationNames.get(position));

        holder.tvTourName.setText(objGeoFenceLocDO.LocationName);

        String descrip = objGeoFenceLocDO.Event + " at " +
                CalendarUtils.getDateinPattern(objGeoFenceLocDO.OccuranceDate, CalendarUtils.DATE_FORMAT1, CalendarUtils.DATE_FORMAT_WITH_COMMA);
//                CalendarUtils.getDateinPattern(objGeoFenceLocDO.OccuranceTime, CalendarUtils.TIME_SEC_FORMAT, CalendarUtils.TIME_HOUR_MINUTE);
        holder.tvTourDesc.setText(descrip);

        if(context instanceof DashboardActivity && ((DashboardActivity)context).trackClickPosition == position){
            childAdapter.refresh(objGeoFenceLocDO.arrTimings);
            holder.rvChild.setVisibility(View.VISIBLE);
        } else {
            holder.rvChild.setVisibility(View.GONE);
        }

        holder.tvTourName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.performClick();
            }
        });

        holder.tvTourDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.performClick();
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof DashboardActivity){
                    ((DashboardActivity)context).trackClickPosition = position;
                    notifyDataSetChanged();
                }
            }
        });

        ((BaseActivity)context).applyTypeface(((BaseActivity)context).getParentView(holder.mView),((BaseActivity)context).tfRegular, Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        if(hashGeoLocs != null) {
            getLocationNames();
            return hashGeoLocs.size();
        }

        return 0;
    }

    private void getLocationNames() {
        Set<String> keyStack = hashGeoLocs.keySet();
        arrLocationNames = new ArrayList<String>(keyStack);
//        for(int i = 0; i < arrLocationNames.size(); i++) {
//            String name = arrLocationNames.get(i);
//            arrLocationNames.set(i, name.split("]")[1]);
//        }
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvTourName;
        public final TextView tvTourDesc;
        public final RecyclerView rvChild;

        public ParentViewHolder(View view) {
            super(view);
            mView = view;
            tvTourName                  = (TextView) view.findViewById(R.id.tvLocationName);
            tvTourDesc                  = (TextView) view.findViewById(R.id.tvLocationAddress);
            rvChild                     = (RecyclerView) view.findViewById(R.id.rvChild);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
