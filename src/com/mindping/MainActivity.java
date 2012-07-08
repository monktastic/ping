package com.mindping;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ToggleButton pingButton = (ToggleButton) findViewById(R.id.ping_toggleButton);
		pingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						PingReceiver.class);
				PendingIntent sender = PendingIntent.getBroadcast(
						MainActivity.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// Get the AlarmManager service
				AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				
				if (!pingButton.isChecked()) {
					// disable future events
					am.cancel(sender);
				} else {
					// get a Calendar object with current time
					Calendar cal = Calendar.getInstance();
					// add 5 seconds to the calendar object
					cal.add(Calendar.SECOND, 5);

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
}
