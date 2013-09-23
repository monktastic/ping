package com.mindping;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference preference = findPreference(key);
		if (key.equals("ping_interval_min")) {
			int value = sharedPreferences.getInt(key, 0);
			String summary = this.getString(
					R.string.settings_summary_ping_interval, value);
			preference.setSummary(summary);
		} else if (key.equals("aware_check_percent")) {
			int value = sharedPreferences.getInt(key, 0);
			String summary = this.getString(
					R.string.settings_summary_aware_reports, value);
			preference.setSummary(summary);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName(MainActivity.SHARED_PREFS_NAME);
		prefMgr.setSharedPreferencesMode(MODE_MULTI_PROCESS);
		SharedPreferences prefs = prefMgr.getSharedPreferences();
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		addPreferencesFromResource(R.xml.settings);
		
		onSharedPreferenceChanged(prefs, "ping_interval");
		onSharedPreferenceChanged(prefs, "aware_checks");
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
