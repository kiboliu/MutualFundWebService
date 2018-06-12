package com.DeltaMutualFund.DeltaMutualFund.service;

import javax.transaction.Transactional;

import com.DeltaMutualFund.DeltaMutualFund.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.DeltaMutualFund.DeltaMutualFund.domain.Employee;
import com.DeltaMutualFund.DeltaMutualFund.repository.EmployeeRepository;

public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {
    @Autowired
    private EmployeeRepository EmployeeRepository;
    @Autowired
    private UserRepository userRepository;
    
    /* (non-Javadoc)
     * @see com.waylau.spring.boot.blog.service.EmployeeService#saveOrUpateEmployee(com.waylau.spring.boot.blog.domain.Employee)
     */
    @Transactional
    @Override
    public Employee saveOrUpateEmployee(Employee Employee) {
        return EmployeeRepository.save(Employee);
    }

    /* (non-Javadoc)
     * @see com.waylau.spring.boot.blog.service.EmployeeService#registerEmployee(com.waylau.spring.boot.blog.domain.Employee)
     */
    @Transactional
    @Override
    public Employee registerEmployee(Employee Employee) {
        return EmployeeRepository.save(Employee);
    }

    /* (non-Javadoc)
     * @see com.waylau.spring.boot.blog.service.EmployeeService#removeEmployee(java.lang.Long)
     */
    @Transactional
    @Override
    public void removeEmployee(String username) {
        EmployeeRepository.delete(username);
    }

    /* (non-Javadoc)
     * @see com.waylau.spring.boot.blog.service.EmployeeService#getEmployeeById(java.lang.Long)
     */
    @Override
    public Employee getEmployeeById(String username) {
        return EmployeeRepository.findOne(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = EmployeeRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByUsername(username);
        }
        if (user == null) {
            throw new  UsernameNotFoundException("Not found");
        }
        return user;
    }

}
