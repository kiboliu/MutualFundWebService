package com.DeltaMutualFund.DeltaMutualFund.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.DeltaMutualFund.DeltaMutualFund.domain.Employee;

public interface EmployeeRepository extends JpaRepository <Employee, String>{

    Employee findByUsername(String username);

}
