package com.testbed.order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbed.order.model.Order;
import com.testbed.order.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public Order create(String userId, String bookId) {
		Order order = new Order(userId, bookId, false);
		return orderRepository.save(order);
	}
	
	public List<Order> getAll(){
		return orderRepository.findAll();
	}
	
	public Order getById(String id) {
		if(id == null | orderRepository.findById(id).isEmpty()) return null;
		return orderRepository.findById(id).get();
	}
	
	public List<Order> getByUserId(String userId) {
		if(userId == null | orderRepository.findByUserId(userId).isEmpty()) return null;
		return orderRepository.findByUserId(userId);
	}
	
	public List<Order> getByBookId(String bookId) {
		if(bookId == null | orderRepository.findByBookId(bookId).isEmpty()) return null;
		return orderRepository.findByBookId(bookId);
	}
	
	public Order update(Order newOrder) {
		if(orderRepository.findById(newOrder.getId()).isEmpty()) return null;
		Order order = orderRepository.findById(newOrder.getId()).get();
		order.setUserId(newOrder.getUserId());
		order.setBookId(newOrder.getBookId());
		order.setClosed(newOrder.isClosed());
		return orderRepository.save(order);
	}
	
	public void deleteAll() {
		orderRepository.deleteAll();
	}
	
	public boolean delete(String id) {
		if(id == null | !orderRepository.existsById(id)) return false;
		Optional<Order> order = orderRepository.findById(id);
		orderRepository.delete(order.get());
		return true;
	}
	
}
