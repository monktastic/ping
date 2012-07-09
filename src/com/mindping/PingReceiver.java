package com.mindping;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.Toast;

import com.mindping.UserHistory.Ping;
import com.mindping.UserHistory.Ping.PingResponse;
import com.mindping.UserHistory.Ping.PingType;

public class PingReceiver extends BroadcastReceiver {
	final static long[] SHAVE_AND_HAIRCUT_PULSE = { 0, 150, //
			250, 150, //
			150, 100, //
			100, 150, //
			250, 200, //
	};

	final static long[] PING_PULSE = { 0, //
			50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, };

	private static UserHistory history;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					MainActivity.SHARED_PREFS_NAME, Context.MODE_MULTI_PROCESS);

			boolean soundEnabled = preferences.getBoolean("is_sound_enabled",
					false);
			if (soundEnabled) {
				AssetFileDescriptor afd = context.getAssets().openFd(
						"simple_ping.wav");
				MediaPlayer player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
				player.prepare();
				player.start();
			}

			boolean vibrationEnabled = preferences.getBoolean(
					"is_vibration_enabled", false);
			if (vibrationEnabled) {
				Vibrator vibrator = (Vibrator) context
						.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(PING_PULSE, -1);
			}

			Toast.makeText(context, "Are you aware? ", Toast.LENGTH_SHORT)
					.show();

			if (history == null) {
				history = new UserHistory(context);
			}
			Ping ping = new UserHistory.Ping(new Date(), PingType.ONE_WAY,
					PingResponse.NONE);
			history.addPing(ping);

			// Kick off the next ping
			int minutes = preferences.getInt("ping_interval", 10);
			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Calendar cal = Calendar.getInstance();

			if (BuildConfig.DEBUG) {
				// Convert to seconds if in debug mode
				cal.add(Calendar.SECOND, minutes);
			} else {
				cal.add(Calendar.MINUTE, minutes);
			}
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		} catch (Exception e) {
			Toast.makeText(context, "ERRAR!: " + e, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} finally {
		}
	}

}
