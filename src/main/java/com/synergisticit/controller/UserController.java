package com.synergisticit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synergisticit.domain.User;
import com.synergisticit.repository.UserRepository;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;

@Controller
public class UserController {
	
	@Autowired UserService uService;
	@Autowired RoleService rService;
	
	@RequestMapping("userForm")
	public String userForm(User user, ModelMap mm) {
		user.setUserId(uService.getMaxId()+1);
		getModel(mm);
		return "userForm";
	}
	
	@RequestMapping("saveUser")
	public String saveUser(User user, ModelMap mm) {
		uService.saveUser(user);
		return "redirect:userForm";
	}
	
	@RequestMapping("updateUser")
	public String updateUser(User user, ModelMap mm) {
		User u = uService.getUserById(user.getUserId());
		
		mm.addAttribute("u", u);
		mm.addAttribute("retrievedRoles", u.getUserRoles());
		getModel(mm);
		
		return "userForm";
	}
	
	@RequestMapping("deleteUser")
	public String deleteUser(User user, ModelMap mm) {
		uService.deleteUser(user.getUserId());
		return "redirect:userForm";
	}
	
	public ModelMap getModel(ModelMap mm) {
		mm.addAttribute("roles", rService.getRoles());
		mm.addAttribute("users", uService.getUsers());
		return mm;
	}
	
	
	
}
