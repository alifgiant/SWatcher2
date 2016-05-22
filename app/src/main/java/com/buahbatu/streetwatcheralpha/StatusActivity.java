package com.buahbatu.streetwatcheralpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener{

    boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        findViewById(R.id.status_button).setOnClickListener(this);
        findViewById(R.id.activation_button).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
        isActive = DeviceSetting.getActivationStatus(this);
        setActivationView();
    }

    void setActivationView(){
        if (isActive){
            ((ImageButton)findViewById(R.id.status_button)).setImageDrawable(getResources().getDrawable(R.drawable.power_buttons_on));
            findViewById(R.id.activation_button).setBackgroundColor(getResources().getColor(R.color.Red));
            ((Button)findViewById(R.id.activation_button)).setText(getString(R.string.deactivate));
            ((TextView)findViewById(R.id.status_text)).setText(getString(R.string.status_active));
        }else {
            ((ImageButton)findViewById(R.id.status_button)).setImageDrawable(getResources().getDrawable(R.drawable.power_buttons_off));
            findViewById(R.id.activation_button).setBackgroundColor(getResources().getColor(R.color.Green));
            ((Button)findViewById(R.id.activation_button)).setText(getString(R.string.activate));
            ((TextView)findViewById(R.id.status_text)).setText(getString(R.string.status_nonactive));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.back_button) {
            isActive = !isActive;
            DeviceSetting.setActivationStatus(this, isActive);
            setActivationView();
        }else
            finishActivity();
    }

    void finishActivity(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
        super.onBackPressed();
    }
}
