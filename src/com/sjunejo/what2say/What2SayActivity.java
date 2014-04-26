package com.sjunejo.what2say;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sjunejo.utils.CSVLoader;
import com.sjunejo.what2say.sqlite.Topic;
import com.sjunejo.what2say.sqlite.TopicsDataSource;

/**
 * The main activity launched when the program is run.
 * @author Sadruddin Junejo
 *
 */
public class What2SayActivity extends Activity implements OnClickListener {
	
	protected static ArrayList<String> arrayTopics;
	TopicsDataSource topicsDataSource;
	Button btnGenerate;
	TextView tvTopic;
	List<Topic> topics;
	TextView tvHeader;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialise();
    }
    
    private void initialise(){
    	btnGenerate = (Button) findViewById(R.id.btnGenerate);
    	tvTopic = (TextView) findViewById(R.id.tvTopic);
    	btnGenerate.setOnClickListener(this);
    	tvHeader = (TextView) findViewById(R.id.tvHeader);
    	tvHeader.setText(tvHeader.getText());
    	topicsDataSource = new TopicsDataSource(this);
    	topicsDataSource.open();
    	topics = new ArrayList<Topic>();
    	insertCSVTopics();
    }
    
    private void insertCSVTopics(){
    	List<Topic> csvTopics = CSVLoader.getInstance().loadCSVTopics();

    	Log.d("CSV_Topics", csvTopics.toString());
    	// Don't add topics that are already in the database!
    	List<Topic> allTopics = topicsDataSource.getAllTopics();
    	List<Topic> duplicateTopics = new ArrayList<Topic>();
    	duplicateTopics.addAll(csvTopics);
    	Log.d("dup_topics", duplicateTopics.toString());
    	duplicateTopics.retainAll(allTopics);
    	Log.d("duplicateTopics", duplicateTopics.toString());
    	csvTopics.removeAll(duplicateTopics);
    	Log.d("New topics", csvTopics.toString());
    	topicsDataSource.insertTopics(csvTopics);
    }
    
    /**
     * Handles the pressing of the 'generate' button.
     * Uses a case/switch statement in order to support additional functionality.
     */
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btnGenerate:
			// There might not be any topics because the app user is silly.
			topics = topicsDataSource.getAllTopics();
			if (!topics.isEmpty()) {
				String str = topics.get(randomString()).toString();
				if (topics.size()>1){ // does not 'randomise' if there is only one topic in the database as loop would never terminate.
					while (str.equals(tvTopic.getText().toString())){
						str = topics.get(randomString()).toString();
					}
				}
				tvTopic.setText(str);
			}
			else
				tvTopic.setText("No topics found. You deleted them all. GG.");
		}
		
	}
	
	// used to select a random string from the database
	int randomString(){
		return new Random().nextInt(topics.size());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Controls what occurs when a menu item is selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			// The Add/Delete Topics menu item
			case R.id.itemAddDelTopics:
				try {
					// move to the "Add/Delete Topics" View
					startActivity(new Intent(this, Class.forName("com.sjunejo.what2say.AddDeleteTopics")));
				}
				catch (ClassNotFoundException e){
					e.printStackTrace();
				}
				break;
			// The 'About' menu item
			case R.id.itemAbout:
				try {
					// move to the "Add/Delete Topics" View
					startActivity(new Intent(this, Class.forName("com.sjunejo.what2say.AboutPage")));
				}
				catch (ClassNotFoundException e){
					e.printStackTrace();
				}
				break;
			// The 'Close' menu item
			case R.id.itemClose: // Close
				finish(); // exit the program
				break;
		}
		return super.onOptionsItemSelected(item);
	}
    
	@Override
	protected void onResume() {
		// prepares database for writing
		topicsDataSource.open(); 
		super.onResume();
	}

	@Override
	protected void onPause() {
		// closes database
		topicsDataSource.close();
		super.onPause();
	}
	
}