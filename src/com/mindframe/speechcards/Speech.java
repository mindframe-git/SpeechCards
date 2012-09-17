package com.mindframe.speechcards;

public class Speech {

	int id_speech;
	String title;

	public Speech(int id_speech, String title) {
		super();
		this.id_speech = id_speech;
		this.title = title;
	}
	
	public Speech() {
		super();
	}

	public int getId_speech() {
		return id_speech;
	}

	public void setId_speech(int id_speech) {
		this.id_speech = id_speech;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
