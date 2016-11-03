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
import com.arpaul.geocare.adapter.GeoLocationsAdapter;
import com.arpaul.geocare.common.ApplicationInstance;
import com.arpaul.geocare.dataAccess.GCCPConstants;
import com.arpaul.geocare.dataObject.GeoFenceLocationDO;
import com.arpaul.geocare.dataObject.PrefLocationDO;
import com.arpaul.utilitieslib.StringUtils;

import java.util.ArrayList;

/**
 * Created by Aritra on 03-11-2016.
 */

public class TrackPathFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    private TextView tvNoLocations;
    private RecyclerView rvGeoLocations;
    private FloatingActionButton fabGeoFence;

    private ArrayList<PrefLocationDO> arrPrefLocationDO = new ArrayList<>();
    private GeoLocationsAdapter adapter;

    public static TrackPathFragment newInstance() {
        TrackPathFragment fragment = new TrackPathFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trackfence, container, false);

        initialiseFragment(view);

        loadData();

        bindControls();

        return view;
    }

    private void loadData(){
        getActivity().getSupportLoaderManager().initLoader(ApplicationInstance.LOADER_FETCH_ALL_LOCATION, null, this);
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

        getActivity().getSupportLoaderManager().restartLoader(ApplicationInstance.LOADER_FETCH_ALL_LOCATION,null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id){
            case ApplicationInstance.LOADER_FETCH_ALL_LOCATION :
                return new CursorLoader(getActivity(), GCCPConstants.CONTENT_URI_GEOFENCE_LOC,
                        new String[]{GeoFenceLocationDO.LOCATIONID, GeoFenceLocationDO.LOCATIONNAME, GeoFenceLocationDO.ADDRESS,
                                GeoFenceLocationDO.LATITUDE, GeoFenceLocationDO.LONGITUDE},
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
            case ApplicationInstance.LOADER_FETCH_ALL_LOCATION :
                if(data instanceof Cursor) {
                    Cursor cursor = (Cursor) data;
                    if(cursor != null && cursor.moveToFirst()){
                        PrefLocationDO objPrefLocationDO = null;
                        arrPrefLocationDO.clear();
                        do {
                            objPrefLocationDO = new PrefLocationDO();
                            objPrefLocationDO.LocationId = StringUtils.getInt(cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LOCATIONID)));
                            objPrefLocationDO.LocationName = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LOCATIONNAME));
                            objPrefLocationDO.Address = cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.ADDRESS));
                            objPrefLocationDO.Latitude = StringUtils.getDouble(cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LATITUDE)));
                            objPrefLocationDO.Longitude = StringUtils.getDouble(cursor.getString(cursor.getColumnIndex(GeoFenceLocationDO.LONGITUDE)));

                            arrPrefLocationDO.add(objPrefLocationDO);
                        } while (cursor.moveToNext());

                        if(arrPrefLocationDO != null && arrPrefLocationDO.size() > 0){
                            tvNoLocations.setVisibility(View.GONE);
                            rvGeoLocations.setVisibility(View.VISIBLE);

                            adapter.refresh(arrPrefLocationDO);
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

        adapter = new GeoLocationsAdapter(getActivity(), new ArrayList<PrefLocationDO>());
        rvGeoLocations.setAdapter(adapter);
    }
}
