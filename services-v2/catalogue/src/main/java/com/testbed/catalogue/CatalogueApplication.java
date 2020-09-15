package com.testbed.catalogue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableRabbit
@SpringBootApplication
public class CatalogueApplication {
	
	private static final String QUEUE_NAME1 = "cartQ";
	private static final String QUEUE_NAME2 = "catalogueQ1";
	private static final String QUEUE_NAME3 = "catalogueQ2";

	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}

	@Bean
	public Queue cartQueue() {
		return new Queue(QUEUE_NAME1, false);
	}
	
	@Bean
	public Queue catalogueQueue() {
		return new Queue(QUEUE_NAME2, false);
	}
	@Bean
	public Queue catalogue2Queue() {
		return new Queue(QUEUE_NAME3,false);
	}

}
