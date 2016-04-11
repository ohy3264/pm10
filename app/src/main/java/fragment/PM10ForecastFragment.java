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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.pm10.R;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import model.PM10ForecastModel;
import utill.HYFont;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.
 * @Time          : 오전 11:09:18
 * @Explanation   : 예보개황 정보 Fragment
 * </pre>
 *
 */
public class PM10ForecastFragment extends Fragment implements OnClickListener {
	// Log
	private static final String TAG = "PM10ForecastFragment";
	private static final boolean DEBUG = true;
	private static final boolean INFO = true;
    //View
    private TextView mTxtDataTime;
    private TextView mTxtInformOverall;
    private TextView mTxtInformCause;
    private TextView mTxtInformGrade;


	// ListView
	private PM10ForecastModel mPM10ForecastRow = new PM10ForecastModel();
	private CountDownLatch requestCallLatch_PM10;

	// utill
	private HYFont mFont;
	private int mCount = 0;

	// calendar
	private Calendar mCal = Calendar.getInstance();
    private RequestQueue mRequestQueue;
    private String mInformCode;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// View Set
		View fragView = inflater.inflate(R.layout.frgment_forecast,
				container, false);
		// Utill Set
		mFont = new HYFont(getActivity());
		mFont.setGlobalFont((ViewGroup) fragView);
        mRequestQueue = Volley.newRequestQueue(getActivity());

        //View Set
        mTxtDataTime = (TextView)fragView.findViewById(R.id.txtDataTime);
        mTxtInformOverall = (TextView)fragView.findViewById(R.id.txtInformOverall);
        mTxtInformCause = (TextView)fragView.findViewById(R.id.txtInformCause);
        mTxtInformGrade = (TextView)fragView.findViewById(R.id.txtInformGrade);

        //BundleSet
        mInformCode = getArguments().getString("InformCode");

        asyncTaskCall();
		return fragView;
	}
    public void dataSet(){
        mTxtDataTime.setText(mPM10ForecastRow.getDataTime().toString());
        mTxtInformOverall.setText(mPM10ForecastRow.getInformOverall().toString());
        mTxtInformCause.setText(mPM10ForecastRow.getInformCause().toString());
        mTxtInformGrade.setText(mPM10ForecastRow.getInformGrade().toString());
    }




	/**
	 * @author : oh
	 * @MethodName : requestCallRest_PM10
	 * @Day : 2014. 11. 24.
	 * @Time : 오후 9:21:28
	 * @Explanation : 예보개황 측정 요청
	 *
	 */
	public void requestCallRest_PM10() {
		if (INFO)
			Log.i(TAG, "예보개황 요청 시작");
		requestCallLatch_PM10 = new CountDownLatch(1);
        if (INFO)
            Log.i(TAG, "requestUserInfoSend()");
        if (INFO)
            Log.i(TAG, "측정소별 대기오염 측정 요청 시작");
        String url = "http://ohy8504.cafe24.com/pm10/SelectForecast.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse :: " + response.toString());
                        mPM10ForecastRow = new Gson()
                                .fromJson(response, PM10ForecastModel.class);

                        requestCallLatch_PM10.countDown();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d(TAG,
                        "onErrorResponse :: " + e.getLocalizedMessage());
                requestCallLatch_PM10.countDown();
                e.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                Log.d(TAG, mInformCode);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("InformCode", mInformCode);
                return params;
            }
        };
        mRequestQueue.add(request);
	}






	public void asyncTaskCall() {
		new asyncTask().execute();
	}

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
			requestCallRest_PM10();
			try {
				requestCallLatch_PM10.await();
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
			mProgressDialog.dismiss();
            dataSet();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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
