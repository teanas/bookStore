package com.testbed.cart.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.testbed.cart.model.Cart;


@Repository
public interface CartRepository extends MongoRepository<Cart, String> {


	public Optional<Cart> findByUserId(String userId);
}
