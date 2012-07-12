package com.mindping;

import java.util.Calendar;

import android.app.Activity;
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
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	static String SHARED_PREFS_NAME = "main_activity_prefs";

	public static SharedPreferences getPreferences(Context context) {
		// set default values in preferences
		PreferenceManager.setDefaultValues(context, R.xml.settings, false);

		SharedPreferences preferences = context.getSharedPreferences(
				MainActivity.SHARED_PREFS_NAME, Context.MODE_MULTI_PROCESS);

		return preferences;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getPreferences(this);

		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);
		pingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (pingButton.isChecked()) {
					// start pinging
					SharedPreferences prefs = MainActivity
							.getPreferences(MainActivity.this);
					int minutes = prefs.getInt("ping_interval", 0);
					Calendar cal = Calendar.getInstance();
					if (!BuildConfig.DEBUG) {
						// In debug mode, ping now
						cal.add(Calendar.MINUTE, minutes);
					}

					Pinger.sendPing(MainActivity.this, cal.getTimeInMillis());
				} else {
					// disable future events
					Pinger.cancelPings(MainActivity.this);
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
	protected void onResume() {
		super.onResume();

		SharedPreferences prefs = MainActivity.getPreferences(this);
		boolean isPinging = prefs.getBoolean("is_pinging", false);

		ToggleButton tb = (ToggleButton) findViewById(R.id.ping_toggleButton);
		tb.setChecked(isPinging);
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
