package com.testbed.employee.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Employee {
	
	@Id
	private String id;
	
	private String name;
	private String position;
	
	public Employee(String name, String position) {
		this.name = name;
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Employee Id: " + id + ", Name: " + name + ", Position: " + position;
	}

}
