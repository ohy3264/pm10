package service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.pm10.MainActivity;
import com.app.pm10.R;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import model.StationModel;
import utill.HYPreference;

/*
 * 서비스 순서
 * onCreate() → onStartCommand() → Service Running → onDestroy()
 */
public class HYService extends IntentService {
    // Log
    private static final String TAG = "HYService";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;
    private Bitmap bmBigPicture;
    private RequestQueue mRequestQueue;
    private HYPreference mPref;
    private CountDownLatch requestCallLatch_Station;
    // DataSet
    private StationModel mAir_Station = new StationModel();


    public HYService() {
        super(TAG);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mPref = new HYPreference(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        if (mPref.getValue(mPref.KEY_WIDGET_STATE, "disabled").equals("enabled") || mPref.getValue(mPref.KEY_ALARM_STATE, "disabled").equals("enabled"))
            asyncTaskCall();


    }

    public boolean peroid_Time() {
        Calendar currentTime = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, mPref.getValue(mPref.KEY_TIME_START_HOUR, 0));
        startTime.set(Calendar.MINUTE, mPref.getValue(mPref.KEY_TIME_START_MIN, 0));

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, mPref.getValue(mPref.KEY_TIME_END_HOUR, 0));
        endTime.set(Calendar.MINUTE, mPref.getValue(mPref.KEY_TIME_END_MIN, 0));

        Log.d("변환전 시작시간", startTime.getTime().toString() + " long :: " + startTime.getTimeInMillis());
        Log.d("변환전 종료시간", endTime.getTime().toString() + " long :: " + endTime.getTimeInMillis());
        Log.d("변환전 현재시간", currentTime.getTime().toString() + " long :: " + currentTime.getTimeInMillis());

        // 시작시간이 종료시간보다 크면 종료시간의 날짜를 하루 뒤로
        if (startTime.getTimeInMillis() > endTime.getTimeInMillis())
            endTime.add(Calendar.DAY_OF_MONTH, 1);
        // 시작시간이 현재시간보다 크면 현재시간의 날짜를 하루 뒤로
        if (startTime.getTimeInMillis() > currentTime.getTimeInMillis()){
            currentTime.add(Calendar.DAY_OF_MONTH, 1);
        }



        Log.d("시작시간", startTime.getTime().toString() + " long :: " + startTime.getTimeInMillis());
        Log.d("종료시간", endTime.getTime().toString() + " long :: " + endTime.getTimeInMillis());
        Log.d("현재시간", currentTime.getTime().toString() + " long :: " + currentTime.getTimeInMillis());


        if (currentTime.getTimeInMillis() > startTime.getTimeInMillis() && currentTime.getTimeInMillis() < endTime.getTimeInMillis()) {
            Log.d(TAG, "시작과 종료 사이");
            return true;
        }
        Log.d(TAG, "시작과 종료에서 벗어남");
        return false;

    }

    public void sendNotification() {
        String khaiGrade = "";
        switch (mAir_Station.getKhaiGrade()) {
            case "1":
                khaiGrade = "좋음";
                break;
            case "2":
                khaiGrade = "보통";
                break;

            case "3":
                khaiGrade = "나쁨";
                break;
            case "4":
                khaiGrade = "매우나쁨";
                break;
        }

        //Pending Intent
        Intent sintent = new Intent(getApplicationContext(), MainActivity.class);
        sintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, sintent, 0);

        //NotiManager
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.app_icon).setContentTitle("통합대기지수 알림").setContentText("현재 " + mAir_Station.getStationName() + "의 상태는 " + khaiGrade + " 입니다.").setContentIntent(pIntent)
                .setAutoCancel(true).setDefaults(4).setTicker("통합대기지수 알림");

        //Sound, Vibrate Set
        if (mPref .getValue(mPref.KEY_ALARM_SOUNDSET, false ))
            notiBuilder .setDefaults(Notification. DEFAULT_SOUND);
        if (mPref .getValue(mPref.KEY_ALARM_VIBRATESET, false ))
            notiBuilder .setDefaults(Notification. DEFAULT_VIBRATE);
        //지수 등급 구분
        if (mPref.getValue(mPref.KEY_SCENARIO_STATE, 4) <= Integer.parseInt(mAir_Station.getKhaiGrade()))
            manager.notify(0, notiBuilder.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void updateAppWidget() {
        Intent intent = new Intent();
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        getApplicationContext().sendBroadcast(intent);
    }

    /**
     * @author : oh
     * @MethodName : requestCallRest_Station
     * @Day : 2014. 11. 6.
     * @Time : 오후 4:56:11
     * @Explanation : api 요청
     */
    public void requestCallRest_Station() {
        if (INFO)
            Log.i(TAG, "requestUserInfoSend()");
        if (INFO)
            Log.i(TAG, "측정소별 대기오염 측정 요청 시작");
        requestCallLatch_Station = new CountDownLatch(1);
        String url = "http://ohy8504.cafe24.com/pm10/SelectStation.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse :: " + response.toString());
                        mAir_Station = new Gson()
                                .fromJson(response, StationModel.class);

                        // 위젯에 데이터 전달하기위해 pref에도 함께 저장
                        mPref.put(mPref.KEY_KHAIVALUE, mAir_Station
                                .getKhaiValue());
                        mPref.put(mPref.KEY_KHAIGRADE, mAir_Station
                                .getKhaiGrade());
                        mPref.put(mPref.KEY_DATE, mAir_Station
                                .getDataTime());

                        requestCallLatch_Station.countDown();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d(TAG,
                        "onErrorResponse :: " + e.getLocalizedMessage());
                requestCallLatch_Station.countDown();
                e.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("STATION", mPref.getValue(mPref.KEY_STATION, ""));
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void asyncTaskCall() {
        new asyncTask().execute();
    }

    private class asyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            requestCallRest_Station();
            try {
                requestCallLatch_Station.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mPref.getValue(mPref.KEY_WIDGET_STATE, "disabled").equals("enabled"))
                updateAppWidget();
            if (mPref.getValue(mPref.KEY_ALARM_STATE, "disabled").equals("enabled")) {
                if (peroid_Time())
                    sendNotification();
            }
        }
    }


}
