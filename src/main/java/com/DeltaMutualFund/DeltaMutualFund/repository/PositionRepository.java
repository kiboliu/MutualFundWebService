package com.DeltaMutualFund.DeltaMutualFund.repository;

import com.DeltaMutualFund.DeltaMutualFund.domain.Position;
import com.DeltaMutualFund.DeltaMutualFund.domain.Positionidentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository <Position, Positionidentity>{
    List<Position> findByPositionIdentityCusId(Long cusId);
}
