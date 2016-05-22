package com.buahbatu.streetwatcheralpha.Service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.buahbatu.streetwatcheralpha.Network.NetHelper;
import com.buahbatu.streetwatcheralpha.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

/**
 * Created by maaakbar on 1/25/16.
 */
public class AlertService implements Service {
    @Override
    public void start() {
        cameraService.start();
        micService.start();

        // running reader
        mHitCount = 0;
        mRunning = true;
        if (micService.ar != null) {
            Log.i(TAG, "tread started");
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    }

    @Override
    public void stop() {
        // stopping reading
        mRunning = false;

        cameraService.stop();
        micService.stop();
    }

    public void pause(){
        // stopping reading
        mRunning = false;
        micService.pause();
        cameraService.pause();
    }

    SurfaceView mSurfaceView;


    /* constants */
    private static final String TAG = "SoundAlert";
    private static final int POLL_INTERVAL = 50;  // default 300 ms
    private static final int NO_NUM_DIALOG_ID=1;

    /** running state **/
    private boolean mRunning = false;
    private int mHitCount =0;

    /** config state **/
    private int mThreshold = 8000; //default value
    private Context mContext;
    private boolean isTakingPicture;

    /** Other Service **/
    CameraService cameraService;
    MicService micService;

    public AlertService(Context mContext, SurfaceView mSurfaceView, boolean isTakingPicture) {
        this.mContext = mContext;
        this.mSurfaceView = mSurfaceView;
        this.isTakingPicture = isTakingPicture;
        cameraService = new CameraService(mContext, mSurfaceView);
        micService = new MicService();
    }

    private Handler mHandler = new Handler();

    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = micService.getAmplitude();
            mThreshold = getCurrentThreshold();
            if ((amp > mThreshold)) {
                Log.i(TAG, "Above" + Double.toString(micService.getAmplitude()));
                mHitCount++;
                if (mHitCount > 1){
//                    Log.i(TAG, "callForHelp called");
                    callForHelp();
                    mHitCount = 0;
                }
            }else{
                mHitCount=0;
                Log.i(TAG, "below"+Double.toString(micService.getAmplitude()));
            }
            insertToLastRead(amp);
            if (mRunning)
                mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    private ArrayList<Double> lastRead = new ArrayList<>();
    private void insertToLastRead(double currentRead){
        lastRead.add(currentRead);
        if (lastRead.size() > 5)
            lastRead.remove(0);
    }
    private int getCurrentThreshold(){
        if (lastRead.size()<5)return mThreshold;
        int tempThres = 0;
        for (double read:lastRead) {
            tempThres += read;
        }
        tempThres /= 5;
        return tempThres * 2; // * n for multiplication
    }

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(byte[] data) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void callForHelp(){
        Log.i(TAG, "callForHelp called");
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addTextBody(mContext.getString(R.string.api_coordinate), mContext.getString(R.string.default_coordinate));
        entityBuilder.addTextBody(mContext.getString(R.string.api_location), mContext.getString(R.string.default_location));
        entityBuilder.addTextBody(mContext.getString(R.string.api_idDev), mContext.getString(R.string.default_id));

        NetHelper.SentFilledAlert(mContext, TAG, entityBuilder.build());

//        // take picture
//        cameraService.takePicture(new CameraService.OnPictureReady() {
//            @Override
//            public void onPictureAvailable(byte[] data) {
//
//                // get the base 64 string
//                String imgString = Base64.encodeToString(getBytesFromBitmap(data), Base64.NO_WRAP);
//
//                // content of alert
//                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//                entityBuilder.addTextBody(mContext.getString(R.string.api_coordinate), mContext.getString(R.string.default_coordinate));
//                entityBuilder.addTextBody(mContext.getString(R.string.api_location), mContext.getString(R.string.default_location));
//                entityBuilder.addTextBody(mContext.getString(R.string.api_idDev), mContext.getString(R.string.default_id));
////                entityBuilder.addTextBody(mContext.getString(R.string.api_imgAlert), "testcoba"); // put image here
////                entityBuilder.addBinaryBody(mContext.getString(R.string.api_imgAlert), data);
//                // end of content
//
//                // sender of alert
//                NetHelper.SentFilledAlert(mContext, TAG, entityBuilder.build());
//            }
//        });

        Toast.makeText(mContext, "Alert Sent", Toast.LENGTH_SHORT).show();
    }
}
