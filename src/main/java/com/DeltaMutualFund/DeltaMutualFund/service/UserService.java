package com.DeltaMutualFund.DeltaMutualFund.service;

import java.util.List;

import com.DeltaMutualFund.DeltaMutualFund.domain.User;

public interface UserService {

    User saveOrUpdate(User user);

    User registerUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    List<User> listAllUsers();
      
    User getUserByUsername(String username);

}
