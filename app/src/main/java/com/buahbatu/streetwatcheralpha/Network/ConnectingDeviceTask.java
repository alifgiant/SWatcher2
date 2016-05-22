package com.buahbatu.streetwatcheralpha.Network;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by maaakbar on 1/23/16.
 */
public class ConnectingDeviceTask extends AsyncTask<ContentValues, Void, Integer> {
    Context context;
    NetHelper.OnDeviceIdAvailable onDeviceIdAvailable;

    ProgressDialog progressDialog;

    public ConnectingDeviceTask(Context context, NetHelper.OnDeviceIdAvailable onDeviceIdAvailable) {
        this.context = context;
        this.onDeviceIdAvailable = onDeviceIdAvailable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "", "Connecting...", true, false);
    }

    @Override
    protected Integer doInBackground(ContentValues... params) {
//        return null;
//        return -1;
        return 1232;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        progressDialog.dismiss();
        onDeviceIdAvailable.deviceIdAvailable(integer==null ? -2 : integer);
    }
}
