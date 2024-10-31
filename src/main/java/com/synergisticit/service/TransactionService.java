package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.Transaction;

public interface TransactionService {
	Transaction borrowTransaction(Transaction t);
	Transaction returnTransaction(Transaction t);
	Transaction fineTransaction(Transaction t);
	Transaction findTransactionById(long tId);
	List<Transaction> findAllTransactions();
	List<Transaction> findAllTransactionsByUserId(Long userId);
	Transaction updateTransaction(Transaction t);
	void deleteTransaction(Long tId);
	void applyFines();
	Long getMaxId();
}
