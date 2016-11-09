package com.arpaul.geocare.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arpaul.geocare.GeoFenceActivity;
import com.arpaul.geocare.R;
import com.arpaul.geocare.adapter.TrackLocationsAdapter;
import com.arpaul.geocare.common.AppConstant;
import com.arpaul.geocare.common.ApplicationInstance;
import com.arpaul.geocare.dataAccess.GCCPConstants;
import com.arpaul.geocare.dataObject.GeoFenceLocationDO;
import com.arpaul.utilitieslib.CalendarUtils;
import com.arpaul.utilitieslib.LogUtils;
import com.arpaul.utilitieslib.StringUtils;

import java.util.LinkedHashMap;

/**
 * Created by Aritra on 03-11-2016.
 */

public class TrackPathFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    private TextView tvNoLocations;
    private RecyclerView rvGeoLocations;
    private FloatingActionButton fabGeoFence;

    private LinkedHashMap<String, GeoFenceLocationDO> hashGeoFenceLocationDO = new LinkedHashMap<>();
    private TrackLocationsAdapter adapter;

    public static TrackPathFragment newInstance() {
        TrackPathFragment fragment = new TrackPathFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trackfence, container, false);

        initialiseFragment(view);

        bindControls();

        return view;
    }

    private void loadData(){
        getActivity().getSupportLoaderManager().initLoader(ApplicationInstance.LOADER_FETCH_TRACK_LOCATION, null, this);
    }

    private void bindControls(){
        fabGeoFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GeoFenceActivity.class));
            }
        });

        AppConstant.trackClickPosition = -1;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity().getSupportLoaderManager().getLoader(ApplicationInstance.LOADER_FETCH_TRACK_LOCATION) != null)
            getActivity().getSupportLoaderManager().restartLoader(ApplicationInstance.LOADER_FETCH_TRACK_LOCATION, null, this);
        else
            loadData();

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id){
            case ApplicationInstance.LOADER_FETCH_TRACK_LOCATION :

//                SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//
//                queryBuilder.setTables(
//                        GCCPConstants.GEOFENCE_LOCATION_TABLE_NAME + GCCPConstants.TABLE_GROUP_BY + GeoFenceLocationDO.OCCURANCEDATE);

//                LogUtils.infoLog("QUERY_FARM_LIST", queryBuilder.getTables());

                return new CursorLoader(getActivity(), GCCPConstants.CONTENT_URI_GEOFENCE_LOC,
                        new String[]{GeoFenceLocationDO.LOCATIONID, GeoFenceLocationDO.LOCATIONNAME, GeoFenceLocationDO.EVENT,
                                GeoFenceLocationDO.OCCURANCEDATE, GeoFenceLocationDO.OCCURANCETIME},
                        null/*queryBuilder.getTables()*//*GeoFenceLocationDO.OCCURANCEDATE + GCCPConstants.TABLE_QUES*/,
                        null/*new String[]{CalendarUtils.getDateinPattern(CalendarUtils.DATE_FORMAT1)}*/,
                        GeoFenceLocationDO.OCCURANCEDATE + GCCPConstants.TABLE_DESC);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()){
            case ApplicationInstance.LOADER_FETCH_TRACK_LOCATION :
                if(data instanceof Cursor) {
                    Cursor cursor = (Cursor) data;
                    if(cursor != null && cursor.moveToFirst()){
                        GeoFenceLocationDO objGeoFenceLocationDO = null;
                        hashGeoFenceLocationDO.clear();
                        do {

                            int locationId = StringUtils.getInt(cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LOCATIONID)));
                            String locationName = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LOCATIONNAME));
                            String event = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.EVENT));
                            String occuranceDate = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.OCCURANCEDATE));
                            String occuranceTime = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.OCCURANCETIME));

                            objGeoFenceLocationDO = hashGeoFenceLocationDO.get(locationName+"]"+occuranceDate);
                            if(objGeoFenceLocationDO == null) {
                                objGeoFenceLocationDO = new GeoFenceLocationDO();

                                objGeoFenceLocationDO.LocationId = locationId;
                                objGeoFenceLocationDO.LocationName = locationName;
                                objGeoFenceLocationDO.Event = event;
                                objGeoFenceLocationDO.OccuranceDate = occuranceDate;
                                objGeoFenceLocationDO.OccuranceTime = occuranceTime;

                                objGeoFenceLocationDO.arrTimings.add(event + "]" + occuranceTime);

                                hashGeoFenceLocationDO.put(locationName+"]"+occuranceDate, objGeoFenceLocationDO);
                            } else {
                                objGeoFenceLocationDO.arrTimings.add(event + "]" + occuranceTime);
                            }
//                            hashGeoFenceLocationDO.add(objGeoFenceLocationDO);
                        } while (cursor.moveToNext());

                        if(hashGeoFenceLocationDO != null && hashGeoFenceLocationDO.size() > 0){
                            tvNoLocations.setVisibility(View.GONE);
                            rvGeoLocations.setVisibility(View.VISIBLE);

                            adapter.refresh(hashGeoFenceLocationDO);
                        } else {
                            tvNoLocations.setVisibility(View.VISIBLE);
                            rvGeoLocations.setVisibility(View.GONE);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void initialiseFragment(View view){
        fabGeoFence = (FloatingActionButton) view.findViewById(R.id.fabGeoFence);
        tvNoLocations = (TextView) view.findViewById(R.id.tvNoLocations);
        rvGeoLocations = (RecyclerView) view.findViewById(R.id.rvGeoLocations);

        adapter = new TrackLocationsAdapter(getActivity(), new LinkedHashMap<String, GeoFenceLocationDO>());
        rvGeoLocations.setAdapter(adapter);
    }
}
