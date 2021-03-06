package com.mindframe.speechcards.model;

public class Speech {

	int id_speech;
	String title;
	int size;
	String color;
	int id_category;

	public Speech(int id_speech, String title, int size, String color, int id_category) {
		super();
		this.id_speech = id_speech;
		this.title = title;
		this.size = size;
		this.color = color;
		this.id_category = id_category;
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

	public int getId_category() {
		return id_category;
	}

	public void setId_category(int id_category) {
		this.id_category = id_category;
	}
	
}
