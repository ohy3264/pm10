package com.app.pm10;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import utill.Constant;
import utill.HYPreference;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 18.   
 * @Time          : 오후 12:44:28
 * @Explanation   : 위젯 
 * 
 * 호출 순서
 * 1. onUpdate()
 * 2. updateAppWidget()
 * 3. onReceive()
 * 	3-1. android.appwidget.action.APPWIDGET_UPDATE
 * 4. removePreviousAlarm
 * 
 * </pre>
 *
 */
public class Pm10Widget extends AppWidgetProvider {
	// Log
	private static final String TAG = "Pm10Widget";
	private static final boolean DEBUG = false;
	private static final boolean INFO = true;
	// DataSet

	private Context context;
	// Utill
	private HYPreference mPref;

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		if (INFO)
			Log.i(TAG, "onEnabled()");
        mPref = new HYPreference(context);
        mPref.put(mPref.KEY_WIDGET_STATE, "enabled");
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		if (INFO)
			Log.i(TAG, "onUpdate()");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.context = context;
		this.mPref = new HYPreference(context);
		// 현재 클래스로 등록된 모든 위젯의 리스트를 가져옴
		appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
				context, getClass()));

		final int N = appWidgetIds.length;

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidgetUI(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		if (INFO)
			Log.i(TAG, "onDeleted()");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		if (INFO)
			Log.i(TAG, "onDisabled()");
        this.mPref = new HYPreference(context);
        mPref.put(mPref.KEY_WIDGET_STATE, "disabled");
		super.onDisabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);

		if (INFO)
			Log.i(TAG, "onReceive()");

		String action = intent.getAction();
		// Default Recevier
		switch (action) {
		case AppWidgetManager.ACTION_APPWIDGET_ENABLED:
			if (INFO)
				Log.i(TAG, "수신 액션 : android.appwidget.action.APPWIDGET_ENABLED");

			break;

		case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
			if (INFO)
				Log.i(TAG, "수신 액션 : android.appwidget.action.APPWIDGET_UPDATE");

			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			this.onUpdate(context, manager, manager
					.getAppWidgetIds(new ComponentName(context, getClass())));
			break;
			
		case AppWidgetManager.ACTION_APPWIDGET_DELETED:
			if (INFO)
				Log.i(TAG, "수신 액션 : android.appwidget.action.APPWIDGET_DELETED");
			break;
			
		case Constant.ACTION_EVENT:
			if (INFO)
				Log.i(TAG, "수신 액션 : com.widget.ACTION_EVENT");
			break;
			
		case Constant.ACTION_CALL_ACTIVITY:
			if (INFO)
				Log.i(TAG, "수신 액션 : com.widget.ACTION_CALL_ACTIVITY");
			callActivity(context);
			break;
		}
		
		
	}

	/**
	 * @author : oh
	 * @MethodName : updateAppWidget
	 * @Day : 2014. 11. 18.
	 * @Time : 오후 12:39:28
	 * @Explanation : Widget UI 설정 이벤트
	 *
	 * @param context
	 * @param appWidgetManager
	 * @param appWidgetId
	 */
	public void updateAppWidgetUI(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		if (INFO)
			Log.i(TAG, "updateAppWidget()");


		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_pm10);
		Intent activityIntent = new Intent(Constant.ACTION_CALL_ACTIVITY);
		PendingIntent activityPIntent = PendingIntent.getBroadcast(context, 0,
				activityIntent, 0);
		updateViews
				.setOnClickPendingIntent(R.id.call_activity, activityPIntent);

		updateViews.setTextViewText(R.id.widget_station,
				mPref.getValue(mPref.KEY_STATION, ""));
		updateViews.setTextViewText(R.id.widget_pm10_value,
				mPref.getValue(mPref.KEY_KHAIVALUE, ""));
		updateViews.setTextViewText(R.id.widget_date,
				mPref.getValue(mPref.KEY_DATE, ""));
		switch (mPref.getValue(mPref.KEY_KHAIGRADE, "")) {
		case "1":
			updateViews.setTextViewText(R.id.widget_pm10_grade, context
					.getResources().getString(R.string.air_index_good));
			updateViews.setTextColor(R.id.widget_pm10_value, context
					.getResources().getColor(R.color.good));
			updateViews.setTextColor(R.id.widget_pm10_grade, context
					.getResources().getColor(R.color.good));
			break;
		case "2":
			updateViews.setTextViewText(R.id.widget_pm10_grade, context
					.getResources().getString(R.string.air_index_usually));
			updateViews.setTextColor(R.id.widget_pm10_value, context
					.getResources().getColor(R.color.usually));
			updateViews.setTextColor(R.id.widget_pm10_grade, context
					.getResources().getColor(R.color.usually));
			break;
		/*case "3":
			updateViews.setTextViewText(R.id.widget_pm10_grade, context
					.getResources().getString(R.string.air_index_slightly_bad));
			updateViews.setTextColor(R.id.widget_pm10_value, context
					.getResources().getColor(R.color.slightly_bad));
			updateViews.setTextColor(R.id.widget_pm10_grade, context
					.getResources().getColor(R.color.slightly_bad));
			break;*/
		case "3":
			updateViews.setTextViewText(R.id.widget_pm10_grade, context
					.getResources().getString(R.string.air_index_bad));
			updateViews.setTextColor(R.id.widget_pm10_value, context
					.getResources().getColor(R.color.bad));
			updateViews.setTextColor(R.id.widget_pm10_grade, context
					.getResources().getColor(R.color.bad));
			break;
		case "4":
			updateViews.setTextViewText(R.id.widget_pm10_grade, context
					.getResources().getString(R.string.air_index_very_bad));
			updateViews.setTextColor(R.id.widget_pm10_value, context
					.getResources().getColor(R.color.very_bad));
			updateViews.setTextColor(R.id.widget_pm10_grade, context
					.getResources().getColor(R.color.very_bad));
			break;
		}
		appWidgetManager.updateAppWidget(appWidgetId, updateViews);
	}

	/**
	 * @author : oh
	 * @MethodName : callActivity
	 * @Day : 2014. 11. 15.
	 * @Time : 오후 2:39:59
	 * @Explanation : Activity 호출 (Intent.FLAG_ACTIVITY_NEW_TASK)
	 *
	 * @param context
	 */
	private void callActivity(Context context) {
		if (INFO)
			Log.i(TAG, "callActivity()");
		Intent intent = new Intent("com.widget.CALL_ACTIVITY");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
