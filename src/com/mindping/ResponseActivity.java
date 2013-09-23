package com.mindping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;

/**
 * This class presents an AlertDialog asking the user whether they are aware. It
 * is responsible for kicking off new pings when done.
 * 
 * @author aprasad
 */
public class ResponseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Ping response");
		alertDialog.setMessage("Are you aware?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yup", new DialogResponse(true));
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nope", new DialogResponse(false));
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.response, menu);
		return true;
	}

	class DialogResponse implements DialogInterface.OnClickListener {
		DialogResponse(boolean accepted) {
			super();
		}

		public void onClick(DialogInterface dialog, int which) {
			// TODO(aprasad) Is it safe to send a ping with "this" Context if I
			// am calling finish()?
			Pinger.sendPing(ResponseActivity.this);
			finish();
		}
	}

}
