package com.mindframe.speechcards;

public class ListaItem {
	private String title;
	private String nCards;
	
	
	public ListaItem(String title, String nCards) {
		super();
		this.title = title;
		this.nCards = nCards;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getnCards() {
		return nCards;
	}


	public void setnCards(String nCards) {
		this.nCards = nCards;
	}
	
	
	
}
