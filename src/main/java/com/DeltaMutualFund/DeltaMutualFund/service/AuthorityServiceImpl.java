package com.DeltaMutualFund.DeltaMutualFund.service;

import com.DeltaMutualFund.DeltaMutualFund.domain.Authority;
import com.DeltaMutualFund.DeltaMutualFund.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}
