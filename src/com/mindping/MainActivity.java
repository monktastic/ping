package com.mindping;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private PendingIntent sender;

	static String SHARED_PREFS_NAME = "main_activity_prefs";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set default values in preferences
		PreferenceManager.setDefaultValues(this.getApplicationContext(),
				R.xml.settings, false);

		final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		final Intent intent = new Intent(this, PingReceiver.class);
		sender = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);

		pingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SharedPreferences preferences = MainActivity.this
						.getSharedPreferences(SHARED_PREFS_NAME,
								MODE_MULTI_PROCESS);

				final int minutes = preferences.getInt("ping_interval", 0);
				Toast.makeText(MainActivity.this, "Duration " + minutes,
						Toast.LENGTH_SHORT).show();

				if (!pingButton.isChecked()) {
					// disable future events
					am.cancel(sender);
				} else {
					Calendar cal = Calendar.getInstance();
					if (!BuildConfig.DEBUG) {
						// In debug mode, ping now
						cal.add(Calendar.MINUTE, minutes);
					}
					am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
							sender);
				}
			}
		});

		final Button statsButton = (Button) findViewById(R.id.stats_button);
		statsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						StatsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);
		savedInstanceState.putBoolean("is_pinging", pingButton.isChecked());
		if (BuildConfig.DEBUG) {
			Toast.makeText(this, "Saving " + pingButton.isChecked(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		boolean checked = savedInstanceState.getBoolean("is_pinging");
		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);
		pingButton.setChecked(checked);
	}

	@Override
	public void onDestroy() {
		stop();
		super.onDestroy();
	}

	@Override
	public void finish() {
		stop();
		super.finish();
	}

	private void stop() {
		if (BuildConfig.DEBUG) {
			Toast.makeText(this, "All pings stopped!", Toast.LENGTH_SHORT)
					.show();
		}
		final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		if (sender != null) {
			am.cancel(sender);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			break;
		case R.id.menu_about:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://sites.google.com/site/mindpingapp/"));
			startActivity(browserIntent);
			break;
		}

		return true;
	}
}
