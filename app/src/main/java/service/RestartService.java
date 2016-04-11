package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by oh on 2015-03-26.
 */
public class RestartService extends BroadcastReceiver {

    //Widget Update Manager
    private PendingIntent mAlarmSender;
    private AlarmManager mAlarmMgr;
    @Override
    public void onReceive(Context context, Intent intent) {

    /* 폰 재부팅할때 서비스 등록 */
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("RestartService", "ACTION_BOOT_COMPLETED");
            requestWidgetAlarm(context);
        }
    }
    public void requestWidgetAlarm(Context context){
        //Widget Update Manager
        Calendar cal = Calendar.getInstance (); // 현재시점의 객체를 가져옴
        // cal.set(Calendar.MINUTE, 35);
        Log.d("설정 시간", cal.getTime().toString());
        mAlarmSender = PendingIntent.getService(context,
                0, new Intent(context, HYService.class), 0);
        mAlarmMgr = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        mAlarmMgr.setRepeating(AlarmManager.RTC, cal.getTime().getTime(), 60 * 60 * 1000, mAlarmSender);
    }
}
