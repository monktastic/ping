/* The following code was written by Matthew Wiggins 
 * and is released under the APACHE 2.0 license 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.hlidskialf.android.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference implements
		SeekBar.OnSeekBarChangeListener {
	private static final String androidns = "http://schemas.android.com/apk/res/android";

	private SeekBar mSeekBar;
	private TextView mSplashText, mValueText;
	private Context mContext;

	private String mDialogMessage, mSuffix;
	private int mDefault, mMin, mMax, mValue = 0;

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
		mSuffix = attrs.getAttributeValue(androidns, "text");
		mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
		mMax = attrs.getAttributeIntValue(androidns, "max", 100);
		mMin = attrs.getAttributeIntValue(null, "min", 0);
	}

	@Override
	protected View onCreateDialogView() {
		LinearLayout.LayoutParams params;
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(6, 6, 6, 6);

		mSplashText = new TextView(mContext);
		if (mDialogMessage != null)
			mSplashText.setText(mDialogMessage);
		layout.addView(mSplashText);

		mValueText = new TextView(mContext);
		mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
		mValueText.setTextSize(32);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(mValueText, params);

		mSeekBar = new SeekBar(mContext);
		mSeekBar.setOnSeekBarChangeListener(this);
		layout.addView(mSeekBar, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		if (shouldPersist())
			mValue = getPersistedInt(mDefault);

		setMax(mMax - mMin);
		setProgress(mValue - mMin);

		return layout;
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		setMax(mMax - mMin);
		setProgress(mValue - mMin);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		super.onSetInitialValue(restore, defaultValue);
		if (restore)
			mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
		else
			mValue = (Integer) defaultValue;
	}

	@Override
	protected boolean persistInt(int value) {
		return super.persistInt(value);
	}

	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
		value += mMin;
		String t = String.valueOf(value);
		mValueText.setText(mSuffix == null ? t : t.concat(mSuffix));
		if (shouldPersist())
			persistInt(value);
		callChangeListener(Integer.valueOf(value));
	}

	public void onStartTrackingTouch(SeekBar seek) {
	}

	public void onStopTrackingTouch(SeekBar seek) {
	}

	public void setMax(int max) {
		if (mSeekBar != null)
			mSeekBar.setMax(max);
	}

	public int getMax() {
		return mMax;
	}

	public void setProgress(int progress) {
		mValue = progress;
		if (mSeekBar != null) {
			mSeekBar.setProgress(progress + mMin);
		}
	}

	public int getProgress() {
		return mValue;
	}
}