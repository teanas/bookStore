package com.testbed.cart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testbed.cart.model.Cart;
import com.testbed.cart.service.CartService;

/**
 * Class for publishing messages to the cartQ queue from a RabbitMQ service.
 *
 */
@RestController
@RequestMapping("/cart/{id}")
public class MessagePublisher {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	private CartService cartService;
	private final static Logger LOGGER = LoggerFactory.getLogger(MessagePublisher.class);
    @Value("${VERSION}")
    private String version;
    
    private static final String QUEUE_NAME = "cartQ";
	private static final String QUEUE_NAME12 = "cartUserQ";
	private static final String QUEUE_NAME13 = "cartOrderQ";
	
    /**
     * Publishes a message containing a userId and ordered bookIds
     * @param userId
     * @return success if published
     */
	@PostMapping("/submitOrder")
	public String sendMessage(@PathVariable("id") String userId) {
		
		Cart cart = cartService.submitOrder(userId);
		if(cart == null) {
			LOGGER.info("Service version: {}. Method: sendMessage. Message \"submit order\" for the user {} was not sent.", version, userId);
			return "Cart not found.";
		}
		String message = cart.getUserId() + ";";
		String books = "";
		for(String book: cart.getBookIds()) {
			books = book + "," + books;
		}
		message = message + books;
		rabbitTemplate.convertAndSend(QUEUE_NAME, message);
		rabbitTemplate.convertAndSend(QUEUE_NAME12, message);
		rabbitTemplate.convertAndSend(QUEUE_NAME13, message);
		LOGGER.info("Service version: {}. Method: sendMessage. Message \"submit order\" for the user {} sent to RabbitMQ.", version, userId);
		return "Message sent.";
		
	}
}
