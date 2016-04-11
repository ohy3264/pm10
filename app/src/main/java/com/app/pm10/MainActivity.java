package com.app.pm10;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.tistory.whdghks913.croutonhelper.CroutonHelper;
import com.winkletop.sdk.WinkleTopConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import adapter.DrawerMenuAdapter;
import dialog.GpsSettingDialog;
import fragment.StationFragment;
import geotranc.GeoPoint;
import geotranc.GeoTrans;
import model.NearbyModel;
import model.SampleItem;
import rest.CallRest_Nearby;
import service.HYService;
import utill.Constant;
import utill.GpsInfo;
import utill.HYBackPressCloserHandler;
import utill.HYFont;
import utill.HYNetworkInfo;
import utill.HYPreference;
import xmlreader.XmlReader_Nearby;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, StationFragment.OnHeadlineSelectedListener {
    public static final String TAG = "MainActivity";
    public static final boolean DEBUG = false;
    public static final boolean INFO = true;

    // View
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ProgressDialog mProgressDialog;
    private DrawerMenuAdapter mDrawerAdapter;
    private ActionBarDrawerToggle mDToggle;

    // ActionBar
    private Toolbar mToolbar;

    //Fragment
    private FragmentManager mFragManager;
    private StationFragment mFragment_Station;

    // Location
    private GpsInfo mGps;

    // Dialog
    private GpsSettingDialog mGpsSettingDig;

    // Data Set
    private ArrayList<NearbyModel> nearbyList = new ArrayList<NearbyModel>();
    private ArrayList<SampleItem> mMenuArrayList = new ArrayList<SampleItem>();
    private Double lat, lng;

    // Crouton
    private CroutonHelper mCroutonHelper;
    private View mCroutonView;
    private TextView mTxtCrouton;


    //Frag
    private Boolean mDToggleisShow = false;


    //Widget Update Manager
    private PendingIntent mAlarmSender;
    private AlarmManager mAlarmMgr;

    //Utill
    private HYNetworkInfo mNetorkInfo;
    private HYBackPressCloserHandler mBackPressCloseHandler; // 뒤로가기 종료 핸들러
    private HYFont mFont;
    private HYPreference mPref;
    private GeoPoint in_pt, tm_pt;
    private CountDownLatch requestCallLatch_Nearby, requestCallLatch_Tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Utill
        mBackPressCloseHandler = new HYBackPressCloserHandler(this);
        mFont = new HYFont(this);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        mFont.setGlobalFont(root);
        mPref = new HYPreference(this);
        mNetorkInfo = new HYNetworkInfo(this);

        // Crouton
        mCroutonHelper = new CroutonHelper(this);
        mCroutonView = getLayoutInflater().inflate(
                R.layout.crouton_custom_view, null);
        mTxtCrouton = (TextView) mCroutonView.findViewById(R.id.txt_crouton);

        actionBarinit();
        drawerinit();


        //Fragment Container
        mFragment_Station = new StationFragment();
        mFragManager = getFragmentManager();
        mFragManager.beginTransaction().add(R.id.container, mFragment_Station).commit();

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

        connectSDK();

        if (!mPref.getValue(mPref.KEY_SERVICE_REGISTERED, false)) {
            Log.i(TAG,"첫 실행");
            requestWidgetAlarm();
            mPref.put(mPref.KEY_SERVICE_REGISTERED, true);
        } else {
            Log.i(TAG,"재 실행");

        }

    }
    private void connectSDK(){
        WinkleTopConnection connection = new WinkleTopConnection(MainActivity.this, new WinkleTopConnection.OnServiceConnectListener() {
            @Override
            public void onServiceConnected(WinkleTopConnection winkleTopConnection) {
                Log.d("CONNCT", "onServiceConnected");
            }
        });
        connection.bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAlarmMgr.cancel(mAlarmSender);

    }

    public void actionBarinit() {
        //ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("PM10");
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        mFont.setGlobalFont((ViewGroup) mToolbar);
    }

    public void drawerinit() {
        //DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDToggleisShow = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDToggleisShow = false;
            }
        };
        mDrawerLayout.setDrawerListener(mDToggle);
        //DrawerList
        menuDataSet();
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerAdapter = new DrawerMenuAdapter(this, mMenuArrayList);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public void onArticleSelected(String tag) {
        switch (tag) {
            case "StationFragment":
                if (INFO)
                    Log.i(TAG, "StationFragment Attach");
                break;
        }
    }

    @Override
    public void onNetworkInfo(String msg) {
        mTxtCrouton.setText(msg);
        mCroutonHelper.setCustomView(mCroutonView);
        mCroutonHelper.show();
    }

    @Override
    public void onActionBarColor(int color) {
        //ActionBar
        mToolbar.setBackgroundColor(color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

            case R.id.action_gps:
                myLocation();
                asyncTaskCall();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void menuDataSet() {
        SampleItem item1 = new SampleItem("측정소 선택", R.drawable.menu_station_icon);
        SampleItem item2 = new SampleItem("지역별 대기오염", R.drawable.menu_map_icon);
        SampleItem item3 = new SampleItem("시간별 대기오염", R.drawable.menu_time_icon);
        SampleItem item4 = new SampleItem("실황정보", R.drawable.menu_fore_icon);
        SampleItem item5 = new SampleItem("설정", R.drawable.icon_setting);
        SampleItem item6 = new SampleItem("문의하기", R.drawable.menu_mail_icon);
        SampleItem item7 = new SampleItem("APP 평가하기", R.drawable.menu_market_icon);
        SampleItem item8 = new SampleItem("Clean Version", R.drawable.menu_clean_icon);
        mMenuArrayList.add(item1);
        mMenuArrayList.add(item2);
        mMenuArrayList.add(item3);
        mMenuArrayList.add(item4);
        mMenuArrayList.add(item5);
        mMenuArrayList.add(item6);
         mMenuArrayList.add(item7);
        mMenuArrayList.add(item8);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {
            //mDrawerLayout.closeDrawer(mDrawerList);
            // selectItem(position);
            closeDrawer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (! mNetorkInfo.networkgetInfo()) {
                        switch (position) {
                            case 0:
                                Intent intent_addr = new Intent(getApplicationContext(), StationSelectActivity.class);
                                intent_addr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent_addr);
                                overridePendingTransition(0, 0);
                                break;
                            case 1:
                                Intent intent_sido = new Intent(getApplicationContext(), SidoPM10Activity.class);
                                intent_sido.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent_sido);
                                overridePendingTransition(0, 0);
                                break;
                            case 2:
                                Intent intent_img = new Intent(getApplicationContext(), ImgPM10Activity.class);
                                intent_img.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent_img);
                                overridePendingTransition(0, 0);
                                break;
                            case 3:
                                Intent intent_fore = new Intent(getApplicationContext(), ForecastActivity.class);
                                intent_fore.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent_fore);
                                overridePendingTransition(0, 0);
                                break;

                            case 4:
                                Intent intent_setting = new Intent(getApplicationContext(), SettingActivity.class);
                                intent_setting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent_setting);
                                overridePendingTransition(0, 0);
                                break;

                            case 5:

                                Intent emailSend = new Intent("android.intent.action.SEND");
                                emailSend.setType("plain/text");
                                emailSend.putExtra("android.intent.extra.EMAIL",
                                        new String[] {"ohy3264@gmail.com"});
                                startActivity(emailSend);
                                break;
                            case 6:

                                Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                                marketLaunch
                                        .setData(Uri
                                                .parse("https://market.android.com/details?id=com.app.pm10"));
                                startActivity(marketLaunch);
                                break;
                            case 7:

                                Intent cleanVersion = new Intent(Intent.ACTION_VIEW);
                                cleanVersion
                                        .setData(Uri
                                                .parse("https://market.android.com/details?id=com.app.pm10clean"));
                                startActivity(cleanVersion);
                                break;

                        }
                    } else {
                        mTxtCrouton.setText("네트워크를 확인해 주세요");
                        mCroutonHelper.setCustomView(mCroutonView);
                        mCroutonHelper.show();
                    }
                }
            }, 250);
        }
    }

    void myLocation() {
        requestCallLatch_Tm = new CountDownLatch(1);
        mGps = new GpsInfo(this);
        if (mGps.isGetLocation()) {
            if (INFO)
                Log.i(TAG, "GPS 세팅이 되어있습니다. ");
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
        } else {
            if (INFO)
                Log.i(TAG, "GPS 세팅이 되어있지 않습니다.");
            //mGps.showSettingsAlert();
            mGpsSettingDig = new GpsSettingDialog();
            mGpsSettingDig.show(getSupportFragmentManager(), "MYTAG");
            requestCallLatch_Tm.countDown();
        }
    }


    public void requestCallRest_Nearby() {
        if (INFO)
            Log.i(TAG, "가까운 측정소 찾기 시작");
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
                        Log.i(TAG, "가까운 측정소 찾기 성공");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    if (INFO)
                        Log.i(TAG, "가까운 측정소 찾기 실패");
                    e.printStackTrace();
                    requestCallLatch_Nearby.countDown();
                }
            }
        }).start();
    }

    public void asyncTaskCall() {
        new asyncTask().execute();
    }

    /**
     * <pre>
     *
     * @author : oh
     * @Day : 2014. 11. 20.
     * @Time : 오전 11:24:38
     * @Explanation : asyncTask
     * - 가까운 측정소 찾아서 Pref 저장
     * - MainActivity 갱신..
     * </pre>
     */
    private class asyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            requestCallRest_Nearby();
            try {
                requestCallLatch_Nearby.await();
                mPref.put(mPref.KEY_STATION, nearbyList.get(0).getStationName());

                /*Intent stationintent = new Intent(getApplicationContext(),
                        MainActivity.class);
                stationintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(stationintent);
                finish();*/
                //Fragment Container
                mFragment_Station = new StationFragment();
                mFragManager = getFragmentManager();
                mFragManager.beginTransaction().replace(R.id.container, mFragment_Station).commit();


                if (INFO)
                    Log.i(TAG, "MainActivy 갱신 성공");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (INFO)
                    Log.i(TAG, "MainActivy 갱신 실패");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // 단말기의 BACK버튼
                    Log.d(TAG, "BACK");
                    if (mDToggleisShow) {
                        //mDrawerLayout.closeDrawer(mDrawerList);
                        closeDrawer();
                    } else {
                        mBackPressCloseHandler.onBackPressed();
                    }
                    return true;
                case KeyEvent.KEYCODE_MENU:
                    // 단말기의 메뉴버튼
                    Log.d(TAG, "MENU");
                    if (mDToggleisShow) {
                        //mDrawerLayout.closeDrawer(mDrawerList);
                        closeDrawer();
                    } else {
                        mDrawerLayout.openDrawer(mDrawerList);
                    }
                    return true;
            }

        }
        return super.dispatchKeyEvent(event);
    }

    protected void closeDrawer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                });
            }
        }).start();
    }

    public void requestWidgetAlarm() {
        //Widget Update Manager
        Calendar cal = Calendar.getInstance(); // 현재시점의 객체를 가져옴
        cal.set(Calendar.MINUTE, 35);
        Log.d("설정 시간", cal.getTime().toString());
        mAlarmSender = PendingIntent.getService(MainActivity.this,
                0, new Intent(MainActivity.this, HYService.class), 0);
        mAlarmMgr = (AlarmManager) getSystemService(MainActivity.this.ALARM_SERVICE);

        mAlarmMgr.setRepeating(AlarmManager.RTC, cal.getTime().getTime(), 60 * 60 * 1000, mAlarmSender);
    }
}
