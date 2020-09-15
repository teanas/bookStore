package com.testbed.order.controller;

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

import com.testbed.order.model.Order;
import com.testbed.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    @Value("${VERSION}")
    private String version;
	
	@PostMapping("/addOrder")
	public Order create(@RequestParam String userId, @RequestParam String bookId) throws InterruptedException {
		Thread.sleep(500);
		if(userId == null | bookId == null) {
			LOGGER.info("Service version: {}. Method: create. Invalid input.", version);
			return null;
		}
		Order order = orderService.create(userId, bookId);
		LOGGER.info("Service version: {}. Method: create. Order {} created.", version, order.getId());
		return order;
	}
	
	@GetMapping("/{id}")
	public Order getOrder(@PathVariable("id") String id) throws InterruptedException {
		Thread.sleep(500);
		Order order = orderService.getById(id);
		if (order == null) LOGGER.info("Service version: {}. Order not found.", version);
			else LOGGER.info("Service version: {}. Method: getOrder. Found order {}.", version, order.getId());
		return order;
	}
	
	@GetMapping("/getAll")
	public List<Order> getAll() throws InterruptedException {
		Thread.sleep(500);
		List<Order> orders = orderService.getAll();
		LOGGER.info("Service version: {}. Method: getAll. Got all orders.", version);
		return orders;
	}
	
	@GetMapping("/getByUserId")
	public List<Order> getByUserId(@RequestParam String userId) throws InterruptedException {
		Thread.sleep(500);
		List<Order> orders = orderService.getByUserId(userId);
		if (orders == null) LOGGER.info("Service version: {}.  Method: getByUserId. Orders not found.", version);
			else LOGGER.info("Service version: {}. Method: getByUserId. Got all orders for user {}.", version, userId);
		return orders;
	}
	
	@GetMapping("/getByBookId")
	public List<Order> getByBookId(@RequestParam String bookId) throws InterruptedException {
		Thread.sleep(500);
		List<Order> orders = orderService.getByBookId(bookId);
		if (orders == null) LOGGER.info("Service version: {}. Method: getByBookId. Orders not found.", version);
			else LOGGER.info("Service version: {}. Method: getByBookId. Got all orders for book {}.", version, bookId);
		return orders;
	}
	
	@PutMapping("/update")
	public Order update(@RequestBody Order newOrder) throws InterruptedException {
		Thread.sleep(500);
		if(newOrder.getId() == null) {
			LOGGER.info("Service version: {}. Method: update. Invalid id for the order.", version);
			return null;
		}
		Order order = orderService.update(newOrder);
		if (order == null) LOGGER.info("Service version: {}. Method: update. Order not found.", version);
			else LOGGER.info("Service version: {}. Method: update. Updated order {}.", version, order.getId());
		return order;
	}
	
	@DeleteMapping("/delete")
	public String deleteOrder(@RequestParam String id) throws InterruptedException {
		Thread.sleep(500);
		if(!orderService.delete(id)) {
			LOGGER.info("Service version: {}. Method: delete. Given order {} does not exist.", version, id);
			return "Invalid id";
		}
		LOGGER.info("Service version: {}. Method: delete. Deleted order {}.", version, id);
		return id + " was deleted.";
	}
	
	@DeleteMapping("/deleteAll")
	public String deleteAll() throws InterruptedException {
		Thread.sleep(500);
		orderService.deleteAll();
		LOGGER.info("Service version: {}. Method: deleteAll. All orders deleted.", version);
		return "Deleted all records.";
	}

}
