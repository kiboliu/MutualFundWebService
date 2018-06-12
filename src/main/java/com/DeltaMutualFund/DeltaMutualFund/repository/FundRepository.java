package com.DeltaMutualFund.DeltaMutualFund.repository;

import com.DeltaMutualFund.DeltaMutualFund.domain.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundRepository extends JpaRepository<Fund, Long> {
    Fund findByName(String name);
}
