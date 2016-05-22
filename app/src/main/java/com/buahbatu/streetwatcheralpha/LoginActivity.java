package com.buahbatu.streetwatcheralpha;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.buahbatu.streetwatcheralpha.Network.NetHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    final String TAG = "LoginActivity";
    EditText domainText, auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        domainText = (EditText)findViewById(R.id.server_name_text);
        auth = (EditText)findViewById(R.id.auth_text);

        // set login button interaction
        findViewById(R.id.login_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!domainText.getText().toString().isEmpty() && !auth.getText().toString().isEmpty()) {
            // store the domain name
            NetHelper.StoreDomainAddress(this, domainText.getText().toString());

            ContentValues values = new ContentValues();
            values.put("domain", domainText.getText().toString());
            values.put("auth", auth.getText().toString());
            final Context context = this;
            NetHelper.RequestDeviceID(this, values, new NetHelper.OnDeviceIdAvailable() {
                @Override
                public void deviceIdAvailable(int id) {
                    switch (id) {
                        case -2:
                            Toast.makeText(context, "Wrong domain, recheck your input", Toast.LENGTH_SHORT).show();
                            break;
                        case -1:
                            Toast.makeText(context, "Wrong auth, recheck your input", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            DeviceSetting.setDeviceID(context, id);
                            Intent move = new Intent(context, HomeActivity.class);
                            startActivity(move);
                            finish();
                            break;
                    }
                }
            });


        }else
            Toast.makeText(this, "Please fill the blank", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Clicked");
    }
}
