package com.synergisticit.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synergisticit.domain.Role;
import com.synergisticit.service.RoleService;

@Controller
public class RoleController {

	@Autowired RoleService rService;
	
	
	@RequestMapping("roleForm")
	public String roleForm(Role role, ModelMap mm, Principal p) {
		role.setRoleId(rService.getMaxId()+1);
		getModel(mm);
		return "roleForm";
	}
	
	@RequestMapping("saveRole")
	public String saveRole(Role user, ModelMap mm) {
		rService.saveRole(user);
		return "redirect:roleForm";
	}
	
	@RequestMapping("updateRole")
	public String updateRole(Role user, ModelMap mm) {
		Role role = rService.getRoleById(user.getRoleId());
		mm.addAttribute("r", role);
		
		getModel(mm);
		return "roleForm";
	}
	
	@RequestMapping("deleteRole")
	public String deleteRole(Role user, ModelMap mm) {
		rService.deleteRole(user.getRoleId());
		
		return "redirect:roleForm";
	}
	
	public ModelMap getModel(ModelMap mm) {
		mm.addAttribute("roles", rService.getRoles());		
		return mm;
	}
	
}
