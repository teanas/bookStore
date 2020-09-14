package com.testbed.user.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cart {
	
	@Id
	private String userId;
	private List<String> bookIds;
	
	public Cart() {
		this.bookIds = new ArrayList<String>();
	}
	public Cart(String userId, List<String> bookIds) {
		this.userId = userId;
		this.bookIds = bookIds;
	}
	
	
	public String getUserId() {
		return userId;
	}
	
	public void setId(String userId) {
		this.userId=userId;
	}
	
	public List<String> getBookIds(){
		return bookIds;
	}
	
	public void setBookIds(List<String> bookIds){
		this.bookIds = bookIds;
	}

}
