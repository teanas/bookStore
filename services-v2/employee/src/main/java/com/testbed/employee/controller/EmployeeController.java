package com.testbed.employee.controller;

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

import com.testbed.employee.model.Employee;
import com.testbed.employee.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    @Value("${VERSION}")
    private String version;
	
	@PostMapping("/addEmployee")
	public Employee create(@RequestParam String name, @RequestParam String position) throws InterruptedException {
		Thread.sleep(500);
		if(name == null | position == null) {
			LOGGER.info("Service version: {}. Method: create. Invalid input.", version);
			return null;
		}
		Employee employee = employeeService.create(name, position);
		LOGGER.info("Service version: {}. Method: create. Employee {} created.", version, employee.getId());
		return employee;
	}
	
	@GetMapping("{id}")
	public Employee getEmployee(@PathVariable("id") String id) throws InterruptedException {
		Thread.sleep(500);
		Employee employee = employeeService.getById(id);
		if(employee == null) LOGGER.info("Service version: {}. Method: getEmployee. Employee not found.", version); 
			else LOGGER.info("Service version: {}. Method: getEmployee. Found employee {}.", version, employee.getId());
		return employee;
	}
	
	@GetMapping("/getAll")
	public List<Employee> getAll() throws InterruptedException {
		Thread.sleep(500);
		List<Employee> employees = employeeService.getAll();
		LOGGER.info("Service version: {}. Method: getAll. Got all employees.", version);
		return employees;
	}
	
	@PutMapping("/update")
	public Employee update(@RequestBody Employee newEmployee) throws InterruptedException {
		Thread.sleep(500);
		if(newEmployee.getId() == null) {
			LOGGER.info("Service version: {}. Method: update. Invalid id for the employee.", version);
			return null;
		}
		Employee employee = employeeService.update(newEmployee.getId(), newEmployee.getName(), newEmployee.getId());
		if(employee == null) LOGGER.info("Service version: {}. Method: update. Employee not found.", version); 
			else LOGGER.info("Service version: {}. Method: update. Updated employee {}.", version, employee.getId());
		return employee;
	}
	
	@DeleteMapping("/delete")
	public String delete(@RequestParam String id) throws InterruptedException {
		Thread.sleep(500);
		if(!employeeService.delete(id)) {
			LOGGER.info("Service version: {}. Method: delete. Given employee {} does not exist.", version, id);
			return "Invalid id";
		}
		LOGGER.info("Service version: {}. Method: delete. Deleted employee {}.", version, id);
		employeeService.delete(id);
		return id + " was deleted.";
	}
	
	@DeleteMapping("/deleteAll")
	public String deleteAll() throws InterruptedException {
		Thread.sleep(500);
		employeeService.deleteAll();
		LOGGER.info("Service version: {}. Method: deleteAll. All employees deleted.", version);
		return "Deleted all records.";
	}

}
