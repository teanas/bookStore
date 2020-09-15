package com.testbed.order.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Order {

	@Id
	private String id;
	private String userId;
	private String bookId;
	private boolean closed;
	private long timeStamp;
	
	public Order(String userId, String bookId, boolean closed) {
		this.userId = userId;
		this.bookId = bookId;
		this.closed = closed;
		this.timeStamp = System.currentTimeMillis();
	}
	
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBookId() {
		return bookId;
	}
	
	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	
	@Override
	public String toString() {
		return "Order UserId: " + userId + ", BookId: " + bookId + ", Timestamp: " + timeStamp;
	}
	

}
