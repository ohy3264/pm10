package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pm10.R;
import com.app.pm10.R.id;

import model.StationModel;
import utill.HYFont;


public class DetailInfoDialog extends DialogFragment {

    // Log
    private static final String TAG = "DetailInfoDialog";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;
    // View
    private TextView dialog_txtStationName, dialog_txtkhaiValue,
            dialog_txtpm10Value, dialog_txtpm2_5Value, dialog_txto3Value, dialog_txtno2Value,
            dialog_txtcoValue, dialog_txtso2Value, dialog_txtDateTime;
    private TextView dialog_BtnOk;
    // Data
    private StationModel mAirpollution_Sido = new StationModel();
    // Utill
    private HYFont mFont;

    public DetailInfoDialog() {
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        View v = mLayoutInflater.inflate(R.layout.dialog_sido, null);
        // Utill
        mFont = new HYFont(getActivity());
        mFont.setGlobalFont((ViewGroup) v);

        // Bundle Data Set
        Bundle mArgs = getArguments();
        mAirpollution_Sido = (StationModel)mArgs.getSerializable("SIDO_VALUE");

        // Button
        dialog_BtnOk = (TextView) v.findViewById(id.btnOk);
        dialog_BtnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();

            }
        });
        // Dialog View
        dialog_txtStationName = (TextView) v
                .findViewById(id.dig_txtStationName);
        dialog_txtkhaiValue = (TextView) v.findViewById(id.dig_txtkhaiValue);
        dialog_txtpm10Value = (TextView) v.findViewById(id.dig_txtpm10Value);
        dialog_txtpm2_5Value = (TextView) v.findViewById(id.dig_txtpm2_5Value);
        dialog_txto3Value = (TextView) v.findViewById(id.dig_txto3Value);
        dialog_txtno2Value = (TextView) v.findViewById(id.dig_txtno2Value);
        dialog_txtcoValue = (TextView) v.findViewById(id.dig_txtcoValue);
        dialog_txtso2Value = (TextView) v.findViewById(id.dig_txtso2Value);
        dialog_txtDateTime  = (TextView) v.findViewById(id.dig_txtDataTime);

        try {
            // Dialog Data Set
            dialog_txtStationName.setText(mAirpollution_Sido.getStationName());
            dialog_txtkhaiValue.setText(mAirpollution_Sido.getKhaiValue());
            dialog_txtpm10Value.setText(mAirpollution_Sido
                    .getPm10Value());
            dialog_txtpm2_5Value.setText(mAirpollution_Sido
                    .getPm2_5Value());
            dialog_txto3Value.setText(mAirpollution_Sido
                    .getO3Value());
            dialog_txtno2Value.setText(mAirpollution_Sido
                    .getNo2Value());
            dialog_txtcoValue.setText(mAirpollution_Sido
                    .getCoValue());
            dialog_txtso2Value.setText(mAirpollution_Sido
                    .getSo2Value());
            dialog_txtDateTime.setText(mAirpollution_Sido.getDataTime());

            // Dialog Color Set
            if (! (null == (mAirpollution_Sido.getKhaiGrade()))) {
                khaiColorSet(mAirpollution_Sido
                        .getKhaiGrade());
            }
            if (! (null == (mAirpollution_Sido.getPm10Grade()))) {
                pm10ColorSet(mAirpollution_Sido
                        .getPm10Grade());
            }
            if (! (null == (mAirpollution_Sido.getPm2_5Grade()))) {
                pm2_5ColorSet(mAirpollution_Sido
                        .getPm2_5Grade());
            }
            if (! (null == (mAirpollution_Sido.getO3Grade()))) {
                o3ColorSet(mAirpollution_Sido.getO3Grade());
            }
            if (! (null == (mAirpollution_Sido.getNo2Grade()))) {
                no2ColorSet(mAirpollution_Sido.getNo2Grade());
            }
            if (! (null == (mAirpollution_Sido.getCoGrade()))) {
                coColorSet(mAirpollution_Sido.getCoGrade());
            }
            if (! (null == (mAirpollution_Sido.getSo2Grade()))) {
                so2ColorSet(mAirpollution_Sido.getSo2Grade());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.i(TAG, "Exception");
        }


        mBuilder.setView(v);
        return mBuilder.create();
    }

    /**
     * @param khaiGrade : CAI 수치
     * @author : oh
     * @MethodName : khaiColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:21:16
     * @Explanation : CAI 수치에 따른 색 변경
     */
    public void khaiColorSet(String khaiGrade) {
        switch (khaiGrade) {
            case "1":
                dialog_txtkhaiValue.setTextColor(getResources().getColor(
                        R.color.good));
                break;
            case "2":
                dialog_txtkhaiValue.setTextColor(getResources().getColor(
                        R.color.usually));
                break;
          /*  case "3":
                dialog_txtkhaiValue.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                dialog_txtkhaiValue.setTextColor(getResources().getColor(
                        R.color.bad));
                break;
            case "4":
                dialog_txtkhaiValue.setTextColor(getResources().getColor(
                        R.color.very_bad));
                break;
        }
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
                dialog_txtpm10Value.setTextColor(getResources().getColor(
                        R.color.good));
                break;
            case "2":
                dialog_txtpm10Value.setTextColor(getResources().getColor(
                        R.color.usually));
                break;
         /*   case "3":
                dialog_txtpm10Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                dialog_txtpm10Value.setTextColor(getResources().getColor(
                        R.color.bad));
                break;
            case "4":
                dialog_txtpm10Value.setTextColor(getResources().getColor(
                        R.color.very_bad));
                break;
        }
    }
    /**
     * @param pm2_5Grade : PM2.5 수치
     * @author : oh
     * @MethodName : pm10ColorSet
     * @Day : 2014. 11. 7.
     * @Time : 오전 1:21:16
     * @Explanation : PM2.5 수치에 따른 색 변경
     */
    public void pm2_5ColorSet(String pm2_5Grade) {
        switch (pm2_5Grade) {
            case "1":
                dialog_txtpm2_5Value.setTextColor(getResources().getColor(
                        R.color.good));
                break;
            case "2":
                dialog_txtpm2_5Value.setTextColor(getResources().getColor(
                        R.color.usually));
                break;
         /*   case "3":
                dialog_txtpm2_5Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                dialog_txtpm2_5Value.setTextColor(getResources().getColor(
                        R.color.bad));
                break;
            case "4":
                dialog_txtpm2_5Value.setTextColor(getResources().getColor(
                        R.color.very_bad));
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
                dialog_txto3Value.setTextColor(getResources()
                        .getColor(R.color.good));
                break;
            case "2":
                dialog_txto3Value.setTextColor(getResources().getColor(
                        R.color.usually));
                break;
          /*  case "3":
                dialog_txto3Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                dialog_txto3Value
                        .setTextColor(getResources().getColor(R.color.bad));
                break;
            case "4":
                dialog_txto3Value.setTextColor(getResources().getColor(
                        R.color.very_bad));
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
                dialog_txtno2Value.setTextColor(getResources().getColor(
                        R.color.good));
                break;
            case "2":
                dialog_txtno2Value.setTextColor(getResources().getColor(
                        R.color.usually));
                break;
         /*   case "3":
                dialog_txtno2Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                dialog_txtno2Value.setTextColor(getResources()
                        .getColor(R.color.bad));
                break;
            case "4":
                dialog_txtno2Value.setTextColor(getResources().getColor(
                        R.color.very_bad));
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
                dialog_txtcoValue.setTextColor(getResources()
                        .getColor(R.color.good));
                break;
            case "2":
                dialog_txtcoValue.setTextColor(getResources().getColor(
                        R.color.usually));
                break;
          /*  case "3":
                dialog_txtcoValue.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                dialog_txtcoValue
                        .setTextColor(getResources().getColor(R.color.bad));
                break;
            case "4":
                dialog_txtcoValue.setTextColor(getResources().getColor(
                        R.color.very_bad));
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
                dialog_txtso2Value.setTextColor(getResources().getColor(
                        R.color.good));
                break;
            case "2":
                dialog_txtso2Value.setTextColor(getResources().getColor(
                        R.color.usually));
                break;
           /* case "3":
                dialog_txtso2Value.setTextColor(getResources().getColor(
                        R.color.slightly_bad));
                break;*/
            case "3":
                dialog_txtso2Value.setTextColor(getResources()
                        .getColor(R.color.bad));
                break;
            case "4":
                dialog_txtso2Value.setTextColor(getResources().getColor(
                        R.color.very_bad));
                break;
        }
    }


}
