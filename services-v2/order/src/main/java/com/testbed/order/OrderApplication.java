package com.testbed.order;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableRabbit
@SpringBootApplication
public class OrderApplication {
	
	private static final String QUEUE_NAME = "cartOrderQ";

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

	@Bean
	  public Queue cartOrderQ() {
	    return new Queue(QUEUE_NAME, false);
	  }
}
