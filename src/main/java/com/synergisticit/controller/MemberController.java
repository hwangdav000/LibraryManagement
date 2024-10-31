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

import com.synergisticit.domain.Member;
import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;
import com.synergisticit.service.MemberService;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.MemberValidator;

import jakarta.validation.Valid;

@Controller
public class MemberController {
	
	@Autowired MemberService mService;
	@Autowired UserService uService;
	@Autowired RoleService rService;
	
	@Autowired MemberValidator mValidator;
	
	@RequestMapping("memberForm")
	public String memberForm(Member member, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="memberId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		
		member.setMemberId(mService.getMaxId()+1);
		mm.addAttribute("members", mService.findAllMembers());
		
		if ((p != null) && (uService.userIsRole(p.getName(), "USER"))) {
			User u = uService.getUserByUsername(p.getName());
			Member m = mService.findMemberByUser(u);
			return "redirect:updateMember?memberId=" + m.getMemberId().toString();
		} else if ((p != null) && (uService.userIsRole(p.getName(), "ADMIN"))) {
			getModel(mm, sortBy, page, size, null);
		}
		
		return "memberForm";
	}
	
	@RequestMapping("saveMember")
	public String saveMember(@Valid @ModelAttribute Member member, BindingResult br,
			ModelMap mm, 
			@RequestParam("username") String username,
	        @RequestParam("userPassword") String userPassword,
	        Principal p) {
	    mValidator.validate(member, br);
	    System.out.println("username" + username);
	    if (isInvalid(username,  userPassword, mm) || br.hasErrors()) {
	    	if (p != null) {
	    		getModel(mm, "memberId", 0, 4, p.getName());
	    	}
	    	
	    	return "memberForm";
	    }
		
	    // Set the role
	    Role userRole = rService.findByRoleName("USER");
	    List<Role> rList = new ArrayList<>();
	    rList.add(userRole);
	    
	    // Check if update
	    Member updateMember = mService.findMemberById(member.getMemberId());
	    if (updateMember != null) {
	    	// update user
	    	User updateUser = updateMember.getUser();
	    	updateUser.setUsername(username);
	    	updateUser.setUserPassword(userPassword);
	    	uService.saveUser(updateUser); 
	    } else {
		    // New user
		    User u = new User();
		    u.setUsername(username);
		    u.setUserPassword(userPassword);  
		    u.setUserRoles(rList);
		    
		    // Save the User
		    User savedUser = uService.saveUser(u);
		    member.setUser(savedUser);
	    }
	    
	    mService.saveMember(member);
	    
	    return "redirect:memberForm";
	}
	
	private boolean isInvalid(String username, String userPassword, ModelMap mm) {
	    boolean hasErrors = false;

	    if (username == null || username.trim().isEmpty()) {
	        mm.addAttribute("errorUsername", "Please specify a username");
	        System.out.println("CHECK USERNAME");
	        hasErrors = true;
	    }

	    if (userPassword == null || userPassword.trim().isEmpty()) {
	        mm.addAttribute("errorPassword", "Please specify a password");
	        hasErrors = true;
	    }

	    return hasErrors;
	}
	
	@RequestMapping("updateMember")
	public String updateMember(Member member, ModelMap mm, Principal p,
			@RequestParam(name="sortBy", required=false, defaultValue="memberId") String sortBy,
	        @RequestParam(name="page", required=false, defaultValue="0") int page,
	        @RequestParam(name="size", required=false, defaultValue="4") int size) {
		Member m = mService.findMemberById(member.getMemberId());
		
		mm.addAttribute("m", m);
		mm.addAttribute("members", mService.findAllMembers());
		
		if ((p != null) && (uService.userIsRole(p.getName(), "USER"))) {
			getModel(mm, sortBy, page, size, p.getName());
		} else if ((p != null) && (uService.userIsRole(p.getName(), "ADMIN"))) {
			getModel(mm, sortBy, page, size, null);
		}
		
		return "memberForm";
	}
	
	@RequestMapping("deleteMember")
	public String deleteMember(Member member, ModelMap mm) {
		mService.deleteMember(member.getMemberId());
		return "redirect:login";
	}
	
	public ModelMap getModel(ModelMap mm, String sortBy, int page, int size, String username) {
	
		if (username == null) {
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
			Page<Member> memberPage = mService.findAllMembers(pageable);
			
			mm.addAttribute("memberPage", memberPage);
			mm.addAttribute("sortBy", sortBy);
			return mm;
		} else {
			User u = uService.getUserByUsername(username);
			Member m = mService.findMemberByUser(u);
			
			mm.addAttribute("md", m);
			
			return mm;
		}

	} 
	
	
	
}
