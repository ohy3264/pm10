package com.app.pm10;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import dialog.GpsSettingDialog;
import dialog.GpsSidoSelectDialog;
import fragment.SidoAddressFragment;
import geotranc.GeoPoint;
import geotranc.GeoTrans;
import model.NearbyModel;
import rest.CallRest_Nearby;
import utill.Constant;
import utill.GpsInfo;
import utill.HYFont;
import utill.HYPreference;
import xmlreader.XmlReader_Nearby;

/**
 * Created by oh on 2015-02-26.
 */
public class StationSelectActivity extends ActionBarActivity {

    // Log
    private static final String TAG = "StationSelectActivity";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    // view
    private ProgressDialog mProgressDialog;

    // ActionBar
    private Toolbar mToolbar;

    // Location
    private GpsInfo mGps;
    private Double lat, lng;

    // Dialog
    private GpsSettingDialog mGpsSettingDig;
    private GpsSidoSelectDialog mGpsSidoSelDig;

    // Data Set
    private ArrayList<NearbyModel> nearbyList = new ArrayList<NearbyModel>();


    // utill
    private GeoPoint in_pt, tm_pt;
    private HYFont mFont;
    private HYPreference mPref;
    private CountDownLatch requestCallLatch_Nearby, requestCallLatch_Tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_address);

        //Utill
        mPref = new HYPreference(this);
        mFont = new HYFont(this);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            mFont.setGlobalFont(root);
        //ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("측정소 선택");
            mToolbar.setBackgroundColor(Constant.ACTIONBAR_COLOR);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mFont.setGlobalFont((ViewGroup) mToolbar);
        }


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_mainContainer);

        if (fragment == null) {
            fragment = new SidoAddressFragment();
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
        getMenuInflater().inflate(R.menu.menu_addr, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_gps:
                myLocation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @author : oh
     * @MethodName : myLocation
     * @Day : 2014. 11. 4.
     * @Time : 오후 3:06:33
     * @Explanation : GPS기반으로 현재 위경도를 TM으로 변환
     */

    void myLocation() {
        // LocationListiener의 핸들을 얻음
        // GPS 사용유무 가져오기
        mGps = new GpsInfo(this);
        if (mGps.isGetLocation()) {
            if (INFO)
                Log.i(TAG, "GPS 세팅이 되어있습니다. ");
            requestCallLatch_Tm = new CountDownLatch(1);
            lat = mGps.getLatitude();
            lng = mGps.getLongitude();

            in_pt = new GeoPoint(lng, lat);
            tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);

            if (DEBUG) {
                Log.d(TAG, "GEO_X" + Double.toString(in_pt.getX()));
                Log.d(TAG, "GEO_Y" + Double.toString(in_pt.getY()));

                Log.d(TAG, "TM_X" + Double.toString(tm_pt.getX()));
                Log.d(TAG, "TM_Y" + Double.toString(tm_pt.getY()));
            }
            requestCallLatch_Tm.countDown();
            asyncTaskCall();


        } else {
            if (INFO)
                Log.i(TAG, "GPS 세팅이 되어있지 않습니다.");
            //   mGps.showSettingsAlert();
            mGpsSettingDig = new GpsSettingDialog();
            mGpsSettingDig.show(getSupportFragmentManager(), "MYTAG");
        }
        // ADmob
        LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
        AdView ad = new AdView(this);
        ad.setAdUnitId("ca-app-pub-8578540426651700/3807365273");
        ad.setAdSize(AdSize.BANNER);
        layout.addView(ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);

    }


    /**
     * @author : oh
     * @MethodName : requestCallRest_Nearby
     * @Day : 2014. 11. 20.
     * @Time : 오후 1:11:52
     * @Explanation : 가까운 측정소 찾기 요청
     */
    public void requestCallRest_Nearby() {
        if (INFO)
            Log.i(TAG, "가까운 측정소 찾기 요청");
        requestCallLatch_Nearby = new CountDownLatch(1);
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    requestCallLatch_Tm.await();
                    String xml;
                    xml = CallRest_Nearby.restClient(
                            Double.toString(tm_pt.getX()),
                            Double.toString(tm_pt.getY()));
                    nearbyList = XmlReader_Nearby.airpollution_parser(xml);
                    requestCallLatch_Nearby.countDown();
                    if (DEBUG)
                        Log.d(TAG + " xml : ", xml);
                    if (INFO)
                        Log.i(TAG, "가까운 측정소 찾기 요청 성공");

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    if (INFO)
                        Log.i(TAG, "가까운 측정소 찾기 요청 실패");
                    e.printStackTrace();
                    requestCallLatch_Nearby.countDown();
                }
            }
        }).start();
    }


    /**
     * @author : oh
     * @MethodName : asyncTaskCall
     * @Day : 2014. 11. 20.
     * @Time : 오후 1:12:41
     * @Explanation : asyncTask 호출부
     */
    public void asyncTaskCall() {
        new asyncTask().execute();
    }

    /**
     * <pre>
     *
     * @author : oh
     * @Day : 2014. 11. 20.
     * @Time : 오후 1:12:52
     * @Explanation : asyncTask
     * - 가까운 측정소 찾아서 Alert 다이어로그 호출
     * </pre>
     */
    private class asyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = showLoadingDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            requestCallRest_Nearby();
            try {
                requestCallLatch_Nearby.await();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mProgressDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try{
                mGpsSidoSelDig = new GpsSidoSelectDialog();
                Bundle args = new Bundle();
                args.putString("NEAR", nearbyList.get(0)
                        .getStationName().toString());
                mGpsSidoSelDig.setArguments(args);

                mGpsSidoSelDig.show(getSupportFragmentManager(), "MYTAG");
                // showStationAlert();
            }catch (Exception e){

            }

            mProgressDialog.dismiss();
        }
    }

    /**
     * @return
     * @author : oh
     * @MethodName : showLoadingDialog
     * @Day : 2014. 11. 20.
     * @Time : 오후 1:26:10
     * @Explanation : Loding Progress Dialog
     */
    public ProgressDialog showLoadingDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loding..");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }
}

