package com.DeltaMutualFund.DeltaMutualFund.service;

import com.DeltaMutualFund.DeltaMutualFund.domain.Employee;

public interface EmployeeService{

    Employee saveOrUpateEmployee(Employee Employee);

    Employee registerEmployee(Employee Employee);

    void removeEmployee(String username);

    Employee getEmployeeById(String username);


}
