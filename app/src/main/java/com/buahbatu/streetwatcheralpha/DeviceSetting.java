package com.buahbatu.streetwatcheralpha;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;

import com.buahbatu.streetwatcheralpha.Service.AlertService;

/**
 * Created by maaakbar on 1/24/16.
 */
public class DeviceSetting {
    public final static String preferenceName = "StreetWatcher";
    public final static String statusKey = "status";
    final static String deviceID = "ID";
    private static Camera camera;
    public static AlertService alertService;

    public static Camera getCameraInstance(){
        return camera;
    }
    public static void setCameraInstance(Camera mCamera){
        camera = mCamera;
    }

    public static SharedPreferences getDefaultPreferences(Context context){
        return context.getSharedPreferences(DeviceSetting.preferenceName, Context.MODE_PRIVATE);
    }

    public static void setDeviceID(Context context, int id){
        getDefaultPreferences(context).edit().putInt(deviceID, id).apply();
    }

    public static boolean isDeviceHasID(Context context){
        return getDefaultPreferences(context).getInt(deviceID, -2) > 0;
    }

    public static boolean getActivationStatus(Context context){
        return getDefaultPreferences(context).getBoolean(statusKey, false);
    }

    public static void setActivationStatus(Context context, boolean status){
        getDefaultPreferences(context).edit().putBoolean(statusKey, status).apply();
    }
}
