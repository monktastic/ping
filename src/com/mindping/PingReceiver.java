package com.mindping;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class PingReceiver extends BroadcastReceiver {
	final static long[] pulse = { 0, 200, //
			200, 200, //
			100, 100, //
			100, 200, //
			200, 200, //
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Toast.makeText(context, "Are you aware?", Toast.LENGTH_SHORT)
					.show();
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);

			vibrator.vibrate(pulse, -1);

			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 3);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		} catch (Exception e) {
			Toast.makeText(context, "ERRAR!: " + e, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} finally {
		}
	}

}
