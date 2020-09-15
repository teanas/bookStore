package com.testbed.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * A service for the cart management.
 *
 */
@SpringBootApplication
@EnableRabbit
public class CartApplication {
	
	private static final String QUEUE_NAME1 = "cartQ";
	private static final String QUEUE_NAME2 = "catalogueQ1";
	private static final String QUEUE_NAME3 = "catalogueQ2";
	private static final String QUEUE_NAME12 = "cartUserQ";
	private static final String QUEUE_NAME13 = "cartOrderQ";
	private static Logger logger = LoggerFactory.getLogger(CartApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
		logger.info("Cart application started successfully");
	}

	@Bean
	public Queue cartQueue() {
	    return new Queue(QUEUE_NAME1, false);
	}
	@Bean
	public Queue cartUserQ() {
		return new Queue(QUEUE_NAME12,false);
	}
	@Bean
	public Queue cartOrderQ() {
		return new Queue(QUEUE_NAME13,false);
	}
	
	@Bean
	public Queue catalogueQueue() {
		return new Queue(QUEUE_NAME2, false);
	}
	
	@Bean
	public Queue catalogue2Queue() {
		return new Queue(QUEUE_NAME3,false);
	}
	
	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
