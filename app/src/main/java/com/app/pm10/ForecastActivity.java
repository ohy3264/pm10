package com.app.pm10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import fragment.PM10ForecastFragment;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import utill.Constant;
import utill.HYFont;

/**
 * Created by oh on 2015-02-26.
 */
public class ForecastActivity extends ActionBarActivity implements MaterialTabListener {

    // Log
    private static final String TAG = "ForecastActivity";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    // ActionBar
    private Toolbar mToolbar;

    private TextView mTxtGuide;
    //TabHost
    private MaterialTabHost mTabHost;

    //ViewPager
    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;

    //fragment
    private PM10ForecastFragment mPm25ForeFragment;
    private PM10ForecastFragment mPm10ForeFragment;
    private PM10ForecastFragment mPmO3ForeFragment;
    private HYFont mFont;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //Utill
        mFont = new HYFont(this);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);

            mFont.setGlobalFont(root);

        //ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        if (mToolbar != null) {
            mToolbar.setTitle("실황정보");
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
                            .setTabListener(ForecastActivity.this)
            );

        }

        mTxtGuide = (TextView) findViewById(R.id.txtGuide);
        mTxtGuide.setSelected(true);

        /*FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_mainContainer);

        if (fragment == null) {
            fragment = new PM10ForecastFragment();
            fm.beginTransaction().add(R.id.fragment_mainContainer, fragment)
                    .commit();
        }
*/

        //FragmentSet
        mPm10ForeFragment = new PM10ForecastFragment();
        mPm25ForeFragment = new PM10ForecastFragment();
        mPmO3ForeFragment = new PM10ForecastFragment();


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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_none, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
            Bundle arguments = new Bundle();
            switch (num) {

                case 0:
                    arguments.putString("InformCode" , "PM10");
                    mPm10ForeFragment.setArguments(arguments );
                    return mPm10ForeFragment;
                case 1:

                    arguments.putString("InformCode" , "PM25");
                    mPm25ForeFragment.setArguments(arguments );
                    return mPm25ForeFragment;
                case 2:

                    arguments.putString("InformCode" , "O3");
                    mPmO3ForeFragment.setArguments(arguments );
                    return mPmO3ForeFragment;

            }
            return new Fragment();

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:

                    return "PM10";
                case 1:

                    return "PM2.5";

                case 2:

                    return "O3";
            }
            return "";
        }
    }


}

