package com.sjunejo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.sjunejo.what2say.sqlite.Topic;

public class CSVLoader {
	
	private static CSVLoader loader;
	
	private static final String FOLDER_PATH = Environment.getExternalStorageDirectory() + "/what2say/";
	private static final String CSV_PREFIX = ".csv";
	private static final String CSV_SPLIT_BY_REGEX = ",|\n";
	
	private CSVLoader(){}
	
	public static CSVLoader getInstance(){
		if (loader == null){
			loader = new CSVLoader();
		}
		return loader;
	}
	
	/**
	 * Loop through each CSV file and add contents to array list of strings
	 * @return array list of strings to be added to SQLite Database
	 */
	public List<Topic> loadCSVTopics(){
		List<Topic> csvTopics = new ArrayList<Topic>();
		
		// Check for files in directory
		File topDirectory = new File(FOLDER_PATH);
		
		BufferedReader bufferedReader = null;
	
		Log.d("topDirectory", topDirectory.toString());
		// Loop through all .csv files
		File[] allFiles = topDirectory.listFiles();
			
		if (allFiles != null){
			for (File file: allFiles){
				// May not be a .csv file so best to check
				String fileName = file.getName();
				if (fileName.substring(fileName.length()-CSV_PREFIX.length(), 
						fileName.length()).toLowerCase()
						.equals(CSV_PREFIX)){
						
					String line;
					// .csv file found - parse data and add to string
					try {
						bufferedReader = new BufferedReader(new FileReader(file));
						while ((line = bufferedReader.readLine()) != null){
							String[] strings = line.split(CSV_SPLIT_BY_REGEX);
							String strTrimmed;
							for (String str: strings){
								if (!(strTrimmed = str.trim()).equals("")){
									Log.d("String", strTrimmed);
									csvTopics.add(new Topic(strTrimmed));
								}
							}
						}
						bufferedReader.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
		
		return csvTopics;
	}

}
