package com.arpaul.geocare;

import android.*;
import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arpaul.geocare.common.AppConstant;
import com.arpaul.geocare.dataAccess.GCCPConstants;
import com.arpaul.utilitieslib.FileUtils;
import com.arpaul.utilitieslib.LogUtils;
import com.arpaul.utilitieslib.PermissionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Aritra on 04-11-2016.
 */

public class GeocareSettingsActivity extends BaseActivity {

    private View llSettingsActivity;
    private TextView tvUploadDb;

    @Override
    public void initialize() {
        llSettingsActivity = baseInflater.inflate(R.layout.activity_geocare_settings,null);
        llBody.addView(llSettingsActivity, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        initialiseControls();

        bindControls();
    }

    private void bindControls(){
        tvUploadDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyAppDbtoSdcard();
            }
        });
    }

    public void copyAppDbtoSdcard(){
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                if(new PermissionUtils().checkPermission(GeocareSettingsActivity.this, new String[]{
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE}) != 0){
                    new PermissionUtils().verifyLocation(GeocareSettingsActivity.this,new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
                } else
                    copyFile();
            } else
                copyFile();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void copyFile(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);

            File Db = new File("/data/data/" + info.packageName + "/databases/" + GCCPConstants.DATABASE_NAME);
            Date d = new Date();

            String path = Environment.getExternalStorageDirectory() + AppConstant.EXTERNAL_FOLDER;
            LogUtils.infoLog("FOLDER_PATH", path);
            File fileDir = new File(path);
            if(!fileDir.exists())
                fileDir.mkdirs();

            File file = new File(Environment.getExternalStorageDirectory() + AppConstant.EXTERNAL_FOLDER + GCCPConstants.DATABASE_NAME);
            file.createNewFile();
            file.setWritable(true);

            FileUtils.copyFile(new FileInputStream(Db), new FileOutputStream(file));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int copyFile = 0;
        if (requestCode == 1) {
            for(int i = 0; i < permissions.length; i++){
                if(permissions[i].equalsIgnoreCase(android.Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == 1)
                    copyFile++;
                else if(permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == 1)
                    copyFile++;
            }

            if(copyFile == 2)
                copyFile();
        }
    }

    private void initialiseControls(){

        tvUploadDb = (TextView) llSettingsActivity.findViewById(R.id.tvUploadDb);
    }
}
