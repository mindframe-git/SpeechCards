package com.mindframe.speechcards.model;

import android.graphics.Color;

public class Category {

	int id;
	String name;
	String color;
	public Category(String name, String color) {
		super();
		this.name = name;
		this.color = color;
	}
	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	/**
	 * 
	 * Esta función coge el atributo color, 
	 * que estará almacenado de la siguiente 
	 * forma "alfa,rojo,verde,azul"
	 * y devuelve el código del color.
	 * 
	 * @return int color
	 */
	public int getColorCode(){
		
		String[] arrColors = color.split(",");
		
		return Color.argb(Integer.valueOf(arrColors[0]), Integer.valueOf(arrColors[1]), Integer.valueOf(arrColors[2]), Integer.valueOf(arrColors[3]));
		
	}
	
	
	
}
