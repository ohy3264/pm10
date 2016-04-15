package fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.app.pm10.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.apache.cxf.common.util.StringUtils;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import utill.Constant;
import utill.HYAnimation;
import utill.HYFont;
import utill.HYNetworkInfo;
import utill.HYPreference;
import utill.ImgDownloader;

/**
 * <pre>
 *
 * @author : oh
 * @Day : 2014. 11. 20.
 * @Time : 오전 11:05:56
 * @Explanation : -Fragment
 * </pre>
 */
public class Pm10ImgFragment extends Fragment implements OnClickListener {
    // Log
    private static final String TAG = "PmImgFragment";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;
    String mImg_down_flag = null;
    //View
    private LinearLayout mLinVisible;
    // Image
    public ImageView mImgAir;

    // Ani
    private AnimationDrawable mAnimationDrawable;
    private Boolean mAniFalg = false;
    private Boolean exit = false;
    private int startNum = 0;

    // calendar
    private Calendar mCal = Calendar.getInstance();
    private int mYear = this.mCal.get(Calendar.YEAR);
    private int mMonth = this.mCal.get(Calendar.MONTH);
    private int mDay = this.mCal.get(Calendar.DATE);


    private ImageView mImgToggle;

    // utill
    private HYNetworkInfo mNetworkInfo;
    private HYPreference mPref;
    private HYFont mFont;
    private ImgDownloader mImgLoader;
    private RequestQueue mRequestQueue;
    private CountDownLatch requestCallLatch_Img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // View Set

        View fragView = inflater.inflate(R.layout.frgment_pm10img, container,
                false);

        // Utill Set
        mFont = new HYFont(getActivity());
        mImgLoader = new ImgDownloader();
        mFont.setGlobalFont((ViewGroup) fragView);
        mNetworkInfo = new HYNetworkInfo(getActivity());

        mLinVisible = (LinearLayout) fragView.findViewById(R.id.lin_Visible);
        mLinVisible.setTag("on");
        mLinVisible.setOnClickListener(this);


        mImgToggle = (ImageView) fragView.findViewById(R.id.imgToggle);
        mImgToggle.setOnClickListener(this);

        mImgAir = (ImageView) fragView.findViewById(R.id.img_air);
        mImgAir.setOnClickListener(this);

        initLoadImg();

        // threadStart();
        setHasOptionsMenu(true);

