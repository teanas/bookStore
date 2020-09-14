package com.testbed.user.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
	
	@Id
	private String id;
	private String userName;
	private String password;
	private List<String> bookIds;
	

	public User() {
		this.bookIds = new ArrayList<String>();
	}
	public User(String userName, String password, List<String> bookIds) {
		this.userName = userName;
		this.password = password;
		this.bookIds = bookIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getOrders() {
		return bookIds;
	}

	public void setOrders(List<String> bookIds) {
		this.bookIds = bookIds;
	}
	
	public String toString() {
		return "User Name: " +userName+", Password: " + password + ", BookIds: " + bookIds;
	}

}
