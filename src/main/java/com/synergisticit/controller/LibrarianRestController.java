package com.synergisticit.controller;

import java.util.ArrayList;
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

import com.synergisticit.domain.Librarian;
import com.synergisticit.domain.Member;
import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;
import com.synergisticit.service.LibrarianService;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.LibrarianValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("r/librarian/")
public class LibrarianRestController {

	@Autowired LibrarianService lService;
	
	@Autowired LibrarianValidator lValidator;
	@Autowired RoleService rService;
	@Autowired UserService uService;
	
	// create
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("createLibrarian")
	public ResponseEntity<?> saveLibrarian(@Valid @RequestBody Librarian librarian, BindingResult br) {
		lValidator.validate(librarian, br);
		
		if (br.hasErrors()) {
			
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		// Set the role
	    Role userRole = rService.findByRoleName("USER");
	    List<Role> rList = new ArrayList<>();
	    rList.add(userRole);
	    
	    if (librarian.getUsername() == null || librarian.getPassword() == null) return new ResponseEntity<>("Missing username/password", HttpStatus.BAD_REQUEST);
	    
	    // Check if update
	    Librarian updateMember = lService.findLibrarianById(librarian.getLibrarianId());
	    if (updateMember != null) {
	    	// update user
	    	User updateUser = updateMember.getUser();
	    	updateUser.setUsername(librarian.getUsername());
	    	updateUser.setUserPassword(librarian.getPassword());
	    	uService.saveUser(updateUser); 
	    } else {
		    // New user
		    User u = new User();
		    u.setUsername(librarian.getUsername());
		    u.setUserPassword(librarian.getUsername());  
		    u.setUserRoles(rList);
		    
		    // Save the User
		    User savedUser = uService.saveUser(u);
		    librarian.setUser(savedUser);
	    }
	    
	    Librarian savedLibrarian = lService.saveLibrarian(librarian);
		
		return new ResponseEntity<Librarian>(savedLibrarian, HttpStatus.OK);
	}
	
	// read
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("getLibrarian")
	public ResponseEntity<List<Librarian>> getLibrarian() {
		return new ResponseEntity<List<Librarian>>(lService.findAllLibrarians(), HttpStatus.OK);
	}
	
	// update
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("updateLibrarian")
	public ResponseEntity<Librarian> updateLibrarian(@RequestBody Librarian l) {
		if (lService.findLibrarianById(l.getLibrarianId()) == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Librarian>(lService.saveLibrarian(l), HttpStatus.OK);
		
	} 
	
	// delete
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("deleteLibrary/{lId}")
	public void deleteLibrary(@PathVariable long lId) {
		lService.deleteLibrarian(lId);
	}
	
}
