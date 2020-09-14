package com.testbed.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.testbed.user.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public Optional<User> findById(String id);
}
