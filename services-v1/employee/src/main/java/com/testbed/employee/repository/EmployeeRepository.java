package com.testbed.employee.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.testbed.employee.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String>{

	public Optional<Employee> findById(String id);
}
