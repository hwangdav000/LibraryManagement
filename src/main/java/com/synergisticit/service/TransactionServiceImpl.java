package com.synergisticit.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Member;
import com.synergisticit.domain.Transaction;
import com.synergisticit.domain.TransactionType;
import com.synergisticit.domain.User;
import com.synergisticit.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired TransactionRepository tRepo;
	@Autowired UserService uService;
	@Autowired MemberService mService;
	
	@Override
	public Transaction borrowTransaction(Transaction t) {
		t.setTransactionType(TransactionType.BORROW);
		if (t.getTransactionDate() == null) t.setTransactionDate(LocalDate.now());
		return tRepo.save(t);
	}

	@Override
	public Transaction returnTransaction(Transaction t) {
		t.setTransactionType(TransactionType.RETURN);
		if (t.getTransactionDate() == null) t.setTransactionDate(LocalDate.now());
		
		return tRepo.save(t);
	}
	
	@Override
	public Transaction fineTransaction(Transaction t) {
		t.setTransactionType(TransactionType.PAYFINE);
		if (t.getTransactionDate() == null) t.setTransactionDate(LocalDate.now());
		
		// subtract from user find balance
		Long uId = t.getUserId();
		User u = uService.getUserById(uId);
		Member m = mService.findMemberByUser(u);
		if (m == null) return null;
		System.out.println(uId);
		
		Double fine = m.getFineBalance() - t.getAmount();
		if (fine < 0) fine = 0.0;
		
		m.setFineBalance(fine);
		
		mService.saveMember(m);
		
		return tRepo.save(t);
	}

	@Override
	public Transaction findTransactionById(long tId) {
		return tRepo.findById(tId).get();
	}

	@Override
	public List<Transaction> findAllTransactions() {

		return tRepo.findAll();
	}

	@Override
	public List<Transaction> findAllTransactionsByUserId(Long userId) {
		return tRepo.findByUserId(userId);
	}

	@Override
	public Transaction updateTransaction(Transaction t) {
		Optional<Transaction> transaction = tRepo.findById(t.getTransactionId());
		if (transaction.isPresent()) {
			return tRepo.save(t);
		}
		return null;
	}

	@Override
	public void deleteTransaction(Long tId) {
		tRepo.deleteById(tId);
	}

	@Override
	public Long getMaxId() {

		return tRepo.getMaxId();
	}

	// run once at midnight 
	@Override
	@Scheduled(cron="0 0 0 * * *")
	public void applyFines() {
		LocalDate now = LocalDate.now();
		
		List<Transaction> transactions = tRepo.findAll();
		for (Transaction t : transactions) {
			if (t.getTransactionType()==TransactionType.BORROW) {
				Long days = java.time.temporal.ChronoUnit.DAYS.between(t.getTransactionDate(), now);
				
				if (days > 30) {
					double fine = (days - 30) * 0.5; // 50 cents per late day
					
					// get member
					Long userId = t.getUserId();
					User u = uService.getUserById(userId);
					Member m = mService.findMemberByUser(u);
					
					// update fine
					double updatedFine = m.getFineBalance() + fine;
					m.setFineBalance(updatedFine);
					
					mService.saveMember(m);
				}
			}
		}
	}

}
