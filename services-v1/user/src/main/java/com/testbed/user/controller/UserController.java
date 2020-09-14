package com.testbed.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.testbed.user.model.User;
import com.testbed.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Value("${VERSION}")
    private String version;
	
	@PostMapping("/addUser")
	public User create(@RequestBody User newUser) throws InterruptedException {
		if(newUser.getPassword() == null | newUser.getUserName() == null) {
			LOGGER.info("Service version: {}. Method: create. Invalid input.", version);
			return null;
		}
		User user = userService.create(newUser.getUserName(), newUser.getPassword());
		LOGGER.info("Service version: {}. Method: create. User {} created.", version, user.getId());
		return user;
	}
	
	@GetMapping("{id}")
	public User getUser(@PathVariable("id") String id) throws InterruptedException {
		User user = userService.getById(id);
		if(user == null) {
			LOGGER.info("Service version: {}. Method: getUser. User not found.");
			return null;
		} else {
			LOGGER.info("Service version: {}. Method: getUser. Found user {}.", version, user.getId());
			return user;
		}
	}
	
	@GetMapping("/getAll")
	public List<User> getAll() throws InterruptedException {
		List<User> users = userService.getAll();
		LOGGER.info("Service version: {}. Method: getAll. Got all users.", version);
		return users;
	}
	
	@PutMapping("/update")
	public User update(@RequestBody User newUser) throws InterruptedException {
		if(newUser.getId() == null) {
			LOGGER.info("Service version: {}. Method: update. Invalid id for the user.", version);
			return null;
		}
		User user = userService.update(newUser);
		if(user == null) LOGGER.info("Service version: {}. Method: update. User not found", version);
			else LOGGER.info("Service version: {}. Method: update. Updated user {}.", version, user.getId());
		return user;
	}
	
	@PutMapping("/addBook")
	public User addBook(@RequestParam String userId, @RequestParam String bookId) throws InterruptedException {
		User user = userService.addBook(userId, bookId);
		if(user == null) LOGGER.info("Service version: {}. Method: addBook. User not found.", version);
			else LOGGER.info("Service version: {}. Book {} added to user {}.", version, bookId, user.getId());
		return user;
	}
	
	@DeleteMapping("/delete")
	public String delete(@RequestParam String id) throws InterruptedException {
		if(!userService.delete(id)) {
			LOGGER.info("Service version: {}. Method: delete. Given user {} does not exist.", version, id);
			return "Invalid id";
		}
		LOGGER.info("Service version: {}. Method: delete. Deleted user {}.", version, id);
		return id + " was deleted.";
	}
	
	@DeleteMapping("/deleteAll")
	public String deleteAll() throws InterruptedException {
		userService.deleteAll();
		LOGGER.info("Service version: {}. Method: deleteAll. All users deleted.", version);
		return "Deleted all records.";
	}

}
