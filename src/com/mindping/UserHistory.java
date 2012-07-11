package com.mindping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.common.collect.ImmutableList;
import com.mindping.UserHistory.Ping.PingResponse;
import com.mindping.UserHistory.Ping.PingType;

/**
 * Provider of user history. Stores it in a SQLite database.
 * 
 * @author joakima
 */
public class UserHistory extends SQLiteOpenHelper {
	public UserHistory(Context context) {
		super(context, "pings", null, 2);
	}

	/**
	 * This is a single ping by the app, either responded to or not.
	 */
	public static class Ping {
		private Ping(Long id, Date date, PingType type, PingResponse response) {
			this.id = id;
			this.date = date;
			this.type = type;
			this.response = response;
		}

		private Ping(Date date, PingType type, PingResponse response) {
			this(null, date, type, response);
		}

		public static enum PingType {
			ONE_WAY, TWO_WAY;

			/**
			 * This decides what integers are used to represent these enums in
			 * database. Do not reorder existing values.
			 */
			private static List<PingType> databaseOrder = ImmutableList.of(
					ONE_WAY, TWO_WAY);
		}

		public static enum PingResponse {
			NONE, YES, NO;

			/**
			 * This decides what integers are used to represent these enums in
			 * database. Do not reorder existing values.
			 */
			private static List<PingResponse> databaseOrder = ImmutableList.of(
					NONE, YES, NO);
		}

		public final Long id;

		public final Date date;

		public final PingType type;

		public final PingResponse response;

		private ContentValues contentValues() {
			ContentValues values = new ContentValues();

			if (id != null) {
				values.put("_id", id);
			}
			values.put("date", iso8601Format.format(date));
			values.put("type", PingType.databaseOrder.indexOf(type));
			values.put("response", PingResponse.databaseOrder.indexOf(response));

			return values;
		}

		private static SimpleDateFormat iso8601Format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.sss");

		private static Ping fromContentValues(ContentValues values)
				throws ParseException {
			Long id = values.getAsLong("_id");
			Date date = iso8601Format.parse(values.getAsString("date"));
			PingType type = PingType.databaseOrder.get(values
					.getAsInteger("type"));
			PingResponse response = PingResponse.databaseOrder.get(values
					.getAsInteger("response"));

			return new Ping(id, date, type, response);
		}
	}

	private Ping getPing(Long id) {
		Cursor cursor = getReadableDatabase().query("pings",
				new String[] { "_id", "date", "type", "response" }, "_id = ?",
				new String[] { id.toString() }, null, null, null);

		Ping ping = null;

		if (cursor.getCount() >= 1) {
			cursor.moveToFirst();
			ContentValues values = new ContentValues();

			values.put("_id", cursor.getString(0));
			values.put("date", cursor.getString(1));
			values.put("type", cursor.getString(2));
			values.put("response", cursor.getString(3));

			try {
				ping = Ping.fromContentValues(values);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}

		cursor.close();
		return ping;
	}

	public Ping createPing(Date date, PingType type, PingResponse response) {
		Ping ping = new Ping(date, type, response);

		Long id = getWritableDatabase().insert("pings", null,
				ping.contentValues());

		return getPing(id);
	}

	public int numPings() {
		Cursor cursor = getReadableDatabase().query("pings",
				new String[] { "count(0)" }, null, null, null, null, null);

		cursor.moveToFirst();
		int num = cursor.getInt(0);
		cursor.close();

		return num;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE pings (_id INTEGER PRIMARY KEY , date STRING , type INTEGER, response INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			// These were only used before release ÐÊjust drop them.
			db.execSQL("DROP TABLE pings");
			onCreate(db);
			break;
		default:
			throw new RuntimeException("Unknown old version of database");
		}
	}
}
