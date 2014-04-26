package com.sjunejo.what2say.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * The Data Access Object Class. Responsible for maintaining database connection as well as
 * fetching and adding new topics.
 * @author Sunny
 *
 */
public class TopicsDataSource {
	
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = { SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_TOPIC };
	
	public TopicsDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	} 

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Topic createTopic(String topic) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TOPIC, topic);
		long insertId = database.insert(SQLiteHelper.TABLE_TOPICS, null,
				values);
		Cursor cursor = database.query(SQLiteHelper.TABLE_TOPICS,
				allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst(); // the topic is added to the top of the database
		Topic newTopic = cursorToTopic(cursor);
		cursor.close();
		return newTopic;
	}

	public void deleteTopic(Topic Topic) {
		long id = Topic.getId();
		System.out.println("Topic deleted with id: " + id);
		database.delete(SQLiteHelper.TABLE_TOPICS, SQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public void insertTopics(List<Topic> topics){
		database.beginTransaction();
		for (Topic t: topics){
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_TOPIC, t.getTopic());
			database.insert(SQLiteHelper.TABLE_TOPICS, null, values);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}
	public void fillWithDefaultTopics(){ 
		database.beginTransaction();
		try {
			for (String str: InitialData.DEFAULT_TOPICS){
				ContentValues values = new ContentValues();
				values.put(SQLiteHelper.COLUMN_TOPIC, str);
				long insertId = database.insert(SQLiteHelper.TABLE_TOPICS, null,
						values);
				
			}
			database.setTransactionSuccessful();
		}
		catch (Exception e){
			
		} 
		finally {
			database.endTransaction();
		}
	}
	public void deleteDatabaseTable(){
		database.delete(SQLiteHelper.TABLE_TOPICS, null, null);
	}
	
	public List<Topic> getAllTopics() {
		List<Topic> Topics = new ArrayList<Topic>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_TOPICS,
				allColumns, null, null, null, null, null);
		
		// Cursor points to topics...
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Topic Topic = cursorToTopic(cursor);
			Topics.add(Topic);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return Topics;
	}

	private Topic cursorToTopic(Cursor cursor) {
		Topic Topic = new Topic();
		Topic.setId(cursor.getLong(0)); // takes ID
		Topic.setTopic(cursor.getString(1)); // takes Topic
		return Topic;
	}
	
}


