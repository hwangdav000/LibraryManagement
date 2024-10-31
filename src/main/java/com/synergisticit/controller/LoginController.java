package com.synergisticit.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
	
	@RequestMapping(value="login")
	public String login(@RequestParam(value="logout", required=false) String logout,
			@RequestParam(value="error", required=false) String error, 
			HttpServletRequest req, HttpServletResponse res,
			Model model
			
			) {
		StringBuilder message = new StringBuilder();
		
		if(logout != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth != null) {
				new SecurityContextLogoutHandler().logout(req, res, auth);
				message.append("You are logged out.");
			}
			
			return "redirect:home";
		}
		
		if(error != null) {
			message.append("Either username or password is incorrect..");
		}
		
		model.addAttribute("message", message);
		return "loginForm";
	}
	
	@RequestMapping(value="accessDenied")
	public String accessDenied() {
		return "accessDenied";
				
	}
	

}
