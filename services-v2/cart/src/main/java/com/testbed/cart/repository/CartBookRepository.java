package com.testbed.cart.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.testbed.cart.model.Book;

@Repository
public interface CartBookRepository extends MongoRepository<Book, String>{

}
