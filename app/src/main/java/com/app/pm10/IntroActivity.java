package com.app.pm10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import utill.Constant;
import utill.HYFont;


/**
 * Created by oh on 2015-02-01.
 */
public class IntroActivity extends Activity {
    public static final String TAG = "IntroActivity";
    public static final boolean DBUG = true;
    public static final boolean INFO = true;

    //Utill
    private HYFont mFont;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Utill
        mRequestQueue = Volley.newRequestQueue(this);
        mFont = new HYFont(this);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        requestCallRest_Guide();
        mFont.setGlobalFont(root);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg = IntentHandler.obtainMessage();
                IntentHandler.sendMessage(msg);
            }
        }, 2000);


    }


    final Handler IntentHandler = new Handler() {
        public void handleMessage(Message msg) {


            Intent intent_main = new Intent(IntroActivity.this, MainActivity.class);
            intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent_main);
            overridePendingTransition(0, 0);
            finish();
        }
    };

    public void requestCallRest_Guide() {
        if (INFO)
            Log.i(TAG, "requestCallRest_Guide()");
        String url = "http://ohy8504.cafe24.com/pm10/GuideMsg.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse :: " + response.toString());
                        Constant.GUIDE_MSG = response.trim();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d(TAG,
                        "onErrorResponse :: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }


}
