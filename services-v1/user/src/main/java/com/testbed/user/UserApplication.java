package com.testbed.user;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableRabbit
@SpringBootApplication
public class UserApplication {

	private static final String QUEUE_NAME = "cartUserQ";
		
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	  public Queue cartUserQ() {
	    return new Queue(QUEUE_NAME, false);
	  }

}
