package com.synergisticit.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.synergisticit.validation.RoleValidator;
import com.synergisticit.validation.TransactionValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("r/transaction/")
public class TransactionRestController {
	@Autowired TransactionValidator tValidator;

	@Autowired TransactionService tService;
	@Autowired UserService uService;
	@Autowired BookService bService;
	@Autowired MemberService mService;
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@PostMapping("borrowBook")
	public ResponseEntity<?> borrowBook(@Valid @RequestBody Transaction t, BindingResult br, Principal p) {
		tValidator.validate(t,  br);
		
		if (br.hasErrors()) {
			
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		
		t.setUserId(u.getUserId());
		
		// check if book is available 
		if (t.getBookId() == null) return new ResponseEntity<>("Please provide BookId", HttpStatus.BAD_REQUEST);
		Book b = bService.findBookById(t.getBookId());
		if (!(b.getStatus() == BookStatus.AVAILABLE)) {
			return new ResponseEntity<>("Book is already borrowed", HttpStatus.BAD_REQUEST);
		}
		
		tService.borrowTransaction(t);
		
		// need to set book status to borrowed
		b.setStatus(BookStatus.BORROWED);
		Book savedBook = bService.saveBook(b);
		
		return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
	}
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@PostMapping("returnBook")
	public ResponseEntity<?> returnBook(@Valid @RequestBody Transaction t, BindingResult br, Principal p) {
		tValidator.validate(t,  br);
		
		if (br.hasErrors()) {
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		
		t.setUserId(u.getUserId());
		
		// check if book is borrowed 
		// need to check if user ID matches as well
		if (t.getBookId() == null) return new ResponseEntity<>("Please provide BookId", HttpStatus.BAD_REQUEST);
		Book b = bService.findBookById(t.getBookId());
		if (!(b.getStatus() == BookStatus.BORROWED)) {
			return new ResponseEntity<>("Book is not in borrowed state", HttpStatus.BAD_REQUEST);
		}
		
		tService.returnTransaction(t);
		
		// need to set book status to available
		b.setStatus(BookStatus.AVAILABLE);
		Book savedBook = bService.saveBook(b);
		
		return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
	}
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@PostMapping("payFine")
	public ResponseEntity<?> payFine(@Valid @RequestBody Transaction transaction, BindingResult br, Principal p) {
		
		tValidator.validate(transaction,  br);
		
		
		if (br.hasErrors() || transaction.getAmount() == null || transaction.getAmount() <= 0.0) {
			
			if (transaction.getAmount() == null || transaction.getAmount() <= 0.0) {
				return new ResponseEntity<>("Please provide valid amount", HttpStatus.BAD_REQUEST);
			}
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		String username = p.getName();
		User u = uService.getUserByUsername(username);
		
		transaction.setUserId(u.getUserId());
		
		Transaction t = tService.fineTransaction(transaction);
		if (t == null) return new ResponseEntity<>("user does not have membership", HttpStatus.BAD_REQUEST);
		
		// get fine
		Member m = mService.findMemberByUser(u);
		
		double totalFine = m.getFineBalance();
		String message = "Amount owed now is " + totalFine;
		
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	
}
