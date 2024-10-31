package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.Role;

public interface RoleService {
	Role saveRole(Role r);
	List<Role> getRoles();
	Role getRoleById(Long rId);
	Role updateRole(Role r);
	void deleteRole(Long rId);
	Role findByRoleName(String name);
	Long getMaxId();
}
