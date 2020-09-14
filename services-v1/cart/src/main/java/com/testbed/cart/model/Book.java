package com.testbed.cart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Book {
	
	@Id
	private String id;
	private boolean available;
	
	public Book() {
	}
	//GETTERS and SETTERS
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id=id;
	}

	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}

	
	
}
