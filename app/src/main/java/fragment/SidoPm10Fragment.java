package fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.pm10.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import adapter.SidoPm10Adapter;
import adapter.SpinnerAdapter;
import dialog.DetailInfoDialog;
import model.StationModel;
import sqlite.DBManager;
import utill.Constant;
import utill.HYFont;
import utill.HYPreference;

/**
 * <pre>
 *
 * @author : oh
 * @Day : 2014. 11. 20.
 * @Time : 오전 11:09:03
 * @Explanation : 시도시별 측정 정보 Fragment
 * </pre>
 */
public class SidoPm10Fragment extends Fragment implements OnItemSelectedListener,
        OnItemClickListener, OnClickListener {
    // Log
    private static final String TAG = "SidoPm10Fragment";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    // View
    private Spinner mSpinner_Sido;
    private LinearLayout mLinActBackground;

    // Dialog
    private DetailInfoDialog mDetailInfoDig;

    // DataSet
    private String mSido;
    private String[] mSidoArray;

    // ListView
    private ListView mSidoListView;
    private SidoPm10Adapter mSidoAdapter;
    private ArrayList<StationModel> mAirpollutionList_Sido = new ArrayList<StationModel>();


    // utill
    private HYFont mFont;
    private HYPreference mPref;
    private CountDownLatch requestCallLatch_Sido;
    private DBManager mDB;
    private RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // View Set
        View fragView = inflater.inflate(R.layout.frgment_sido_pm10, container,
                false);



        mLinActBackground = (LinearLayout)fragView.findViewById(R.id.linActBackground);
        mLinActBackground.setBackgroundColor(Constant.ACTIONBAR_COLOR);

        mSidoArray = getResources().getStringArray(R.array.sido);
        mSpinner_Sido = (Spinner) fragView.findViewById(R.id.spinner_sido);
        mSpinner_Sido.setAdapter(new SpinnerAdapter(getActivity(),
                R.layout.spinner_row_default, mSidoArray));
        mSpinner_Sido.setOnItemSelectedListener(this);

        mSidoListView = (ListView) fragView.findViewById(R.id.sido_listView);
        mSidoListView.setOnItemClickListener(this);

        // Utill Set
        mDB = new DBManager(getActivity());
        mPref = new HYPreference(getActivity());
        mFont = new HYFont(getActivity());
        mRequestQueue = Volley.newRequestQueue(getActivity());

        mSpinner_Sido.setSelection(mPref.getValue(mPref.KEY_SIDO_PM10, 0));

        mFont.setGlobalFont((ViewGroup) fragView);
        mFont.setGlobalFont((ViewGroup) mSpinner_Sido);

        setAdapter();
        return fragView;
    }


    public void setAdapter() {
        mSidoAdapter = new SidoPm10Adapter(getActivity(), mAirpollutionList_Sido);
        if (mSidoListView != null) {
            mSidoListView.setAdapter(mSidoAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        mDetailInfoDig = new DetailInfoDialog();
        StationModel item = mAirpollutionList_Sido.get(position);
        Bundle args = new Bundle();
        args.putSerializable("SIDO_VALUE", item);
        mDetailInfoDig.setArguments(args);
        mDetailInfoDig.show(getFragmentManager(), "MYTAG");


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        mSido = parent.getItemAtPosition(position).toString();
        mPref.put(mPref.KEY_SIDO_PM10, position);
        asyncTaskCall();


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

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

    /**
     * @author : oh
     * @MethodName : requestCallRest_Station
     * @Day : 2014. 11. 6.
     * @Time : 오후 4:56:11
     * @Explanation : api 요청
     */
    public void requestCallRest_Sido() {
        if (INFO)
            Log.i(TAG, "requestCallRest_Station()");
        if (INFO)
            Log.i(TAG, "시도시별 대기오염 측정시작");
        mAirpollutionList_Sido.clear();
        requestCallLatch_Sido = new CountDownLatch(1);
        String url = "http://ohy8504.cafe24.com/pm10/SelectSido.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse :: " + response.toString());

                        if (response.toString().trim().equals("Sucess")) {

                        } else {

                        }
                        try {
                            java.lang.reflect.Type type = new TypeToken<List<StationModel>>() {
                            }.getType();

                            ArrayList<StationModel> value = new Gson()
                                    .fromJson(response, type);

                            for (StationModel m : value) {
                                StationModel t = new StationModel();

                                t.set_id(m.get_id());
                                t.setDataTime(m.getDataTime());
                                t.setStationName(m.getStationName());
                                t.setSidoName(m.getSidoName());
                                t.setSo2Value(m.getSo2Value());
                                t.setCoValue(m.getCoValue());
                                t.setO3Value(m.getO3Value());
                                t.setNo2Value(m.getNo2Value());
                                t.setPm10Value(m.getPm10Value());
                                t.setKhaiValue(m.getKhaiValue());
                                t.setKhaiGrade(m.getKhaiGrade());
                                t.setSo2Grade(m.getSo2Grade());
                                t.setCoGrade(m.getCoGrade());
                                t.setO3Grade(m.getO3Grade());
                                t.setNo2Grade(m.getNo2Grade());
                                t.setPm10Grade(m.getPm10Grade());
                                t.setPm2_5Value(m.getPm2_5Value());
                                t.setPm2_5Grade(m.getPm2_5Grade());
                                mAirpollutionList_Sido.add(t);
                            }
                            requestCallLatch_Sido.countDown();
                        } catch (Exception e) {
                            Log.d(TAG, "allsync.Response : Gson Exception");
                            requestCallLatch_Sido.countDown();
                        }
                        requestCallLatch_Sido.countDown();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d(TAG,
                        "onErrorResponse :: " + e.getLocalizedMessage());
                requestCallLatch_Sido.countDown();
                e.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("SIDO", mSido);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    /**
     * @author : oh
     * @MethodName : asyncTaskCall
     * @Day : 2014. 11. 24.
     * @Time : 오후 9:20:11
     * @Explanation : asyncTask 호출부
     */
    public void asyncTaskCall() {
        new asyncTask().execute();
    }

    /**
     * <pre>
     *
     * @author : oh
     * @Day : 2014. 11. 24.
     * @Time : 오후 9:20:25
     * @Explanation : asyncTask
     * </pre>
     */
    private class asyncTask extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = showLoadingDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            requestCallRest_Sido();
            try {

                requestCallLatch_Sido.await();
                mProgressDialog.dismiss();
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
            setAdapter();

            mProgressDialog.dismiss();
        }
    }


    public ProgressDialog showLoadingDialog() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loding..");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

}
