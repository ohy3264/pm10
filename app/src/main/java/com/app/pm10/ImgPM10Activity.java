package com.app.pm10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import fragment.Pm10ImgFragment;
import fragment.Pm25ImgFragment;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import utill.Constant;
import utill.HYFont;
import utill.HYPreference;

/**
 * Created by oh on 2015-02-26.
 */
public class ImgPM10Activity extends ActionBarActivity implements MaterialTabListener {
    // Log
    private static final String TAG = "StationSelectActivity";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    // ActionBar
    private Toolbar mToolbar;

    // utill
    private HYFont mFont;
    private HYPreference mPref;

    //TabHost
    private MaterialTabHost mTabHost;

    //ViewPager
    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;

    private TextView mTxtGuide;
    //fragment
    private Pm25ImgFragment mPm25ImgFragment;
    private Pm10ImgFragment mPm10ImgFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        //Utill
        mPref = new HYPreference(this);
        mFont = new HYFont(this);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        mFont.setGlobalFont(root);

        mTxtGuide = (TextView) findViewById(R.id.txtGuide);
        mTxtGuide.setSelected(true);

        //ActionBar, TabHost
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        if (mToolbar != null) {
            mToolbar.setTitle("시간별 대기오염");
            mToolbar.setBackgroundColor(Constant.ACTIONBAR_COLOR);
            mTabHost.setPrimaryColor(Constant.ACTIONBAR_COLOR);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mFont.setGlobalFont((ViewGroup) mToolbar);
        }



        // init view pager

        mViewPager = (ViewPager) this.findViewById(R.id.pager );
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                mTabHost.setSelectedNavigationItem(position);

            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setText(mPagerAdapter.getPageTitle(i))
                            .setTabListener(ImgPM10Activity.this)
            );

        }

        // ADmob
        if(Constant.ADMOB){
            LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
            layout.setVisibility(View.VISIBLE);
            AdView ad = new AdView(this);
            ad.setAdUnitId("ca-app-pub-8578540426651700/3807365273");
            ad.setAdSize(AdSize.BANNER);
            layout.addView(ad);
            AdRequest adRequest = new AdRequest.Builder().build();
            ad.loadAd(adRequest);
        }

        //FragmentSet
        mPm10ImgFragment = new Pm10ImgFragment();
        mPm25ImgFragment = new Pm25ImgFragment();

       /* mSettingFragment = new SettingFragment();
        pm10ForecastFragment = new PM10ForecastFragment();
        mPm10ImgFragment = new Pm10ImgFragment();*/
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }
        public Fragment getItem(int num) {
            switch (num) {

                case 0:
                    return mPm10ImgFragment;
                case 1:
                    return mPm25ImgFragment;

            }
            return new Fragment();

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:

                    return "PM10";
                case 1:

                    return "PM2.5";
            }
            return "";
        }
    }



}

