package com.arpaul.geocare.activityRecognition;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.arpaul.geocare.R;
import com.arpaul.geocare.common.AppConstant;
import com.arpaul.geocare.dataAccess.GCCPConstants;
import com.arpaul.geocare.dataObject.ActivityRecogDO;
import com.arpaul.geocare.dataObject.GeoFenceLocationDO;
import com.arpaul.utilitieslib.CalendarUtils;
import com.arpaul.utilitieslib.LogUtils;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by ARPaul on 19-11-2016.
 */

public class DetectedIntentService extends IntentService {

    protected static final  String TAG = "DetectedIntentService";
    private String name, event, date, time;
    private int locationID;

    public DetectedIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent.hasExtra(ActivityRecogDO.LOCATIONID)) {
            locationID = intent.getExtras().getInt(ActivityRecogDO.LOCATIONID, 0);
            name = intent.getExtras().getString(ActivityRecogDO.LOCATIONNAME);
            event = intent.getExtras().getString(ActivityRecogDO.EVENT);
            date = intent.getExtras().getString(ActivityRecogDO.OCCURANCEDATE);
            time = intent.getExtras().getString(ActivityRecogDO.OCCURANCETIME);
        }

        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
//        Intent localIntent = new Intent(AppConstant.BROADCAST_ACTION);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        String actiRecog = "";
        for(DetectedActivity thisActivity: detectedActivities){
            actiRecog +=  getActivityString(thisActivity.getType()) + thisActivity.getConfidence() + "%\n";
        }
        // Log each activity.
        LogUtils.infoLog(TAG, actiRecog);

        ContentValues cValues = new ContentValues();
        cValues.put(ActivityRecogDO.LOCATIONID, locationID);
        cValues.put(ActivityRecogDO.LOCATIONNAME, name);
        cValues.put(ActivityRecogDO.EVENT, event);
        cValues.put(ActivityRecogDO.OCCURANCEDATE, date);
        cValues.put(ActivityRecogDO.OCCURANCETIME, time);
        cValues.put(ActivityRecogDO.CURRENT_ACTIVITY, actiRecog);

        AppConstant.writeFile("\nInsert: locationID: " + locationID +
                " name: " + name +
                " event: " + event +
                " date: " + date +
                " time: " + time +
                " actiRecog: " + actiRecog);

        Uri uri = getContentResolver().insert(GCCPConstants.CONTENT_URI_ACTI_RECOG, cValues);

        // Broadcast the list of detected activities.
//        localIntent.putExtra(AppConstant.ACTIVITY_EXTRA, detectedActivities);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    /**
     * Returns a human readable String corresponding to a detected activity type.
     */
    public String getActivityString(int detectedActivityType) {
        Resources resources = this.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }
}