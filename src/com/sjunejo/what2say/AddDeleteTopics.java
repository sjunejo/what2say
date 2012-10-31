package com.sjunejo.what2say;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.sjunejo.what2say.sqlite.SQLiteHelper;
import com.sjunejo.what2say.sqlite.Topic;
import com.sjunejo.what2say.sqlite.TopicsDataSource;

/**
 * The class that controls the addition and delete of topics to the SQLite database.;
 * @author Sadruddin Junejo
 *
 */
public class AddDeleteTopics extends ListActivity {
	private TopicsDataSource datasource;
	public EditText etTopic;
	private ArrayAdapter<Topic> adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.databasetest);

		etTopic  = (EditText) findViewById(R.id.etTopic);
		datasource = new TopicsDataSource(this);
		datasource.open();

		List<Topic> values = datasource.getAllTopics();

		// Adapter is used to display elements in the form of a ListView
		 adapter = new ArrayAdapter<Topic>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		
	}

	// Called via 'onClick' attribute of buttons as specified in the corresponding .xml
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		Topic Topic = null;
		switch (view.getId()) {
		case R.id.btnAdd:
			// Save the new Topic to the database
			if (!etTopic.getText().toString().matches("[\\s]*")){ // if it's not just whitespace
				Topic = datasource.createTopic(etTopic.getText().toString());
				adapter.add(Topic);
				etTopic.setText("");
			}
			break;
		case R.id.btnBla: // I guess this is the delete button
			int rr = datasource.getAllTopics().size();
			for (int i = 0; i < rr; i++){
				adapter.remove(adapter.getItem(0));
			}
			datasource.deleteDatabaseTable();
			break;
		case R.id.btnReset:
				datasource.deleteDatabaseTable();
				datasource.fillWithDefaultTopics();
				this.finish();		
			break;
		}
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ArrayAdapter<Topic> adapter = (ArrayAdapter<Topic>) getListAdapter();
		Topic topic = (Topic) getListAdapter().getItem(position);
		datasource.deleteTopic(topic);
		adapter.remove(topic);
	}

	
}