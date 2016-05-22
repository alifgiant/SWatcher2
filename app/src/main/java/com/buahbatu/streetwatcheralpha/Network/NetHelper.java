package com.buahbatu.streetwatcheralpha.Network;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.buahbatu.streetwatcheralpha.DeviceSetting;
import com.buahbatu.streetwatcheralpha.R;

import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

/**
 * Created by maaakbar on 1/23/16.
 */
public class NetHelper {
    final static String domainServer = "StreetWatcherDomain";

    public static void StoreDomainAddress(Context context, String domain){
        SharedPreferences preferences = DeviceSetting.getDefaultPreferences(context);
        preferences.edit().putString(domainServer, domain).apply();
    }

    static String GetDomainAddress(Context context){
        SharedPreferences preferences = DeviceSetting.getDefaultPreferences(context);
        return preferences.getString(domainServer, context.getString(R.string.default_domain));
    }

    public static String GetAlertDomainAddress(Context context){
        return context.getString(R.string.default_protocol)+ GetDomainAddress(context) + context.getString(R.string.alert_url);
    }

    public static String GetLoginDomainAddress(Context context){
        return GetDomainAddress(context) + context.getString(R.string.login_url);
    }

    public static void RequestDeviceID(Context context, ContentValues values, OnDeviceIdAvailable onDeviceIdAvailable){
        ConnectingDeviceTask task = new ConnectingDeviceTask(context, onDeviceIdAvailable);
        task.execute(values);
    }

    public interface OnDeviceIdAvailable{
        void deviceIdAvailable(int id);
    }

    public static void SentEmptyAlert(Context context, String LogTAG){
        try {
            PostWebTask webTask = new PostWebTask(context,
                    new URL(NetHelper.GetAlertDomainAddress(context)));
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            entityBuilder.addTextBody(context.getString(R.string.api_coordinate),
                    context.getString(R.string.default_coordinate));
            entityBuilder.addTextBody(context.getString(R.string.api_location),
                    context.getString(R.string.default_location));
            entityBuilder.addTextBody(context.getString(R.string.api_idDev), context.getString(R.string.default_id));

            webTask.execute(entityBuilder.build());
        }catch (MalformedURLException e){
            Log.i(LogTAG, "malformed");
            e.printStackTrace();
        }
    }

    public static void SentFilledAlert(Context context, String LogTAG, HttpEntity entity){
        try {
            PostWebTask webTask = new PostWebTask(context,
                    new URL(NetHelper.GetAlertDomainAddress(context)));
            webTask.execute(entity);
        }catch (MalformedURLException e){
            Log.i(LogTAG, "malformed");
            e.printStackTrace();
        }
    }
}
