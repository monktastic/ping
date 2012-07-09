package com.mindping;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * An activity that shows stats about the user history.
 * 
 * @author joakima
 */
public class StatsActivity extends Activity {
	private UserHistory userHistory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		userHistory = new UserHistory(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_stats, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();

		StringBuilder sb = new StringBuilder();
		sb.append("number of pings: ");
		sb.append(userHistory.numPings());
		sb.append("\n");

		TextView textView = (TextView) findViewById(R.id.stats_text);
		textView.setText(sb.toString());
	}
}
