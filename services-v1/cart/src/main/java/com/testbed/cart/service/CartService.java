package com.testbed.cart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbed.cart.model.Cart;
import com.testbed.cart.repository.CartBookRepository;
import com.testbed.cart.repository.CartRepository;


@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartBookRepository bookRepository;
	
	public Cart addCart() {
		Cart cart = new Cart();
		return cartRepository.save(cart);
	}
	
	public Cart addToCart(String bookId, String userId) {
		if(bookId == null | bookRepository.findById(bookId).isEmpty() | !bookRepository.findById(bookId).get().isAvailable()) return null;
		if(userId == null | cartRepository.findByUserId(userId).isEmpty()) return null;
		
		Cart cart = cartRepository.findByUserId(userId).get();
		cart.getBookIds().add(bookId);
		return cartRepository.save(cart);
	}
	
	public Cart removeFromCart(String bookId, String userId) {
		if(userId == null | cartRepository.findByUserId(userId).isEmpty()) return null;
		Cart cart = cartRepository.findByUserId(userId).get();
		cart.getBookIds().remove(bookId);
		return cartRepository.save(cart);
	}
	
	public Cart emptyCart(String userId) {
		if(userId == null | cartRepository.findByUserId(userId).isEmpty()) return null;
		Cart cart = cartRepository.findByUserId(userId).get();
		cart.setBookIds(new ArrayList<String>());
		return cartRepository.save(cart);
	}
	
	public List<Cart> getAll(){
		return cartRepository.findAll();
	}
	
	public Cart getById(String userId) {
		if(userId == null | cartRepository.findByUserId(userId).isEmpty()) return null;
		return cartRepository.findByUserId(userId).get();
	}
	
	public Cart submitOrder(String userId) {
		if(userId == null | cartRepository.findByUserId(userId).isEmpty()) return null;
		return cartRepository.findByUserId(userId).get();
	}
	
	public void deleteAll() {
		cartRepository.deleteAll();
	}
	
	public boolean delete(String id) {
		if(id == null | !cartRepository.existsById(id)) return false;
		Optional<Cart> cart = cartRepository.findById(id);
		cartRepository.delete(cart.get());
		return true;
	}
}
