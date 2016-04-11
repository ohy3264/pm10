package fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.app.pm10.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import utill.HYFont;
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
public class SettingFragment extends Fragment implements View.OnClickListener {
    // Log
    private static final String TAG = "ControlFragment";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    //View
    private Button mBtnStartTime, mBtnEndTime;

    //RadioGroup
    private RadioGroup mRadioGroupOnOff, mRadioGroupScenario;

    //CheckBox
    private CheckBox mChkVibrate, mChkSound;

    // Calendar
    private int mStartHour;
    private int mStartMin;
    private int mEndHour;
    private int mEndMin;

    // utill
    private HYFont mFont;
    private HYPreference mPref;
    SimpleDateFormat mDateFormat = new SimpleDateFormat("hh시 mm분");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // View Set
        View fragView = inflater.inflate(R.layout.fragment_setting, container,
                false);

        mFont = new HYFont(getActivity());
        mFont.setGlobalFont((ViewGroup) fragView);
        mPref = new HYPreference(getActivity());

        //RadioGroup
        mRadioGroupOnOff = (RadioGroup) fragView.findViewById(R.id.radioGroupOnOff);
        mRadioGroupOnOff.setOnCheckedChangeListener(onoffCheckedChanged);


        mRadioGroupScenario = (RadioGroup) fragView.findViewById(R.id.radioGroupScenario);
        mRadioGroupScenario.setOnCheckedChangeListener(scenarioCheckedChanged);

        //CheckBox
        mChkVibrate = (CheckBox) fragView.findViewById(R.id.chk_Vibrate);
        mChkSound = (CheckBox) fragView.findViewById(R.id.chk_Sound);
        mChkVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPref.put(mPref.KEY_ALARM_VIBRATESET, isChecked);
            }
        });
        mChkSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPref.put(mPref.KEY_ALARM_SOUNDSET, isChecked);

            }
        });

        mBtnStartTime = (Button) fragView.findViewById(R.id.btnStartTime);
        mBtnEndTime = (Button) fragView.findViewById(R.id.btnEndTime);

        mBtnStartTime.setOnClickListener(this);
        mBtnEndTime.setOnClickListener(this);

        initTimeSet();
        initAlarmSet();
        initScenarioSet();

        mChkSound.setChecked(mPref.getValue(mPref.KEY_ALARM_SOUNDSET, false));
        mChkVibrate.setChecked(mPref.getValue(mPref.KEY_ALARM_VIBRATESET, false));

        return fragView;
    }

    private RadioGroup.OnCheckedChangeListener onoffCheckedChanged = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.chk_on:
                    if (INFO)
                        Log.i(TAG, "CheckedChanged Alarm On");
                    mPref.put(mPref.KEY_ALARM_STATE, "enabled");
                    break;
                case R.id.chk_off:
                    if (INFO)
                        Log.i(TAG, "CheckedChanged Alarm Off");
                    mPref.put(mPref.KEY_ALARM_STATE, "disabled");
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener scenarioCheckedChanged = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.chk_scenario1:
                    if (INFO)
                        Log.i(TAG, "CheckedChanged 좋음");
                    mPref.put(mPref.KEY_SCENARIO_STATE, 1);
                    break;
                case R.id.chk_scenario2:
                    if (INFO)
                        Log.i(TAG, "CheckedChanged 보통");
                    mPref.put(mPref.KEY_SCENARIO_STATE, 2);
                    break;
                case R.id.chk_scenario3:
                    if (INFO)
                        Log.i(TAG, "CheckedChanged 나쁨");
                    mPref.put(mPref.KEY_SCENARIO_STATE, 3);
                    break;
                case R.id.chk_scenario4:
                    if (INFO)
                        Log.i(TAG, "CheckedChanged 매우나쁨");
                    mPref.put(mPref.KEY_SCENARIO_STATE, 4);
                    break;
            }
        }
    };
    public void initAlarmSet() {
        if (mPref.getValue(mPref.KEY_ALARM_STATE, "disabled").equals("enabled")) {
            mRadioGroupOnOff.check(R.id.chk_on);
        } else {
            mRadioGroupOnOff.check(R.id.chk_off);

        }
    }
    public void initScenarioSet() {
        switch (mPref.getValue(mPref.KEY_SCENARIO_STATE, 4)) {
            case 1:
                if (INFO)
                    Log.i(TAG, "initScenarioSet 좋음");
                mRadioGroupScenario.check(R.id.chk_scenario1);
                break;
            case 2:
                if (INFO)
                    Log.i(TAG, "initScenarioSet 보통");
                mRadioGroupScenario.check(R.id.chk_scenario2);

                break;
            case 3:
                if (INFO)
                    Log.i(TAG, "initScenarioSet 나쁨");
                mRadioGroupScenario.check(R.id.chk_scenario3);

                break;
            case 4:
                if (INFO)
                    Log.i(TAG, "initScenarioSet 매우나쁨");
                mRadioGroupScenario.check(R.id.chk_scenario4);

                break;
        }
    }
    public void initTimeSet() {
        mStartHour = mPref.getValue(mPref.KEY_TIME_START_HOUR, 0);
        mStartMin = mPref.getValue(mPref.KEY_TIME_START_MIN, 0);
        mEndHour = mPref.getValue(mPref.KEY_TIME_END_HOUR, 0);
        mEndMin = mPref.getValue(mPref.KEY_TIME_END_MIN, 0);


        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.HOUR, mStartHour);
        startCal.set(Calendar.MINUTE, mStartMin);

        mBtnStartTime.setText(getHourMessage(mStartHour) + " " + mDateFormat.format(startCal.getTime()));

        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.HOUR, mEndHour);
        endCal.set(Calendar.MINUTE, mEndMin);
        mBtnEndTime.setText(getHourMessage(mEndHour) + " " + mDateFormat.format(endCal.getTime()));
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTime:
                TimePickerDialog startDialog = new TimePickerDialog(getActivity(), startListener, mStartHour, mStartMin, false);
                startDialog.show();
                break;

            case R.id.btnEndTime:
                TimePickerDialog endDialog = new TimePickerDialog(getActivity(), endListener, mEndHour, mEndMin, false);
                endDialog.show();
                break;

        }
    }

    private TimePickerDialog.OnTimeSetListener startListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mPref.put(mPref.KEY_TIME_START_HOUR, hourOfDay);
            mPref.put(mPref.KEY_TIME_START_MIN, minute);
            mStartHour = hourOfDay;
            mStartMin = minute;

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            mBtnStartTime.setText(getHourMessage(hourOfDay) + " " + mDateFormat.format(cal.getTime()));

           /* if (minute < 10) {
                mBtnStartTime.setText(hourOfDay + ":0" + minute);
            } else {
                mBtnStartTime.setText(hourOfDay + ":" + minute);
            }*/

        }
    };
    private TimePickerDialog.OnTimeSetListener endListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mPref.put(mPref.KEY_TIME_END_HOUR, hourOfDay);
            mPref.put(mPref.KEY_TIME_END_MIN, minute);
            mEndHour = hourOfDay;
            mEndMin = minute;


            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            mBtnEndTime.setText(getHourMessage(hourOfDay) + " " + mDateFormat.format(cal.getTime()));

           /* if (minute < 10) {
                mBtnEndTime.setText(hourOfDay + ":0" + minute);
            } else {
                mBtnEndTime.setText(hourOfDay + ":" + minute);
            }*/
        }
    };

    public String getHourMessage(int hour) {
        if (hour < 12) {
            return "오전";
        }

        return "오후";
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
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
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


}
