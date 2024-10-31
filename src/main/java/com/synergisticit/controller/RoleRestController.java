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


import com.synergisticit.domain.Role;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.RoleValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("r/role/")
public class RoleRestController {
	
	@Autowired RoleValidator rValidator;

	@Autowired RoleService rService;
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("saveRole")
	public ResponseEntity<?> saveRole(@Valid @RequestBody Role r, BindingResult br) {
		rValidator.validate(r, br);
		
		if (br.hasErrors()) {
			
			Map<String, String> fieldErrors = br.getFieldErrors().stream()
			        .collect(Collectors.toMap(
			            FieldError::getField,  
			            FieldError::getDefaultMessage
			        ));
			return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Role>(rService.saveRole(r), HttpStatus.OK);
	}
	
	// read 
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("getRoles")
	public ResponseEntity<List<Role>> getRole() {
		return new ResponseEntity<List<Role>>(rService.getRoles(), HttpStatus.OK);
	}
	
	// update
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("updateRole")
	public ResponseEntity<Role> updateRole(@RequestBody Role r) {
		if (rService.getRoleById(r.getRoleId()) == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Role>(rService.saveRole(r), HttpStatus.OK);
	} 
	
	// delete
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("deleteRole/{rId}")
	public void deleteRole(@PathVariable long rId) {
		rService.deleteRole(rId);
	}
}
