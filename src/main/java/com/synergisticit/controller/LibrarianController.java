package com.synergisticit.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.synergisticit.domain.Librarian;
import com.synergisticit.domain.Member;
import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;
import com.synergisticit.service.LibrarianService;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.LibrarianValidator;

import jakarta.validation.Valid;

@Controller
public class LibrarianController {
	@Autowired LibrarianService lService;
	@Autowired UserService uService;
	@Autowired RoleService rService;
	
	@Autowired LibrarianValidator lValidator;
	
	@RequestMapping("librarianForm")
	public String librarianForm(Librarian librarian, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="librarianId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		
		librarian.setLibrarianId(lService.getMaxId()+1);
		
		getModel(mm, sortBy, page, size);
		
		return "librarianForm";
	}
	
	@RequestMapping("saveLibrarian")
	public String saveLibrarian(@Valid @ModelAttribute Librarian librarian, BindingResult br, ModelMap mm, 
			@RequestParam("username") String username,
	        @RequestParam("userPassword") String userPassword) {
		
		lValidator.validate(librarian, br);
		
	    if (isInvalid(username, userPassword, mm) || br.hasErrors()) {
	    	
	    	getModel(mm, "librarianId", 0, 4);
	    	return "librarianForm";
	    }
		
	    // Set the role
	    Role userRole = rService.findByRoleName("ADMIN");
	    List<Role> rList = new ArrayList<>();
	    rList.add(userRole);
	    
	    // Check if update
	    System.out.println("check " + librarian.getLibrarianId());
	    if (librarian.getLibrarianId() != null) {
		    Librarian updateLibrarian = lService.findLibrarianById(librarian.getLibrarianId());
		    if (updateLibrarian != null) {
		    	// update librarian
		    	User updateUser = updateLibrarian.getUser();
		    	updateUser.setUsername(username);
		    	updateUser.setUserPassword(userPassword);
		    	User u = uService.saveUser(updateUser);
		    	librarian.setUser(u);
		    	lService.saveLibrarian(librarian);
		    	
		    	return "redirect:librarianForm";	
		    }
	    }
	    
	    // New user
	    User u = new User();
	    u.setUsername(username);
	    u.setUserPassword(userPassword);  
	    u.setUserRoles(rList);
	    
	    // Save the User
	    User savedUser = uService.saveUser(u);
	    librarian.setUser(savedUser);
		    
	    lService.saveLibrarian(librarian);
	    
	    return "redirect:login";
	}
	
	private boolean isInvalid(String username, String userPassword, ModelMap mm) {
	    boolean hasErrors = false;

	    if (username == null || username.trim().isEmpty()) {
	        mm.addAttribute("errorUsername", "Please specify a username");
	        hasErrors = true;
	    }

	    if (userPassword == null || userPassword.trim().isEmpty()) {
	        mm.addAttribute("errorPassword", "Please specify a password");
	        hasErrors = true;
	    }

	    return hasErrors;
	}
	
	@RequestMapping("updateLibrarian")
	public String updateLibrarian(Librarian librarian, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="librarianId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		Librarian l = lService.findLibrarianById(librarian.getLibrarianId());
		
		mm.addAttribute("l", l);
		getModel(mm, sortBy, page, size);
		
		return "librarianForm";
	}
	
	@RequestMapping("deleteLibrarian")
	public String deleteLibrarian(Librarian librarian, ModelMap mm) {
		lService.deleteLibrarian(librarian.getLibrarianId());
		return "redirect:librarianForm";
	}
	
	public ModelMap getModel(ModelMap mm, String sortBy, int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Librarian> libraryPage = lService.findAllLibrarians(pageable);
		
		mm.addAttribute("libraryPage", libraryPage);
		mm.addAttribute("sortBy", sortBy);
		
		return mm;

	} 
}
