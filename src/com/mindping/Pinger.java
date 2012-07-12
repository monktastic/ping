package com.mindping;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.mindping.UserHistory.Ping.PingResponse;
import com.mindping.UserHistory.Ping.PingType;

public class Pinger extends BroadcastReceiver {
	private static final String TAG = "Pinger";

	private static UserHistory history;

	final static long[] SHAVE_AND_HAIRCUT_PULSE = { 0, 150, //
			250, 150, //
			150, 100, //
			100, 150, //
			250, 200, //
	};

	final static long[] PING_PULSE = { 0, //
			50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, };

	/**
	 * Plays the sound specified in soundName.
	 * 
	 * @param context
	 *            A valid context
	 * @param soundName
	 *            The name of the sound file
	 */
	private void playSound(Context context, String soundName) {
		try {
			AssetFileDescriptor afd = context.getAssets().openFd(soundName);
			MediaPlayer player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			player.prepare();
			player.start();
		} catch (IOException e) {
			System.err.println("Couldn't play file with name " + soundName);
		}
	}

	private void vibrate(Context context, long[] pattern) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, -1);
	}

	private static PendingIntent getPendingIntent(Context context) {
		Intent intent = new Intent(context, Pinger.class);
		PendingIntent action = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return action;
	}

	/**
	 * Sends a ping scheduled to arrive at millisTime.
	 * 
	 * @param context
	 *            A valid context.
	 * @param millisTime
	 *            The time at which the ping should arrive, in milliseconds.
	 */
	public static void sendPing(Context context, long millisTime) {
		Log.i(TAG, "Starting pinging in " + (millisTime - new Date().getTime())
				/ 1000.0f + " seconds");

		SharedPreferences prefs = MainActivity.getPreferences(context);
		Editor editor = prefs.edit();
		editor.putBoolean("is_pinging", true);
		editor.apply();

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, millisTime, getPendingIntent(context));
	}

	/**
	 * Cancels all future pings.
	 * 
	 * @param context
	 *            A valid context.
	 */
	public static void cancelPings(Context context) {
		Log.i(TAG, "Cancelling pinging");

		SharedPreferences prefs = MainActivity.getPreferences(context);
		Editor editor = prefs.edit();
		editor.putBoolean("is_pinging", false);
		editor.apply();

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.cancel(getPendingIntent(context));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			SharedPreferences prefs = MainActivity.getPreferences(context);

			// play sound if enabled
			if (prefs.getBoolean("is_sound_enabled", false)) {
				playSound(context, "simple_ping.wav");
			}

			// vibrate if enabled
			if (prefs.getBoolean("is_vibration_enabled", false)) {
				vibrate(context, PING_PULSE);
			}

			// toast!
			Toast.makeText(context, "Are you aware? ", Toast.LENGTH_SHORT)
					.show();

			if (history == null) {
				history = new UserHistory(context);
			}
			history.createPing(new Date(), PingType.ONE_WAY, PingResponse.NONE);

			// Kick off the next ping
			int minutes = prefs.getInt("ping_interval", 10);
			Calendar cal = Calendar.getInstance();
			if (BuildConfig.DEBUG) {
				// Convert to seconds if in debug mode
				cal.add(Calendar.SECOND, minutes);
			} else {
				cal.add(Calendar.MINUTE, minutes);
			}
			sendPing(context, cal.getTimeInMillis());
		} catch (Exception e) {
			Toast.makeText(context, "ERRAR!: " + e, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

}
