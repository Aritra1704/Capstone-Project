package com.arpaul.geocare.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.arpaul.geocare.BaseActivity;
import com.arpaul.geocare.R;
import com.arpaul.geocare.dataObject.PrefLocationDO;

import java.util.ArrayList;

/**
 * Created by Aritra on 23-06-2016.
 */
public class GeoLocationsAdapter extends RecyclerView.Adapter<GeoLocationsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PrefLocationDO> arrPrefLocationDO = new ArrayList<>();

    public GeoLocationsAdapter(Context context, ArrayList<PrefLocationDO> arrTours) {
        this.context = context;
        this.arrPrefLocationDO = arrTours;
    }

    public void refresh(ArrayList<PrefLocationDO> arrTours) {
        this.arrPrefLocationDO = arrTours;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cell_geolocation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PrefLocationDO objTourDO = arrPrefLocationDO.get(position);

        holder.tvLocationName.setText(objTourDO.LocationName);
        holder.tvLocationAddress.setText(objTourDO.Address);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, objTourDO.LocationName, Toast.LENGTH_SHORT).show();
            }
        });

        ((BaseActivity)context).applyTypeface(((BaseActivity)context).getParentView(holder.mView),((BaseActivity)context).tfRegular, Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        if(arrPrefLocationDO != null)
            return arrPrefLocationDO.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvLocationName;
        public final TextView tvLocationAddress;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvLocationName      = (TextView) view.findViewById(R.id.tvLocationName);
            tvLocationAddress   = (TextView) view.findViewById(R.id.tvLocationAddress);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
