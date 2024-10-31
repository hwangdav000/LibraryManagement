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
import com.synergisticit.service.BookService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.BookValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("r/book/")
public class BookRestController {

	@Autowired BookService bService;
	@Autowired UserService uService;
	
	@Autowired BookValidator bValidator;
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("saveBook")
	public ResponseEntity<?> saveBook(@Valid @RequestBody Book b, BindingResult br) {
		bValidator.validate(b, br);
		
		if (br.hasErrors()) {
			
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Book>(bService.saveBook(b), HttpStatus.OK);
	}
	
	// read 
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("getBook")
	public ResponseEntity<List<Book>> getBooks() {
		return new ResponseEntity<List<Book>>(bService.findAllBooks(), HttpStatus.OK);
	}
	
	// update
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("updateBook")
	public ResponseEntity<Book> updateBook(@RequestBody Book b) {
		if (bService.findBookById(b.getBookId()) == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Book>(bService.saveBook(b), HttpStatus.OK);
	} 
	
	// delete
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("deleteBook/{bId}")
	public void deleteBook(@PathVariable long bId) {
		bService.deleteBook(bId);
	}
}
