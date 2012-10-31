package com.sjunejo.what2say;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.sjunejo.what2say.R;

/**
 * Simple About view.
 * @author Sadruddin Junejo
 *
 */
public class AboutPage extends Activity {

	TextView tvAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initialise();
	}
	
	void initialise(){
		tvAbout = (TextView) findViewById(R.id.tvAbout);
		
		// Set 'about' text
		tvAbout.setText("\nWhat2Say Random Topic Generator \nVersion 1.5 \n\n" +
				"Developed By: Sadruddin Junejo \n\n" +
				"Website: what2sayapp.blogspot.com\n" + 
				"Contact: what2sayapp@gmail.com");
	}
	
	

}
