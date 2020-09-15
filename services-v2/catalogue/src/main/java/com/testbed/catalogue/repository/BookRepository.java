package com.testbed.catalogue.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.testbed.catalogue.model.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

	public Book findByName(String name);
	public Optional<Book> findById(String id);
	public List<Book> findByAvailable(boolean available);
	public List<Book> findByAuthor(String author);
}
