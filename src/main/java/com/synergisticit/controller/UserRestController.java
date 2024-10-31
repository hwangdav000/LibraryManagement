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

import com.synergisticit.domain.User;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.UserValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("r/user/")
public class UserRestController {
	
	@Autowired UserValidator uValidator;

	@Autowired UserService uService;
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("saveUser")
	public ResponseEntity<?> saveUser(@Valid @RequestBody User r, BindingResult br) {
		uValidator.validate(r, br);
		
		if (br.hasErrors()) {
			
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<User>(uService.saveUser(r), HttpStatus.OK);
	}
	
	// read 
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("getUsers")
	public ResponseEntity<List<User>> getUsers() {
		return new ResponseEntity<List<User>>(uService.getUsers(), HttpStatus.OK);
	}
	
	// update
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("updateUser")
	public ResponseEntity<User> updateUser(@RequestBody User r) {
		if (uService.getUserById(r.getUserId()) == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<User>(uService.saveUser(r), HttpStatus.OK);
	} 
	
	// delete
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("deleteUser/{rId}")
	public void deleteUser(@PathVariable long rId) {
		uService.deleteUser(rId);
	}
}
