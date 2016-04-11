package fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.pm10.R;
import com.app.pm10.StationSelectActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import model.StationModel;
import utill.Constant;
import utill.HYFont;
import utill.HYNetworkInfo;
import utill.HYPreference;

/**
 * <pre>
 *
 * @author : oh
 * @Day : 2014. 11. 20.
 * @Time : 오전 11:05:56
 * @Explanation : 측정소별 대기오염 정보 Fragment
 * </pre>
 */
public class StationFragment extends Fragment {
    // Log
    private static final String TAG = "StationFragment";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;
    // View
    private TextView txtStationName, txtkhaiValue, txtkhaiGrade, txtkhaiIndex,
            txtkhaiContent, txtpm10Value, txtpm10Grade, txto3Value,
            txtno2Value, txtcoValue, txtso2Value, txtDateTime, txtGuide, txtpm2_5Value;

    // DataSet
    private StationModel mAir_Station = new StationModel();

    // utill
    private HYNetworkInfo mNetworkInfo;
    private CountDownLatch requestCallLatch_Station;
    private HYFont mFont;
    private SwipeRefreshLayout mTopPullRefresh = null; // 상단 새로고침
    private HYPreference mPref;
    private String currentStation = null;
    private RequestQueue mRequestQueue;
    private ProgressBar mLodingProgress;

    // CallBack
    private OnHeadlineSelectedListener mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // View Set
        View fragView = inflater.inflate(R.layout.frgment_station, container,
                false);

        mLodingProgress = (ProgressBar) fragView.findViewById(R.id.loding_progressBar);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        txtStationName = (TextView) fragView.findViewById(R.id.txtStationName);
        txtkhaiValue = (TextView) fragView.findViewById(R.id.txtkhaiValue);
        txtkhaiGrade = (TextView) fragView.findViewById(R.id.txtkhaiGrade);
        txtkhaiIndex = (TextView) fragView.findViewById(R.id.txtkhaiIndex);
        txtkhaiContent = (TextView) fragView.findViewById(R.id.txtkhaiContent);
        txtpm2_5Value = (TextView) fragView.findViewById(R.id.txtpm2_5Value);
        txtpm10Value = (TextView) fragView.findViewById(R.id.txtpm10Value);
        txtpm10Grade = (TextView) fragView.findViewById(R.id.txtpm10Grade);
        txto3Value = (TextView) fragView.findViewById(R.id.txto3Value);
        txtno2Value = (TextView) fragView.findViewById(R.id.txtno2Value);
        txtcoValue = (TextView) fragView.findViewById(R.id.txtcoValue);
        txtso2Value = (TextView) fragView.findViewById(R.id.txtso2Value);
        txtDateTime = (TextView) fragView.findViewById(R.id.txtDateTime);
        txtGuide = (TextView) fragView.findViewById(R.id.txtGuide);
        txtGuide.setText(Constant.GUIDE_MSG);
        txtGuide.setSelected(true);

        // Utill Set
        mNetworkInfo = new HYNetworkInfo(getActivity());
        mPref = new HYPreference(getActivity());
        mFont = new HYFont(getActivity());

            mFont.setGlobalFont((ViewGroup) fragView);


        // Top pull refresh
        mTopPullRefresh = (SwipeRefreshLayout) fragView
                .findViewById(R.id.swipe_container);

        mTopPullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTopPullRefresh.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (INFO)
                            Log.i(TAG, "TopPullRefresh");
                        if (currentStation.equals("")) {

							/*mRedToast.setText("도시를 선택하세요");
                            mRedToast.show();*/

                        } else {
                            if (! mNetworkInfo.networkgetInfo()) {
                                asyncTaskCall();
                                mTopPullRefresh.setRefreshing(false);
                            } else {
                                mCallback.onNetworkInfo("네트워크를 확인해 주세요");
                                mTopPullRefresh.setRefreshing(false);
                                //Toast.makeText(getActivity(), "네트워크를 확인해 주세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, 1000);
            }
        });

        // 저장된 측정소 가져옴
        currentStation = mPref.getValue(mPref.KEY_STATION, "");
        if (currentStation.equals("")) {
            if (INFO)
                Log.i(TAG, "저장된 측정소 정보가 없습니다, 측정소 선택화면으로 이동합니다.");
            Intent stationSelintent = new Intent(getActivity(),
                    StationSelectActivity.class);
            stationSelintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(stationSelintent);
        }

        return fragView;
    }

    @TargetApi (Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (INFO)
                Log.i(TAG, "Visible");

        } else {
            if (INFO)
                Log.i(TAG, "InVisible");

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
            mCallback.onArticleSelected(TAG);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    // container (프래그먼트를 포함하는) Activity 가 이 인터페이스를 구현해야 한다.
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(String tag);

        public void onNetworkInfo(String msg);

        public void onActionBarColor(int color);
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        currentStation = mPref.getValue(mPref.KEY_STATION, "");
        if (currentStation.equals("")) {

		/*	mRedToast.setText("도시를 선택하세요");
            mRedToast.show();
*/
        } else {
            // myLocation();
            if (! mNetworkInfo.networkgetInfo()) {
                asyncTaskCall();
            } else {
                mCallback.onNetworkInfo("네트워크를 확인해 주세요");
                //Toast.makeText(getActivity(), "네트워크를 확인해 주세요", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    /**
     * @author : oh
     * @MethodName : dataSet_Station
     * @Day : 2014. 11. 12.
     * @Time : 오후 5:24:37
     * @Explanation : 측정결과 데이터셋
     */
    public void dataSet_Station() {
        try {
            if (INFO)
                Log.i(TAG, "측정결과 데이터 셋");
            txtStationName.setText(mAir_Station
                    .getStationName());
            txtkhaiValue.setText(mAir_Station
                    .getKhaiValue());
            txtkhaiGrade.setText(mAir_Station
                    .getKhaiGrade());
            txtkhaiIndex.setText(khaiIndexSet(mAir_Station.getKhaiGrade()));
            txtkhaiContent.setText(khaiContentSet(mAir_Station.getKhaiGrade()));
            txtpm10Value.setText(mAir_Station.getPm10Value());
            pm10ColorSet(mAir_Station.getPm10Grade());

            if(mAir_Station.getPm2_5Value().equals("0")){
                txtpm2_5Value.setText("-");
            }else{
                txtpm2_5Value.setText(mAir_Station.getPm2_5Value());
                pm2_5ColorSet(mAir_Station.getPm2_5Grade());
            }

            txto3Value
                    .setText(mAir_Station.getO3Value());
            o3ColorSet(mAir_Station.getO3Grade());
            txtno2Value.setText(mAir_Station.getNo2Value());
            no2ColorSet(mAir_Station.getNo2Grade());
            txtcoValue
                    .setText(mAir_Station.getCoValue());
            coColorSet(mAir_Station.getCoGrade());
            txtso2Value.setText(mAir_Station
                    .getSo2Value());
            so2ColorSet(mAir_Station.getSo2Grade());
            txtDateTime.setText(mAir_Station
                    .getDataTime() + " 발표");
            // 위젯에 데이터 전달하기위해 pref에도 함께 저장
            mPref.put(mPref.KEY_KHAIVALUE, mAir_Station
                    .getKhaiValue());
            mPref.put(mPref.KEY_KHAIGRADE, mAir_Station
                    .getKhaiGrade());
            mPref.put(mPref.KEY_DATE, mAir_Station
                    .getDataTime());

            return;
        } catch (Exception e) {
            // TODO: handle exception
        }


    }

    /**
     * @param khaiGrade : CAI 수치
     * @return
     * @author : oh
     * @MethodName : khaiIndexSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 12:19:54
     * @Explanation : 수치에 따른 지수 값 (ex: 좋음, 보통, 나쁨 등등)
     */
    public String khaiIndexSet(String khaiGrade) {
        String khaiIndex = null;
        switch (khaiGrade) {
            case "1":
                khaiIndex = getResources().getString(R.string.air_index_good);
                txtkhaiValue.setTextColor(getResources().getColor(R.color.good));
                mCallback.onActionBarColor(getResources().getColor(R.color.good));
                Constant.ACTIONBAR_COLOR = getResources().getColor(R.color.good);
                break;
            case "2":
                khaiIndex = getResources().getString(R.string.air_index_usually);
                txtkhaiValue.setTextColor(getResources().getColor(R.color.usually));
                mCallback.onActionBarColor(getResources().getColor(R.color.usually));
                Constant.ACTIONBAR_COLOR = getResources().getColor(R.color.usually);
                break;
           /* case "3":
                khaiIndex = getResources().getString(
                        R.string.air_index_slightly_bad);
                txtkhaiValue.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                mCallback.onActionBarColor(getResources().getColor(R.color.slightly_bad));
                Config.ACTIONBAR_COLOR = getResources().getColor(R.color.slightly_bad);
                break;*/
            case "3":
                khaiIndex = getResources().getString(R.string.air_index_bad);
                txtkhaiValue.setTextColor(getResources().getColor(R.color.bad));
                mCallback.onActionBarColor(getResources().getColor(R.color.bad));
                Constant.ACTIONBAR_COLOR = getResources().getColor(R.color.bad);
                break;
            case "4":
                khaiIndex = getResources().getString(R.string.air_index_very_bad);
                txtkhaiValue
                        .setTextColor(getResources().getColor(R.color.very_bad));
                mCallback.onActionBarColor(getResources().getColor(R.color.very_bad));
                Constant.ACTIONBAR_COLOR = getResources().getColor(R.color.very_bad);
                break;

        }
        return khaiIndex;
    }

    /**
     * @param khaiGrade : CAI 수치
     * @return
     * @author : oh
     * @MethodName : khaiContentSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 12:20:37
     * @Explanation : CAI 수치에 따른 의미 변경
     */
    public String khaiContentSet(String khaiGrade) {
        String khaiContent = null;
        switch (khaiGrade) {
            case "1":
                khaiContent = getResources().getString(R.string.air_content_good);
                break;
            case "2":
                khaiContent = getResources()
                        .getString(R.string.air_content_usually);
                break;
            case "3":
                khaiContent = getResources().getString(
                        R.string.air_content_slightly_bad);
                break;
            case "4":
                khaiContent = getResources().getString(R.string.air_content_bad);
                break;
            case "5":
                khaiContent = getResources().getString(
                        R.string.air_content_very_bad);
                break;
        }
        return khaiContent;
    }

    /**
     * @param pm10Grade : PM10 수치
     * @author : oh
     * @MethodName : pm10ColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:21:16
     * @Explanation : PM10 수치에 따른 색 변경
     */
    public void pm10ColorSet(String pm10Grade) {
        switch (pm10Grade) {
            case "1":
                txtpm10Value.setTextColor(getResources().getColor(R.color.good));
                txtpm10Grade.setText("좋음");
                break;
            case "2":
                txtpm10Value.setTextColor(getResources().getColor(R.color.usually));
                txtpm10Grade.setText("보통");
                break;
           /* case "3":
                txtpm10Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                txtpm10Grade.setText("약간나쁨");
                break;*/
            case "3":
                txtpm10Value.setTextColor(getResources().getColor(R.color.bad));
                txtpm10Grade.setText("나쁨");
                break;
            case "4":
                txtpm10Value
                        .setTextColor(getResources().getColor(R.color.very_bad));
                txtpm10Grade.setText("매우나쁨");
                break;
        }
    }
    /**
     * @param pm2_5Grade : PM10 수치
     * @author : oh
     * @MethodName : pm10ColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:21:16
     * @Explanation : PM2.5 수치에 따른 색 변경
     */
    public void pm2_5ColorSet(String pm2_5Grade) {
        switch (pm2_5Grade) {
            case "1":
                txtpm2_5Value.setTextColor(getResources().getColor(R.color.good));
                break;
            case "2":
                txtpm2_5Value.setTextColor(getResources().getColor(R.color.usually));
                break;
           /* case "3":
                txtpm10Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                txtpm10Grade.setText("약간나쁨");
                break;*/
            case "3":
                txtpm2_5Value.setTextColor(getResources().getColor(R.color.bad));
                break;
            case "4":
                txtpm2_5Value
                        .setTextColor(getResources().getColor(R.color.very_bad));
                break;
        }
    }


    /**
     * @param o3Grade : o3 수치
     * @author : oh
     * @MethodName : o3ColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:23:39
     * @Explanation : o3 수치에 따른 색변경
     */
    public void o3ColorSet(String o3Grade) {
        switch (o3Grade) {
            case "1":
                txto3Value.setTextColor(getResources().getColor(R.color.good));
                break;
            case "2":
                txto3Value.setTextColor(getResources().getColor(R.color.usually));
                break;
           /* case "3":
                txto3Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                txto3Value.setTextColor(getResources().getColor(R.color.bad));
                break;
            case "4":
                txto3Value.setTextColor(getResources().getColor(R.color.very_bad));
                break;
        }
    }

    /**
     * @param no2Grade : no2 수치
     * @author : oh
     * @MethodName : no2ColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:23:39
     * @Explanation : no2 수치에 따른 색변경
     */
    public void no2ColorSet(String no2Grade) {
        switch (no2Grade) {
            case "1":
                txtno2Value.setTextColor(getResources().getColor(R.color.good));
                break;
            case "2":
                txtno2Value.setTextColor(getResources().getColor(R.color.usually));
                break;
           /* case "3":
                txtno2Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                txtno2Value.setTextColor(getResources().getColor(R.color.bad));
                break;
            case "4":
                txtno2Value.setTextColor(getResources().getColor(R.color.very_bad));
                break;
        }
    }

    /**
     * @param coGrade : co 수치
     * @author : oh
     * @MethodName : coColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:23:39
     * @Explanation : co 수치에 따른 색변경
     */
    public void coColorSet(String coGrade) {
        switch (coGrade) {
            case "1":
                txtcoValue.setTextColor(getResources().getColor(R.color.good));
                break;
            case "2":
                txtcoValue.setTextColor(getResources().getColor(R.color.usually));
                break;
          /*  case "3":
                txtcoValue.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                txtcoValue.setTextColor(getResources().getColor(R.color.bad));
                break;
            case "4":
                txtcoValue.setTextColor(getResources().getColor(R.color.very_bad));
                break;
        }
    }

    /**
     * @param so2Grade : so2 수치
     * @author : oh
     * @MethodName : o3ColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:23:39
     * @Explanation : so2 수치에 따른 색변경
     */
    public void so2ColorSet(String so2Grade) {
        switch (so2Grade) {
            case "1":
                txtso2Value.setTextColor(getResources().getColor(R.color.good));
                break;
            case "2":
                txtso2Value.setTextColor(getResources().getColor(R.color.usually));
                break;
           /* case "3":
                txtso2Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                txtso2Value.setTextColor(getResources().getColor(R.color.bad));
                break;
            case "4":
                txtso2Value.setTextColor(getResources().getColor(R.color.very_bad));
                break;
        }
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
                        if (response.toString().trim().equals("Sucess")) {


                        } else {

                        }
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
            mLodingProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                requestCallRest_Station();
                requestCallLatch_Station.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dataSet_Station();
            updateAppWidget();
            mLodingProgress.setVisibility(View.GONE);
        }
    }

    public void updateAppWidget() {
        Intent intent = new Intent();
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        getActivity().sendBroadcast(intent);
    }





}
