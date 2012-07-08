package com.mindping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class PingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Toast.makeText(context, "Are you aware?", Toast.LENGTH_SHORT).show();
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			
			long[] pulse = {0, 200, //
					200, 200, //
					100, 100, //
					100, 200, //
					200, 200, //
					};
			
			vibrator.vibrate(pulse, -1);
		} catch (Exception e) {
			Toast.makeText(context, "ERRAR!: " + e, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} finally {
		}
	}

}
