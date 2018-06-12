package com.DeltaMutualFund.DeltaMutualFund.repository;

import com.DeltaMutualFund.DeltaMutualFund.domain.Funddetails;
import com.DeltaMutualFund.DeltaMutualFund.domain.Fundidentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunddetailsRepository extends JpaRepository <Funddetails, Fundidentity>{
    List<Funddetails> findByFundIdentityFundId(Long fundId);
}
