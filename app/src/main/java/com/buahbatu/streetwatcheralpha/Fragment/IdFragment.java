package com.buahbatu.streetwatcheralpha.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.buahbatu.streetwatcheralpha.Network.NetHelper;
import com.buahbatu.streetwatcheralpha.R;

/**
 * Created by maaakbar on 1/24/16.
 */
public class IdFragment extends Fragment implements View.OnClickListener{
    final static String TAG = "idFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_id, container, false);
        parent.findViewById(R.id.test_button).setOnClickListener(this);
        return parent;
    }

    @Override
    public void onClick(View v) {
        NetHelper.SentEmptyAlert(getContext(), TAG);
        Toast.makeText(getContext(), "Alert Sent", Toast.LENGTH_LONG).show();
    }
}
