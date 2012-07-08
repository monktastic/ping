package com.mindping;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		final Intent intent = new Intent(this, PingReceiver.class);
		final PendingIntent sender = PendingIntent.getBroadcast(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);
		pingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!pingButton.isChecked()) {
					// disable future events
					am.cancel(sender);
				} else {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.SECOND, 3);
					am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
							sender);
				}
			}
		});

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