        // asyncTaskCall("PM10");
        return fragView;
    }

    private ProgressDialog progressDialog;
    public void initLoadImg(){
        progressDialog = showLoadingDialog();
        Glide.with(this)
                .load("http://www.webairwatch.com/kaq/modelimg/PM10.09km.Animation.gif").listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressDialog.dismiss();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressDialog.dismiss();
                return false;
            }
        }).crossFade().into(mImgAir);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_img, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.action_reset:
                Log.i("reset", "reset");
                onImgResetState();
                HYAnimation.VISIBLE(mImgToggle);
                mLinVisible.setTag("on");

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lin_Visible:
                if (mLinVisible.getTag().equals("on")) {
                    HYAnimation.GONE(mImgToggle);
                    Log.i("Visible", "off");
                    mLinVisible.setTag("off");
                } else {
                    HYAnimation.VISIBLE(mImgToggle);
                    Log.i("Visible", "on");
                    mLinVisible.setTag("on");
                }
                break;


            case R.id.imgToggle:
                if (mImgToggle.getTag().equals("재생")) {
                    Log.i("Toggle", "재생");
                    onImgStartState();

                } /*else if(mImgToggle.getTag().equals("일시정지")){
                    Log.i("Toggle", "일시정지");
                    onImgPauseState();

                }*/ else if (mImgToggle.getTag().equals("초기화")) {
                    Log.i("Toggle", "초기화");
                    onImgResetState();
                }
                break;
        }
    }

    public void onImgStartState() {
        asyncTaskAniCall("PM10"); // 애니메이션 세팅하고 재생
        mAniFalg = true; // 타이머 쓰레드 재가동
        HYAnimation.GONE(mImgToggle); // 버튼 숨김
        mLinVisible.setTag("off"); // 자동숨김 상태 off
        //mImgToggle.setTag("일시정지"); //현재 토글 상태 (재생->일시정지)
        mImgToggle.setTag("초기화"); //현재 토글 상태 (재생->일시정지)
        //  mImgToggle.setImageResource(R.drawable.icon_pause); // 이미지 변경
        mImgToggle.setImageResource(R.drawable.icon_reset); // 이미지 변경
    }

    public void onImgPauseState() {
        if (startNum != 0) //0이 아닐떄 하나빼줌..
            startNum--;
        mAnimationDrawable.stop(); //애니메이션 정지
        mAniFalg = false; //타이머 쓰레드 일시정지
        mImgToggle.setTag("재생"); //현재 토글상태 (일시정지->재생)
        mImgToggle.setImageResource(R.drawable.icon_play); //이미지 변경
    }

    public void onImgResetState() {
        startNum = 0; //시작이미지 0으로 세팅
        asyncTaskCall("PM10"); // 애니메이션 재세팅 후 프리뷰만 뛰움
        mAniFalg = false; // 타이머 쓰레드 일시정지
        mImgToggle.setTag("재생"); //현재 토글상태 (초기화->재생)
        mImgToggle.setImageResource(R.drawable.icon_play); //이미지 변경

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

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (!mNetworkInfo.networkgetInfo()) {
            //  asyncTaskCall("PM10");
        } else {
            Toast.makeText(getActivity(), "네트워크를 확인해 주세요", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    public void requestCallRest_Station(final int imgNum) {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        String num = "";
        if (imgNum < 10) {
            num = "0" + imgNum;
        } else {
            num = "" + imgNum;
        }
        String addr = "http://www.webairwatch.com/kaq/modelimg/";
        addr = addr + "PM10" + ".09km." + num + ".gif";
        ImageRequest imgRequest = new ImageRequest(addr, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // holder.imgProfile.setImageBitmap(response);
                try {

                    mImgLoader = new ImgDownloader();
                    mImgLoader.SaveBitmapToFileCache(response, getActivity()
                            .getCacheDir().toString(), "PM10" + "_" + imgNum);
                    Log.i(TAG, "PM10" + "_" + imgNum);

                } catch (Exception e) {

                }
                requestCallLatch_Img.countDown();
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // holder.imgProfile.setImageResource(R.drawable.ic_launcher);
                requestCallLatch_Img.countDown();
            }
        });
        mRequestQueue.add(imgRequest);
    }


    public void asyncTaskCall(String flag) {
        new asyncTask().execute(flag);

    }

    private class asyncTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mAnimationDrawable = new AnimationDrawable();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            long startTime = System.currentTimeMillis();
            mPref = new HYPreference(getActivity());
            mImg_down_flag = mPref.getValue(mPref.KEY_IMG_PM10, null);


            // 이미지 다운로드
            if (StringUtils.isEmpty(mImg_down_flag)
                    || !mImg_down_flag.equals(mYear + "-" + (mMonth + 1) + "-"
                    + mDay)) {
                if (INFO)
                    Log.d(TAG, "오늘 첫 실행 - PM10 예측 Img를 다운로드");
                requestCallLatch_Img = new CountDownLatch(Constant.img_num + 1);
                for (int i = 0; i <= Constant.img_num; i++) {
                    requestCallRest_Station(i);
                }
                mPref.put(mPref.KEY_IMG_PM10, mYear + "-" + (mMonth + 1) + "-"
                        + mDay);

                try {
                    requestCallLatch_Img.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (INFO)
                    Log.d(TAG, "재 실행 - PM10 예측 Img를 다운로드 생략");
            }
            animationSet(params[0]);

            long endTime = System.currentTimeMillis();
            Log.d("총 걸린 시간 : ", (endTime - startTime) / 1000.0 + " sec");

            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mImgAir.setImageDrawable(mAnimationDrawable.getFrame(0));
            //mAnimationDrawable.start();

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
        exit = true;
        mAniFalg = true;
    }


    public void asyncTaskAniCall(String flag) {
        new asyncTaskAni().execute(flag);

    }

    private class asyncTaskAni extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mAnimationDrawable = new AnimationDrawable();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            animationSet(params[0]);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mImgAir.setImageDrawable(mAnimationDrawable);
            mAnimationDrawable.start();
        }
    }

    public void animationSet(String type) {
        Bitmap bImg;
        Drawable draw;
        int duration = 1000;

        for (int i = startNum; i <= Constant.img_num; i++) {
            mImgLoader = new ImgDownloader();
            bImg = mImgLoader.LoadBitmapToFileCache(getActivity()
                    .getCacheDir().toString(), type + "_" + i);
            draw = new BitmapDrawable(getResources(), bImg);
            mAnimationDrawable.addFrame((BitmapDrawable) draw, duration);
        }
    }


    public void threadStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                timerProcess();
            }
        }).start();
    }

    public void timerProcess() {
        while (true) {
            if (mAniFalg) {
                try {
                    handler.sendEmptyMessage(startNum++);
                    Thread.sleep(1000);

                    if (startNum >= Constant.img_num) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAniFalg = false;
                                mImgToggle.setImageResource(R.drawable.icon_reset);
                                mImgToggle.setTag("초기화");
                                mLinVisible.setTag("on");
                                HYAnimation.VISIBLE(mImgToggle);

                            }
                        });
                    }
                    if (exit) {
                        break;
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("타이머", Integer.toString(msg.what));
        }
    };


    /**
     * @return
     * @author : oh
     * @MethodName : showLoadingDialog
     * @Day : 2014. 10. 12.
     * @Time : 오후 8:26:28
     * @Explanation : LodingProgress
     */
    public ProgressDialog showLoadingDialog() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loding..");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }
}
