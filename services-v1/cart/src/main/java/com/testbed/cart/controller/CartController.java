package com.testbed.cart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.testbed.cart.model.Cart;
import com.testbed.cart.service.CartService;


@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	private final static Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    @Value("${VERSION}")
    private String version;
	
	@PostMapping("/addCart")
	public Cart create() throws InterruptedException {
		Cart cart = cartService.addCart();
		LOGGER.info("Service version: {}. Method: create. A cart {} is created.", version, cart.getUserId());
		return cart;
	}
	
	@PutMapping("/{id}/addBook")
	public Cart addToCart(@RequestParam String bookId, @PathVariable("id") String userId) throws InterruptedException {
		Cart added = cartService.addToCart(bookId, userId);
		if(added != null) {
			LOGGER.info("Service version: {}. Method: addToCart. Book {} added to cart {}.", version, bookId, added.getUserId());
			return added;
		} else {
			LOGGER.info("Service version: {}. Method: addToCart. Adding to cart failed.", version);
			return added;
		}
	}
	
	@PutMapping("/{id}/deleteBook")
	public Cart removeFromCart(@RequestParam String bookId, @PathVariable("id") String userId) throws InterruptedException {
		Cart removed = cartService.removeFromCart(bookId, userId);
		if(removed != null)	{		
			LOGGER.info("Service version: {}. Method: removeFromCart. Book {} removed from the cart {}.", version, bookId, removed.getUserId());
			return removed;
		} else {
			LOGGER.info("Service version: {}. Method: removeFromCart. Removing from cart failed.", version);
			return removed;
		}

	}
	
	@PutMapping("/{id}/emptyCart")
	public Cart emptyCart(@PathVariable("id") String userId) throws InterruptedException {
		Cart cart = cartService.emptyCart(userId);
		LOGGER.info("Service version: {}. Method: emptyCart. Removed all books from the cart {}.", version, userId);
		return cart;
	}
	
	
	@GetMapping("{id}")
	public Cart getCart(@PathVariable("id") String id) throws InterruptedException {
		Cart cart = cartService.getById(id);
		if(cart == null) LOGGER.info("Service version: {}. Method: getCart. Cart not found.", version);
			else LOGGER.info("Service version: {}. Method: getCart. Found cart {}.", version, cart.getUserId());
		return cart;
	}
	
	
	@GetMapping("/getAll")
	public List<Cart> getAll() throws InterruptedException {
		List<Cart> carts = cartService.getAll();
		LOGGER.info("Service version: {}. Method: getAll. Got all carts.", version);
		return carts;
	}
	
	@DeleteMapping("/{id}/delete")
	public String delete(@RequestParam String id) throws InterruptedException {
		if(!cartService.delete(id)) {
			LOGGER.info("Service version: {}. Method: delete. Given cart {} does not exist.", version, id);
			return "Invalid id.";
		}
		LOGGER.info("Service version: {}. Method: delete. Deleted cart {}.", version, id);
		return id + " was deleted.";
	}
	
	@DeleteMapping("/deleteAllCarts")
	public String deleteAll() throws InterruptedException {
		cartService.deleteAll(); 
		LOGGER.info("Service version: {}. Method: deleteAll. All carts deleted.", version);
		return "Deleted all records.";
	}

}
