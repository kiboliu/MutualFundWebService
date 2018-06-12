package com.DeltaMutualFund.DeltaMutualFund.service;


import com.DeltaMutualFund.DeltaMutualFund.domain.User;
import com.DeltaMutualFund.DeltaMutualFund.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.DeltaMutualFund.DeltaMutualFund.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository EmployeeRepository;

    @Override
    @Transactional
    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }


    @Override
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
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
