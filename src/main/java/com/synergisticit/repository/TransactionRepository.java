package com.synergisticit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.synergisticit.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@Query("SELECT COALESCE(MAX(t.transactionId), 0) FROM Transaction t")
	Long getMaxId();
	
	List<Transaction> findByUserId(Long userId);
}
