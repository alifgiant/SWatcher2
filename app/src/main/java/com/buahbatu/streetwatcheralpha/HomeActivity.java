package com.buahbatu.streetwatcheralpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;

import com.buahbatu.streetwatcheralpha.Service.AlertService;
import com.buahbatu.streetwatcheralpha.Service.CameraService;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    final static int ActivationRequest = 1;
    final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (!DeviceSetting.isDeviceHasID(this)) { // if device didn't has id
            // Ask for Login
            Intent move = new Intent(this, LoginActivity.class);
            startActivity(move);
            finish();
        }else { // if device has id
            // set our toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
            toolbar.setTitle("What To Do!");
            setSupportActionBar(toolbar);
            // set on click listener
            findViewById(R.id.setup_button).setOnClickListener(this);
            findViewById(R.id.status_button).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent move = null;
        switch (v.getId()){
            case R.id.setup_button:
                move = new Intent(this, SetupActivity.class);
                break;
            case R.id.status_button:
                move = new Intent(this, StatusActivity.class);
                pauseService();
                break;
        }
        if (move != null)
            startActivityForResult(move, ActivationRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void enableService(){
        SurfaceView view = (SurfaceView)findViewById(R.id.camera_view);
        DeviceSetting.alertService = new AlertService(this, view, true);
        DeviceSetting.alertService.start();
    }

    void pauseService(){
        try {
            DeviceSetting.alertService.pause();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void shutdownService(){
        DeviceSetting.alertService.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ActivationRequest && resultCode==RESULT_OK){
            Log.i(TAG, "onActivityResult " + DeviceSetting.getActivationStatus(this));
            boolean turnOn = DeviceSetting.getActivationStatus(this);
            if (turnOn)
                enableService();
            else
                shutdownService();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_disconnect) {
            // Logout
            Intent move = new Intent(this, LoginActivity.class);
            startActivity(move);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
