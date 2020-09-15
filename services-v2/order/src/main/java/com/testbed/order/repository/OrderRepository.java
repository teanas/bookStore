package com.testbed.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.testbed.order.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

	public Optional<Order> findById(String id);
	public List<Order> findByUserId(String userId);
	public List<Order> findByBookId(String bookId);
}
