package com.testbed.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbed.employee.model.Employee;
import com.testbed.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public Employee create(String name, String position) {
		return employeeRepository.save(new Employee(name, position));
	}

	public List<Employee> getAll(){
		return employeeRepository.findAll();
	}
	
	public Employee getById(String id) {
		if(id == null | employeeRepository.findById(id).isEmpty()) return null;
		return employeeRepository.findById(id).get();
	}
	
	public Employee update(String id, String name, String position) {
		if(employeeRepository.findById(id).isEmpty()) return null;
		Optional<Employee> employee = employeeRepository.findById(id);
		employee.get().setName(name);
		employee.get().setPosition(position);
		return employeeRepository.save(employee.get());
	}
	
	public void deleteAll() {
		employeeRepository.deleteAll();
	}
	
	public boolean delete(String id) {
		if(id == null | !employeeRepository.existsById(id)) return false;
		Optional<Employee> employee = employeeRepository.findById(id);
		employeeRepository.delete(employee.get());
		return true;
	}
}
