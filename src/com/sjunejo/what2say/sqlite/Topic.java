package com.sjunejo.what2say.sqlite;

public class Topic {
	
	private long id;
	private String topic;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	// Will be used by Adapter in ListView. Apparently.
	@Override
	public String toString(){
		return topic;
	}
	
	

}
