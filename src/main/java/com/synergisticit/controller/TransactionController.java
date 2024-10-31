package com.synergisticit.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.synergisticit.domain.Book;
import com.synergisticit.domain.BookStatus;
import com.synergisticit.domain.Member;
import com.synergisticit.domain.Role;
import com.synergisticit.domain.Transaction;
import com.synergisticit.domain.User;
import com.synergisticit.service.BookService;
import com.synergisticit.service.MemberService;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.TransactionService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.TransactionValidator;

import jakarta.validation.Valid;

@Controller
public class TransactionController {
	@Autowired TransactionService tService;
	@Autowired UserService uService;
	@Autowired BookService bService;
	@Autowired MemberService mService;
	
	@Autowired TransactionValidator tValidator;
	
	@RequestMapping("transactionHistory")
	public String transactionHistory(ModelMap mm, Principal p) {
		
		List<Transaction> tList; 
		
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		
		if (uService.userIsRole(username, "ADMIN")) {
			tList = tService.findAllTransactions();
		} else {
			tList = tService.findAllTransactionsByUserId(u.getUserId());
		}
		
		List<String> bookStatusList = new ArrayList<>();
		
		// get bookstatus along with transaction history
		for (Transaction transaction : tList) {
			Long bookId = transaction.getBookId();
			if (bookId == null) {
				bookStatusList.add(null);
				continue;
			}
			Book book = bService.findBookById(bookId);
			String bookStatus = book.getStatus().name();
			bookStatusList.add(bookStatus);
		}
		
		mm.addAttribute("transactions", tList);
		mm.addAttribute("bookStatusList", bookStatusList);
		
		return "transactionHistory";
	}

	@RequestMapping("transactionForm")
	public String transactionForm(Transaction transaction, BindingResult br,
			ModelMap mm, Principal p,
			@RequestParam(value = "bkId", required = false) Long bkId,
			@RequestParam(value = "activeTab", required = false, defaultValue = "borrowTab") String activeTab) {
		getModel(mm);
		
		if (p == null) {
			return "redirect:login";
		}
		
		// make current date default for transaction
		LocalDate today = LocalDate.now();
        mm.addAttribute("transactionDate", today.toString()); // "YYYY-MM-DD" format
        
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		Member m = mService.findMemberByUser(u);
		
		mm.addAttribute("activeTab", activeTab);
		
		if (m != null) {
			double totalFine = m.getFineBalance();
			mm.addAttribute("totalFine", totalFine);
		}
		
		if (bkId != null && activeTab.equals("borrowTab")) mm.addAttribute("borrowId", bkId);
		if (bkId != null && activeTab.equals("returnTab")) mm.addAttribute("returnId", bkId);
		
		return "transactionForm";
	}
	
	@RequestMapping("borrowBook")
	public String borrowBook(@ModelAttribute @Valid Transaction transaction, BindingResult br, ModelMap mm, Principal p,
			@RequestParam(value = "activeTab", required = false, defaultValue = "borrowTab") String activeTab) {
		tValidator.validate(transaction, br);
		mm.addAttribute("activeTab", activeTab);
		if (br.hasErrors() || transaction.getBookId() == null) {
			getModel(mm);
			
			if (transaction.getBookId() == null) {
				mm.addAttribute("bookError", "please enter valid bookId");
			}
			
			return "transactionForm";
		}
		
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		
		transaction.setUserId(u.getUserId());
		
		// check if book is available 
		Book b = bService.findBookById(transaction.getBookId());
		if (!(b.getStatus() == BookStatus.AVAILABLE)) {
			mm.addAttribute("messageFail", "Transaction Failed");
			
			return "transactionForm";
		}
		
		tService.borrowTransaction(transaction);
		
		// need to set book status to borrowed
		b.setStatus(BookStatus.BORROWED);
		bService.saveBook(b);
		
		mm.addAttribute("message", "Transaction Completed");
		
		return "transactionForm";
	}
	
	@RequestMapping("returnBook")
	public String returnBook(@ModelAttribute @Valid Transaction transaction, BindingResult br, ModelMap mm, Principal p,
			@RequestParam(value = "activeTab", required = false, defaultValue = "returnTab") String activeTab) {
		tValidator.validate(transaction, br);
		mm.addAttribute("activeTab", activeTab);
		if (br.hasErrors() || transaction.getBookId() == null) {
			getModel(mm);
			
			if (transaction.getBookId() == null) {
				mm.addAttribute("bookError", "please enter valid bookId");
			}
			
			return "transactionForm";
		}
		
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		
		transaction.setUserId(u.getUserId());
		
		// check if book is borrowed 
		// need to check if user ID matches as well
		Book b = bService.findBookById(transaction.getBookId());
		if (!(b.getStatus() == BookStatus.BORROWED)) {
			mm.addAttribute("messageFail", "Transaction Failed");
			
			return "transactionForm";
		}
		
		tService.returnTransaction(transaction);
		
		// need to set book status to available
		b.setStatus(BookStatus.AVAILABLE);
		bService.saveBook(b);
		
		mm.addAttribute("message", "Transaction Completed");
		
		return "transactionForm";
	}
	
	@RequestMapping("payFine")
	public String payFine(@ModelAttribute @Valid Transaction transaction, BindingResult br, ModelMap mm, Principal p,
			@RequestParam(value = "activeTab", required = false, defaultValue = "payFineTab") String activeTab) {
		tValidator.validate(transaction, br);
		mm.addAttribute("activeTab", activeTab);
		
		if (br.hasErrors() || transaction.getAmount() == null || transaction.getAmount() <= 0.0) {
			
			if (transaction.getAmount() == null || transaction.getAmount() <= 0.0) {
				mm.addAttribute("amountError", "Please add valid amount");
			}
			
			
			getModel(mm);
			return "transactionForm";
		}
		
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		
		transaction.setUserId(u.getUserId());
		
		tService.fineTransaction(transaction);
		
		mm.addAttribute("message", "Transaction Completed");
		// get fine
		Member m = mService.findMemberByUser(u);
		
		if (m == null) {
			mm.addAttribute("errorFine", "N/A");
			return "transactionForm";
		}
		double totalFine = m.getFineBalance();
		mm.addAttribute("totalFine", totalFine);
		
		
		return "redirect:transactionForm";
	}
	 
	public ModelMap getModel(ModelMap mm) {
		return mm;
	}
}
