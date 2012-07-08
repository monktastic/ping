package com.mindping;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class PingReceiver extends BroadcastReceiver {
	final static long[] SHAVE_AND_HAIRCUT_PULSE = { 0, 150, //
			250, 150, //
			150, 100, //
			100, 150, //
			250, 200, //
	};

	final static long[] PING_PULSE = { 0, //
			50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, };

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			AssetFileDescriptor afd = context.getAssets().openFd(
					"simple_ping.wav");
			MediaPlayer player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			player.prepare();
			player.start();

			Toast.makeText(context, "Are you aware?", Toast.LENGTH_SHORT)
					.show();
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);

			vibrator.vibrate(PING_PULSE, -1);

			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			int minutes = preferences.getInt("ping_interval", 0);

			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, minutes);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		} catch (Exception e) {
			Toast.makeText(context, "ERRAR!: " + e, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} finally {
		}
	}

}
