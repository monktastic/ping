package com.mindping;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.common.collect.ImmutableList;

/**
 * Data format for user history, storing all interactions in the past.
 * 
 * @author joakima
 */
public class UserHistory extends SQLiteOpenHelper {
	public UserHistory(Context context) {
		super(context, "pings", null, 1);
	}

	public static class Ping {
		public Ping(Date date, PingType type, PingResponse response) {
			super();
			this.date = date;
			this.type = type;
			this.response = response;
		}

		public static enum PingType {
			ONE_WAY, TWO_WAY;

			private static List<PingType> databaseOrder = ImmutableList.of(ONE_WAY,
					TWO_WAY);
		}

		public static enum PingResponse {
			NONE, YES, NO;

			private static List<PingResponse> databaseOrder = ImmutableList.of(NONE,
					YES, NO);
		}

		final Date date;

		final PingType type;

		final PingResponse response;

		private ContentValues contentValues() {
			ContentValues values = new ContentValues();

			values.put("date", date.toString());
			values.put("type", PingType.databaseOrder.indexOf(type));
			values.put("response", PingResponse.databaseOrder.indexOf(response));

			return values;
		}
	}

	public void addPing(Ping ping) {
		getWritableDatabase().insert("pings", null, ping.contentValues());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE pings (id INTEGER PRIMARY KEY , date DATE , type INTEGER, response INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public int numPings() {
		Cursor cursor = getReadableDatabase().query("pings",
				new String[] { "count(0)" }, null, null, null, null, null);

		cursor.moveToFirst();
		int num = cursor.getInt(0);
		cursor.close();

		return num;
	}
}
