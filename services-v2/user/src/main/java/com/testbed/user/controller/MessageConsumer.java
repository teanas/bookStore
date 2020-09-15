package com.testbed.user.controller;

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

import com.testbed.user.service.UserService;

/**
 * Class for consuming messages from the cartQ queue from a RabbitMQ service.
 *
 */
@Component
public class MessageConsumer {
	
	@Autowired
	private UserService userService;
	private final static Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    @Value("${VERSION}")
    private String version;
    
	
	/**
	 * Once a message in the cartQ queue is published, books are added to the user (new user is created if none was found).
	 * @param published message
	 */
    @RabbitListener(queues = "cartUserQ")
	public void addBookFromMessage(Message message) {
		LOGGER.info("Service version: {}. Message \"submit order\" received.", version);
		
		String reply = new String (message.getBody(), UTF_8);
		int indexUserId = reply.indexOf(";");
		String userId = reply.substring(1, indexUserId);
		if(userId == null) return;
		if(userService.getById(userId) == null) userService.create(userId);
			
		if(reply.length() < indexUserId+1) {
			LOGGER.info("Service version: {}. Method: addBookFromMessage. Added user {} without ordered books.", version, userId);
		} else {
			String bookIds = reply.substring(indexUserId+1, reply.length()-1);
			List<String> items = Arrays.asList(bookIds.split("\\s*,\\s*"));

			for(String bookId : items) {
				userService.addBook(userId, bookId);
				LOGGER.info("Service version: {}. Method: addBookFromMessage. Book {} added to user {}.", version, bookId, userId);
			}
		}

		
	}
	

}
