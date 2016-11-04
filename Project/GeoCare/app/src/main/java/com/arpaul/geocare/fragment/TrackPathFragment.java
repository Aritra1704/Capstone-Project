package com.arpaul.geocare.fragment;

import android.content.Intent;
import android.database.Cursor;
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
import com.arpaul.geocare.common.ApplicationInstance;
import com.arpaul.geocare.dataAccess.GCCPConstants;
import com.arpaul.geocare.dataObject.GeoFenceLocationDO;
import com.arpaul.utilitieslib.StringUtils;

import java.util.ArrayList;

/**
 * Created by Aritra on 03-11-2016.
 */

public class TrackPathFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    private TextView tvNoLocations;
    private RecyclerView rvGeoLocations;
    private FloatingActionButton fabGeoFence;

    private ArrayList<GeoFenceLocationDO> arrGeoFenceLocationDO = new ArrayList<>();
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
                return new CursorLoader(getActivity(), GCCPConstants.CONTENT_URI_GEOFENCE_LOC,
                        new String[]{GeoFenceLocationDO.LOCATIONID, GeoFenceLocationDO.LOCATIONNAME, GeoFenceLocationDO.EVENT,
                                GeoFenceLocationDO.OCCURANCEDATE, GeoFenceLocationDO.OCCURANCETIME},
                        null,
                        null,
                        null);
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
                        arrGeoFenceLocationDO.clear();
                        do {
                            objGeoFenceLocationDO = new GeoFenceLocationDO();
                            objGeoFenceLocationDO.LocationId = StringUtils.getInt(cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LOCATIONID)));
                            objGeoFenceLocationDO.LocationName = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LOCATIONNAME));
                            objGeoFenceLocationDO.Event = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.EVENT));
                            objGeoFenceLocationDO.OccuranceDate = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.OCCURANCEDATE));
                            objGeoFenceLocationDO.OccuranceTime = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.OCCURANCETIME));

                            arrGeoFenceLocationDO.add(objGeoFenceLocationDO);
                        } while (cursor.moveToNext());

                        if(arrGeoFenceLocationDO != null && arrGeoFenceLocationDO.size() > 0){
                            tvNoLocations.setVisibility(View.GONE);
                            rvGeoLocations.setVisibility(View.VISIBLE);

                            adapter.refresh(arrGeoFenceLocationDO);
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

        adapter = new TrackLocationsAdapter(getActivity(), new ArrayList<GeoFenceLocationDO>());
        rvGeoLocations.setAdapter(adapter);
    }
}
