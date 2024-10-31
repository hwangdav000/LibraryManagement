package com.synergisticit.controller;

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
import com.synergisticit.domain.Publisher;
import com.synergisticit.service.PublisherService;
import com.synergisticit.validation.BookValidator;
import com.synergisticit.validation.PublisherValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("r/publisher/")
public class PublisherRestController {
	@Autowired PublisherService pService;
	
	@Autowired PublisherValidator pValidator;
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("savePublisher")
	public ResponseEntity<?> savePublisher(@Valid @RequestBody Publisher p, BindingResult br) {
		pValidator.validate(p, br);
		
		if (br.hasErrors()) {
			
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Publisher>(pService.savePublisher(p), HttpStatus.OK);
	}
	
	// read 
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@GetMapping("getPublisher")
	public ResponseEntity<List<Publisher>> getPublishers() {
		return new ResponseEntity<List<Publisher>>(pService.findAllPublishers(), HttpStatus.OK);
	}
	
	// update
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("updatePublisher")
	public ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher p) {
		if (pService.findPublisherById(p.getPublisherId()) == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Publisher>(pService.savePublisher(p), HttpStatus.OK);
	} 
	
	// delete
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("deletePublisher/{bId}")
	public void deletePublisher(@PathVariable long pId) {
		pService.deletePublisher(pId);
	}
	
	
}
