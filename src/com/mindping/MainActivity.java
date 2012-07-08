package com.mindping;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private PendingIntent sender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// set default values in preferences
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);

		final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		final Intent intent = new Intent(this, PingReceiver.class);
		sender = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		final SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);

		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);

		pingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!pingButton.isChecked()) {
					// disable future events
					am.cancel(sender);
				} else {
					int minutes = preferences.getInt("ping_interval", 0);
					Calendar cal = Calendar.getInstance();
					// cal.add(Calendar.SECOND, minutes);
					am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
							sender);
				}
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);
		savedInstanceState.putBoolean("is_pinging", pingButton.isChecked());
		Toast.makeText(this, "Saving " + pingButton.isChecked(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		boolean checked = savedInstanceState.getBoolean("is_pinging");
		Toast.makeText(this, "2) Value is " + checked, Toast.LENGTH_SHORT)
				.show();
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
		Toast.makeText(this, "All pings stopped!", Toast.LENGTH_SHORT).show();
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
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		return true;
	}
}
