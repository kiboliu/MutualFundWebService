package com.DeltaMutualFund.DeltaMutualFund.repository;

import com.DeltaMutualFund.DeltaMutualFund.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository <Transaction, Long>{
    List<Transaction> findTransactionsByCusid(Long id);
    List<Transaction> findTransactionsByStatus(String status);
}
