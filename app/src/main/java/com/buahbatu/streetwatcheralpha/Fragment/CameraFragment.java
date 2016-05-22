package com.buahbatu.streetwatcheralpha.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buahbatu.streetwatcheralpha.R;
import com.buahbatu.streetwatcheralpha.Service.CameraService;

import java.io.IOException;

public class CameraFragment extends Fragment implements View.OnClickListener{
    boolean isTaken = false;
    CameraService cameraService;
    View retake;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_camera, container, false);
        parent.findViewById(R.id.test_take).setOnClickListener(this);
        retake = parent.findViewById(R.id.test_reset);
        retake.setOnClickListener(this);
        SurfaceView cameraView = (SurfaceView)parent.findViewById(R.id.camera_view);

        cameraService = new CameraService(getContext(), cameraView);
        cameraService.start();

        return parent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_take:
                isTaken = true;
                cameraService.takePicture(onPictureReady);
                retake.setVisibility(View.VISIBLE);
                break;
            case R.id.test_reset:
                cameraService.start();
                retake.setVisibility(View.GONE);
                isTaken = false;
                break;
        }
    }

    CameraService.OnPictureReady onPictureReady = new CameraService.OnPictureReady() {
        @Override
        public void onPictureAvailable(byte[] data) {

        }
    };
}
