package com.mindframe.speechcards;

public class Card {

	int id_card;
	int id_prev_card;
	int id_next_card;
	String header;
	String body;
	int id_speech;

	public Card(int id_card, int id_prev_card, int id_next_card, String header, String body, int id_speech) {
		super();
		this.id_card = id_card;
		this.id_prev_card = id_prev_card;
		this.id_next_card = id_next_card;
		this.header = header;
		this.body = body;
		this.id_speech = id_speech;
	}
	
	public Card() {
		super();
	}

	
	public int getId_card() {
		return id_card;
	}

	public void setId_card(int id_card) {
		this.id_card = id_card;
	}

	public int getId_prev_card() {
		return id_prev_card;
	}

	public void setId_prev_card(int id_prev_card) {
		this.id_prev_card = id_prev_card;
	}

	public int getId_next_card() {
		return id_next_card;
	}

	public void setId_next_card(int id_next_card) {
		this.id_next_card = id_next_card;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getId_speech() {
		return id_speech;
	}

	public void setId_speech(int id_speech) {
		this.id_speech = id_speech;
	}
	
	public boolean isFirst(){
		if(this.id_prev_card == 0)
			return true;
		else
			return false;
				
	}
	
	public boolean isLast(){
		if(this.id_next_card == 0)
			return true;
		else
			return false;
	}

}
