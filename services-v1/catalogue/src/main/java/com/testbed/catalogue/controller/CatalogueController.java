package com.testbed.catalogue.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.testbed.catalogue.model.Book;
import com.testbed.catalogue.service.CatalogueService;

// path /catalog
@RestController
@RequestMapping("/catalogue")
public class CatalogueController {
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	private CatalogueService catalogueService;
	private final static Logger LOGGER = LoggerFactory.getLogger(CatalogueController.class);
    @Value("${VERSION}")
    private String version;
    
    private static final String QUEUE_NAME = "catalogueQ1";
	
	
	@PostMapping("/addBook")
	public Book create(@RequestBody Book newBook) throws InterruptedException {
		if(newBook.getName()==null | newBook.getAuthor() == null| newBook.getGenre() == null) {
			LOGGER.info("Service version: {}. Method: create. Invalid input.", version);
			return null;
		}
		Book book = catalogueService.create(newBook.getName(), newBook.getAuthor(), newBook.getGenre(), newBook.getQuantity());
		LOGGER.info("Service version: {}. Method: create. A new book {} added to the catalogue.", version, book.getId());
		String message = book.getId() + "," + book.isAvailable();
		rabbitTemplate.convertAndSend(QUEUE_NAME, message);
		return book;
	}
	
	
	@GetMapping("{id}")
	public Book getBook(@PathVariable("id") String id) throws InterruptedException {
		if(catalogueService.getById(id) == null) {
			LOGGER.info("Service version: {}. Method: getBook. Book {} not found.", version, id);
		}
		LOGGER.info("Service version: {}. Method: getBook. Found book {}.", version, id); 
		return catalogueService.getById(id);
	}
	
	@GetMapping("/getAll")
	public List<Book> getAll() throws InterruptedException {
		List<Book> catalogue = catalogueService.getAll();
		LOGGER.info("Service version: {}. Method: getAll. Got the catalogue.", version);
		return catalogue;
	}
	
	@GetMapping("/getAllAvailable")
	public List<Book> getAllAvailable() throws InterruptedException {
		List<Book> allAvailable = catalogueService.getByAvailable(true);
		LOGGER.info("Service version: {}. Method: getAllAvailable. Got all available.", version);
		return allAvailable;
	}
	
	
	@PutMapping("/update")
	public Book update(@RequestBody Book newBook) throws InterruptedException {
		if(newBook.getId() == null) {
			LOGGER.info("Service version: {}. Method: update. Invalid id for the book.", version);
			return null;
		}
		Book book = catalogueService.update(newBook.getId(), newBook.getName(), newBook.getAuthor(), newBook.getGenre(), newBook.getQuantity());
		if(book == null) LOGGER.info("Service version: {}. Method: update. Book not found.", version); 
			else LOGGER.info("Service version: {}. Method: update. Updated book {}.", version, book.getId());
		return book;
	}
	
	@DeleteMapping("/delete")
	public String deleteBook(@RequestParam String id) throws InterruptedException {
		if(!catalogueService.delete(id)) {
			LOGGER.info("Service version: {}. Method: delete. Given book {} does not exist.", version, id);
		    return "Invalid id.";
		};
		LOGGER.info("Service version: {}. Method: delete. Deleted book {}.", version, id);
		return id + " was deleted.";
	}
	
	@DeleteMapping("/deleteAll")
	public String deleteAll() throws InterruptedException {
		catalogueService.deleteAll();
		LOGGER.info("Service version: {}. Method: deleteAll. All books in the catalogue deleted.", version);
		return "Deleted all records.";
	}

}
