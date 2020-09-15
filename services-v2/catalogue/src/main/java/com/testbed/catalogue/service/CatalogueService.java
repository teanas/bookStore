package com.testbed.catalogue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbed.catalogue.model.Book;
import com.testbed.catalogue.repository.BookRepository;

@Service
public class CatalogueService {
	
	@Autowired
	private BookRepository bookRepository;
	
	public Book create(String name, String author, String genre, int quantity) {
		return bookRepository.save(new Book(name, author, genre, quantity));
	}
	
	public List<Book> getAll(){
		return bookRepository.findAll();
	}
	
	public Book getById(String id) {
		if(id == null | bookRepository.findById(id).isEmpty()) return null;
		return bookRepository.findById(id).get();
	}
	
	public List<Book> getByAvailable(boolean available){
		return bookRepository.findByAvailable(available);
	}
	
	public Book update(String id, String name, String author, String genre, int quantity) {
		if(bookRepository.findById(id).isEmpty()) return null;
		Book book = bookRepository.findById(id).get();
		book.setAuthor(author);
		book.setQuantity(quantity);
		book.setGenre(genre);
		return bookRepository.save(book);
	}
	
	public void deleteAll() {
		bookRepository.deleteAll();
	}
	
	public boolean delete(String id) {
		if(id == null | !bookRepository.existsById(id)) return false;
		Optional<Book> book = bookRepository.findById(id);
		bookRepository.delete(book.get());
		return true;
	}
	
}
