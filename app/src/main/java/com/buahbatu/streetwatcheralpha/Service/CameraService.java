package com.buahbatu.streetwatcheralpha.Service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.buahbatu.streetwatcheralpha.DeviceSetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maaakbar on 1/24/16.
 */
public class CameraService implements Service{
    @Override
    public void start(){
        try {
            // The Surface has been created, now tell the camera where to draw the preview.
            mCamera.startPreview();
        } catch (NullPointerException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void pause() {
        mCamera.stopPreview();
    }

    @Override
    public void stop() {
        if(mCamera!=null) {
            pause();
            mCamera.release();
            mCamera = null;
            DeviceSetting.setCameraInstance(mCamera);
        }
    }

    public void takePicture (OnPictureReady onPictureReadyListener){
        this.onPictureReadyListener = onPictureReadyListener;
        if (mCamera!=null)
            mCamera.takePicture(null, null, pictureCallback);
    }

    private static final String TAG = "Camera Service";
    private static  final int FOCUS_AREA_SIZE= 300;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolder;
    public int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Context mContext;
    private OnPictureReady onPictureReadyListener;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG, "Image Taken");
            onPictureReadyListener.onPictureAvailable(data);
        }
    };

    public CameraService(Context mContext, SurfaceView mSurfaceView){
        this.mSurfaceView = mSurfaceView;
        this.mContext = mContext;
//        this.mCamera = DeviceSetting.getCameraInstance();
        // add callbacks and listeners
        this.mSurfaceView.setOnTouchListener(mOnTouchListener);
        mSurfaceView.getHolder().addCallback(callback);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int cameraID) throws IOException{
        try {
            Camera camera = Camera.open(cameraID);
            // parameters for camera
            Camera.Parameters params = camera.getParameters();
            params.set("jpeg-quality", 100);
            params.set("iso", "auto");
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            params.setPictureFormat(PixelFormat.JPEG);
            // set the image dimensions
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            int max = 0, width = 0, height = 0;
            for(Camera.Size size : sizes) {
                if(max < (size.width *size.height)) {
                    max = (size.width*size.height);
                    width = size.width;
                    height = size.height;
                }
            }
            params.setPictureSize(width, height);
            camera.setParameters(params);
            return camera;
        }catch (Exception e){
            throw new IOException("No camera is available");
        }
    }

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                if (mCamera == null)
                    mCamera = getCameraInstance(currentCameraId);
                DeviceSetting.setCameraInstance(mCamera);
                // The Surface has been created, now tell the camera where to draw the preview.
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stop(); // stop camera service
        }
    };

    SurfaceView.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                focusOnTouch(event);
            }
            return false;
        }
    };

    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null ) {
            Camera.Parameters parameters = mCamera.getParameters();
            Rect rect = calculateFocusArea(event.getX(), event.getY());
            if (parameters.getMaxNumMeteringAreas() > 0){
                Log.i(TAG, "fancy !");

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);

                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }else {
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("tap_to_focus","success!");
            } else {
                // do something...
                Log.i("tap_to_focus", "fail!");
            }
        }
    };

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mSurfaceView.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mSurfaceView.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }
    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper)+focusAreaSize/2>1000){
            if (touchCoordinateInCameraReper>0){
                result = 1000 - focusAreaSize/2;
            } else {
                result = -1000 + focusAreaSize/2;
            }
        } else{
            result = touchCoordinateInCameraReper - focusAreaSize/2;
        }
        return result;
    }

    public interface OnPictureReady{
        void onPictureAvailable(byte[] data);
    }
}
