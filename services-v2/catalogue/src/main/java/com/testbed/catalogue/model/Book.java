package com.testbed.catalogue.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Book {
	
	@Id
	private String id;
	private String name;
	private String author;
	private String genre;
	private int quantity;
	private boolean available;
	
	
	public Book(String name, String author, String genre, int quantity) {
		this.name = name;
		this.author = author;
		this.genre = genre;
		this.quantity = quantity;
		if(quantity>0) {
			this.available = true; 
		}else {
			this.available = false;
		}
	}
	//GETTERS and SETTERS
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}

	public boolean isAvailable() {
		return available;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Book Name: "+name+", Author:"+author+", Genre:"+genre +", Books left: " + quantity + ", Availability: " + available;
	}
	
	
}
