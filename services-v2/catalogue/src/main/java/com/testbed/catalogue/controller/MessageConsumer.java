package com.testbed.catalogue.controller;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.testbed.catalogue.model.Book;
import com.testbed.catalogue.service.CatalogueService;

/**
 * Class for consuming messages from the cartQ queue from a RabbitMQ service.
 *
 */
@Component
public class MessageConsumer {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	private CatalogueService catalogueService;
	private final static Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    @Value("${VERSION}")
    private String version;
    
	private static final String QUEUE_NAME3 = "catalogueQ2";
    

    @RabbitListener(queues = "cartQ")
	public void decreaseQuantity(Message message) {
		LOGGER.info("Service version: {}. Message \"submit order\" received.", version);
		
		String reply = new String (message.getBody(), UTF_8);
		int indexUserId = reply.indexOf(";");
		if(reply.length() < indexUserId+1) {
				LOGGER.info("Service version: {}. Method: decreaseQuantity. No books have been ordered.", version);
				return;
		} else {
			String bookIds = reply.substring(indexUserId+1, reply.length()-1);
			List<String> items = Arrays.asList(bookIds.split("\\s*,\\s*"));
			for(String bookId : items) {
				if(catalogueService.getById(bookId) == null | bookId.isEmpty()) {
					LOGGER.info("Service version: " + version + ". Method: decreaseQuantity. Book not found.");
				} else {
					Book book = catalogueService.getById(bookId);
					int decrease = book.getQuantity() - 1;
					book.setQuantity(decrease);
					if(book.isAvailable() != catalogueService.getById(bookId).isAvailable()) {
						String messageUpd = book.getId() + "," + book.isAvailable();
						rabbitTemplate.convertAndSend(QUEUE_NAME3, messageUpd);
					}
					LOGGER.info("Service version: " + version + ". Method: decreaseQuantity. Book " + book.getId() + ": " + book.getQuantity() + " left.");
				}
			}
		}
	}
	

}
