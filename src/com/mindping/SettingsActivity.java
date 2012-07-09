package com.mindping;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName(MainActivity.SHARED_PREFS_NAME);
		prefMgr.setSharedPreferencesMode(MODE_MULTI_PROCESS);
		
		addPreferencesFromResource(R.xml.settings);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			overridePendingTransition(0, 0);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
