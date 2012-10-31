package com.sjunejo.what2say.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper class. Necessary for creating and upgrading the database.
 * @author Sadruddin Junejo
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TOPICS = "topics";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TOPIC = "topic";
	
	// I should probably  move this.

	
	private static final String DATABASE_NAME = "topics.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_TOPICS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_TOPIC
			+ " text not null);";

	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE); // Executes the query specified by the string DATABASE_CREATE
		
		// Initial Population goes here
		ContentValues values = new ContentValues();
		for (String defaultTopic: InitialData.DEFAULT_TOPICS){
			values.put(this.COLUMN_TOPIC, defaultTopic);
			db.insert(TABLE_TOPICS, null, values);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
		onCreate(db);
	}

}
