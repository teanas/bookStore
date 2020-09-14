package com.testbed.order.controller;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.testbed.order.service.OrderService;

/**
 * Class for consuming messages from the cartQ queue from a RabbitMQ service.
 *
 */
@Component
public class MessageConsumer {
	
	@Autowired
	private OrderService orderService;
	private final static Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    @Value("${VERSION}")
    private String version;
    
    /**
	 * Once a message in the cartQ queue is published, orders for the user and books are created.
	 * @param published message
	 */
    @RabbitListener(queues = "cartUserQ")
	public void createOrderFromMessage(Message message) {
		LOGGER.info("Service version: {}. Message \"submit order\" received.", version);
		
		String reply = new String (message.getBody(), UTF_8);
		int indexUserId = reply.indexOf(";");
		String userId = reply.substring(1, indexUserId);
		
		String bookIds = reply.substring(indexUserId+1, reply.length()-1);
		List<String> items = Arrays.asList(bookIds.split("\\s*,\\s*"));
		if(bookIds.isEmpty()) {
			LOGGER.info("Service version: {}. Method: createOrderFromMessage. No books have been ordered.", version);
			return;
		}
		for(String bookId : items) {
			orderService.create(userId, bookId);
			LOGGER.info("Service version: {}. Method: createOrderFromMessage. Create order book {} to user {}.", version, bookId, userId);
		}

	}
	

}
