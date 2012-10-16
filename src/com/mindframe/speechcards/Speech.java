package com.mindframe.speechcards;

public class Speech {

	int id_speech;
	String title;
	int size;
	String color;

	public Speech(int id_speech, String title, int size, String color) {
		super();
		this.id_speech = id_speech;
		this.title = title;
		this.size = size;
		this.color = color;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
