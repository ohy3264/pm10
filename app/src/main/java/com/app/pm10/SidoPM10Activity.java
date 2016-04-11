package com.app.pm10;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import fragment.SidoPm10Fragment;
import utill.Constant;
import utill.HYFont;

/**
 * Created by oh on 2015-02-26.
 */
public class SidoPM10Activity extends ActionBarActivity {

    // Log
    private static final String TAG = "SidoPM10Activity";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    // ActionBar
    private Toolbar mToolbar;
    private HYFont mFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sido_pm10);
        //Utill
        mFont = new HYFont(this);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            mFont.setGlobalFont(root);

        //ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("지역별 대기오염");
            mToolbar.setBackgroundColor(Constant.ACTIONBAR_COLOR);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mFont.setGlobalFont((ViewGroup) mToolbar);
        }


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_mainContainer);

        if (fragment == null) {
            fragment = new SidoPm10Fragment();
            fm.beginTransaction().add(R.id.fragment_mainContainer, fragment)
                    .commit();
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

}

