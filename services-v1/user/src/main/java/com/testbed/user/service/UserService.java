package com.testbed.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbed.user.model.User;
import com.testbed.user.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User create(String id) {
		User user = new User();
		user.setId(id);
		return userRepository.save(user);
	}
	
	public User create(String userName, String password) {
		User user = new User(userName, password, new ArrayList<String>());
		return userRepository.save(user);
	}

	public List<User> getAll(){
		return userRepository.findAll();
	}
	
	public User getById(String id) {
		if(id == null | userRepository.findById(id).isEmpty()) return null;
		return userRepository.findById(id).get();
	}
	
	public User update(User newUser) {
		if(userRepository.findById(newUser.getId()).isEmpty()) return null;
		User user = userRepository.findById(newUser.getId()).get();
		user.setUserName(newUser.getUserName());
		user.setPassword(newUser.getPassword());
		user.setOrders(newUser.getOrders());
		return userRepository.save(user);
	}
	
	public User addBook(String userId, String bookId) {
		if(userId == null | userRepository.findById(userId).isEmpty()) return null;
		User user = userRepository.findById(userId).get();
		user.getOrders().add(bookId);
		return userRepository.save(user);
	}
	
	public void deleteAll() {
		userRepository.deleteAll();
	}
	
	public boolean delete(String id) {
		if(id == null | !userRepository.existsById(id)) return false;
		Optional<User> user = userRepository.findById(id);
		userRepository.delete(user.get());
		return true;
	}
}
