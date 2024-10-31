package com.synergisticit.controller;

import java.security.Principal;
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

import com.synergisticit.domain.Book;
import com.synergisticit.domain.Member;
import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;
import com.synergisticit.service.MemberService;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.MemberValidator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("r/member/")
public class MemberRestController {

	@Autowired MemberService mService;
	
	@Autowired MemberValidator mValidator;
	@Autowired UserService uService;
	
	@Autowired RoleService rService;
	
	// create 
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	@PostMapping("createMember")
	public ResponseEntity<?> saveMember(@Valid @RequestBody Member member, BindingResult br) {
		mValidator.validate(member, br);
		
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
	    
	    if (member.getUsername() == null || member.getPassword() == null) return new ResponseEntity<>("Missing username/password", HttpStatus.BAD_REQUEST);
	    
	    // Check if update
	    Member updateMember = mService.findMemberById(member.getMemberId());
	    if (updateMember != null) {
	    	// update user
	    	User updateUser = updateMember.getUser();
	    	updateUser.setUsername(member.getUsername());
	    	updateUser.setUserPassword(member.getPassword());
	    	uService.saveUser(updateUser); 
	    } else {
		    // New user
		    User u = new User();
		    u.setUsername(member.getUsername());
		    u.setUserPassword(member.getUsername());  
		    u.setUserRoles(rList);
		    
		    // Save the User
		    User savedUser = uService.saveUser(u);
		    member.setUser(savedUser);
	    }
	    
	    Member savedMember = mService.saveMember(member);
		
		return new ResponseEntity<Member>(savedMember, HttpStatus.OK);
	}
	
	// read 
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@GetMapping("getMember")
	public ResponseEntity<List<Member>> getMembers(Principal p) {
		// for user   
		String username = p.getName();
		
		if (username != null) {
			User u = uService.getUserByUsername(username);
			List<Member> mList = new ArrayList<>();
			Member m = mService.findMemberByUser(u);
			mList.add(m);
			
			return new ResponseEntity<List<Member>>(mList, HttpStatus.OK);
		}
		
		return new ResponseEntity<List<Member>>(mService.findAllMembers(), HttpStatus.OK);
	}
	
	// update
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@PutMapping("updateMember")
	public ResponseEntity<Member> updateMember(@RequestBody Member m) {
		if (mService.findMemberById(m.getMemberId()) == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Member>(mService.saveMember(m), HttpStatus.OK);
	}
	
	// delete
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("deleteMember/{mId}")
	public void deleteMember(@PathVariable long mId) {
		mService.deleteMember(mId);
	}
}
