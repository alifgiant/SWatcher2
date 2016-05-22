package com.buahbatu.streetwatcheralpha;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.buahbatu.streetwatcheralpha.Fragment.CameraFragment;
import com.buahbatu.streetwatcheralpha.Fragment.IdFragment;
import com.buahbatu.streetwatcheralpha.Fragment.MicFragment;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        findViewById(R.id.back_button).setOnClickListener(this);

        // set up the tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setTabView(tabLayout);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final SetupPagerAdapter adapter = new SetupPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void setTabView(TabLayout tabLayout){
        tabLayout.addTab(tabLayout.newTab().setText("Device ID"));
        tabLayout.addTab(tabLayout.newTab().setText("Camera Test"));
        tabLayout.addTab(tabLayout.newTab().setText("Mic Test"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.back_button) {

        }else
            finish();
    }

    class SetupPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public SetupPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    //Fragment for ID Tab
                    return new IdFragment();
                case 1:
                    //Fragement for CameraService Tab
                    return new CameraFragment();
                case 2:
                    //Fragement for CameraService Tab
                    return new MicFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mNumOfTabs; //No of Tabs
        }
    }
}
