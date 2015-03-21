package com.bernard.beaconportal.activities.homeworkdue.alarms;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AlarmReset extends BroadcastReceiver {

	final static int RQS_1 = 1;

	@Override
	public void onReceive(Context context, Intent intent) {

		setAlarm(context);

		SharedPreferences refresh_time = context.getSharedPreferences("Alarm",
				Context.MODE_PRIVATE);

		int hour = refresh_time.getInt("Hour", 15);

		int minute = refresh_time.getInt("Minute", 0);

		
		if(hour == 10000 && minute == 10000){
			
			Log.d("Alarm Reset","alarm was canceled");
			
		}else{
		
		setAlarmCustom(context, hour, minute);
		
		Log.d("Alarm Reset","alarm reset");

		}
		
	}

	private void setAlarm(Context context) {

		Intent intent = new Intent(context, MidnightHomeworkDownload.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				RQS_1, intent, 0);

		Calendar calNow = Calendar.getInstance();
		Calendar targetCal = (Calendar) calNow.clone();

		targetCal.set(Calendar.HOUR_OF_DAY, 0);
		targetCal.set(Calendar.MINUTE, 0);
		targetCal.set(Calendar.SECOND, 0);
		targetCal.set(Calendar.MILLISECOND, 0);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
				pendingIntent);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);

		System.out.println("midnight");

	}

	private void setAlarmCustom(Context context, int hour, int minute) {

		Calendar calNow = Calendar.getInstance();
		Calendar targetCal = (Calendar) calNow.clone();

		targetCal.set(Calendar.HOUR_OF_DAY, hour);
		targetCal.set(Calendar.MINUTE, minute);
		targetCal.set(Calendar.SECOND, 0);
		targetCal.set(Calendar.MILLISECOND, 0);

		Intent intent = new Intent(context, DailyHomeworkDownload.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				RQS_1, intent, 0);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);

		System.out.println(targetCal);

		System.out.println("alarm reset three");

	}

}