package com.sigurtest.repositories;

import com.sigurtest.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    public List<Employee> findByFiredTimeIsNull();
}
