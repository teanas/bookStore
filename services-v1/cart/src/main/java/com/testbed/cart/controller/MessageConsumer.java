package com.testbed.cart.controller;

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

import com.testbed.cart.model.Book;
import com.testbed.cart.repository.CartBookRepository;

/**
 * Class for consuming messages from the catalogueQ queue from a RabbitMQ service.
 *
 */
@Component
public class MessageConsumer {
	
	@Autowired
	private CartBookRepository bookRepository;
	private final static Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    @Value("${VERSION}")
    private String version;
    
    @RabbitListener(queues = "catalogueQ1")
   	public void saveBookId(Message message) {
    	LOGGER.info("Service version: {}. Message \"add new book\" received.", version);
    	String reply = new String (message.getBody(), UTF_8);
		List<String> items = Arrays.asList(reply.split("\\s*,\\s*"));
    	Book book = new Book();
    	book.setId(items.get(0));
    	book.setAvailable(Boolean.parseBoolean(items.get(1)));
    	bookRepository.save(book);
    	LOGGER.info("Service version: {}. Method: saveBookId. Book {} saved.", version, reply);
    }
    
    @RabbitListener(queues = "catalogueQ2")
    public void updateAvailability(Message message) {
    	LOGGER.info("Service version: {}. Message \"update availability\" received.", version);
    	String reply = new String (message.getBody(), UTF_8);
		List<String> items = Arrays.asList(reply.split("\\s*,\\s*"));
		if(items.get(0) == null) {
			LOGGER.info("Service version: {}. Method: updateAvailability. Invalid id.", version);
		} else if(bookRepository.findById(items.get(0)).isEmpty()) {
			LOGGER.info("Service version: {}. Method: updateAvailability. Book not found.", version);
		} else {
			Book book = bookRepository.findById(items.get(0)).get();
			book.setAvailable(Boolean.parseBoolean(items.get(1)));
			bookRepository.save(book);
			LOGGER.info("Service version: {}. Method: updateAvailability. Book {}, available: {}.", version, book.getId(), book.isAvailable());
		}
    	
    	
    }

}
