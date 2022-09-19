package com.sigurtest.repositories;

import com.sigurtest.model.Department;
import org.springframework.data.repository.CrudRepository;;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
}
