package utill;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;


/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 10.   
 * @Time          : 오후 1:23:59
 * @Explanation   : Preferences
 * </pre>
 *
 */
public class HYPreference extends Preference {
	private final String PREF_NAME = "com.pm10.pref";
	public static final String KEY_SERVICE_REGISTERED = "service_registered";

    //상태변화 체크(위젯, 알림, 알림단계)
    public static final String KEY_WIDGET_STATE = "widget_state";
    public static final String KEY_ALARM_STATE = "alarm_state";
    public static final String KEY_SCENARIO_STATE = "scenario_state";

    //알림 시간
    public static final String KEY_TIME_START_HOUR = "start_time_hour";
    public static final String KEY_TIME_START_MIN = "start_time_min";
    public static final String KEY_TIME_END_HOUR = "end_time_hour";
    public static final String KEY_TIME_END_MIN = "end_time_min";

    //알림 진동, 소리
    public static final String KEY_ALARM_VIBRATESET = "vibrateSet";
    public static final String KEY_ALARM_SOUNDSET = "soundSet";


    public static final String KEY_SIDO_ADDR = "sido_addr";
    public static final String KEY_SIDO_PM10 = "sido_pm10";

    //위젯 데이터
	public static final String KEY_STATION = "station";
	public static final String KEY_KHAIGRADE = "khaigrade";
	public static final String KEY_KHAIVALUE = "khaivalue";
	public static final String KEY_DATE = "date";

    //이미지 다운로드
    public static final String KEY_IMG_PM10 = "pm10img_down_flag";
    public static final String KEY_IMG_PM2_5 = "pm2_5img_down_flag";
	

	private static Context mContext;

	public HYPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void put(String key, String value) {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putString(key, value);
		editor.commit();
	}

	public void put(String key, boolean value) {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean(key, value);
		editor.commit();
	}

	public void put(String key, int value) {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putInt(key, value);
		editor.commit();
	}

	public String getValue(String key, String dftValue) {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);

		try {
			return pref.getString(key, dftValue);
		} catch (Exception e) {
			return dftValue;
		}

	}

	public int getValue(String key, int dftValue) {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);

		try {
			return pref.getInt(key, dftValue);
		} catch (Exception e) {
			return dftValue;
		}

	}

	public boolean getValue(String key, boolean dftValue) {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);

		try {
			return pref.getBoolean(key, dftValue);
		} catch (Exception e) {
			return dftValue;
		}
	}

	public void allClear() {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();

	}

	public void setClear(String key) {
		SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();

	}
}
