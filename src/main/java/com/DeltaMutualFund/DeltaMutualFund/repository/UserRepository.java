package com.DeltaMutualFund.DeltaMutualFund.repository;

import com.DeltaMutualFund.DeltaMutualFund.domain.User;
        import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}