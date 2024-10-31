package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Role;
import com.synergisticit.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired RoleRepository rRepo;
	
	@Override
	public Role saveRole(Role r) {
		
		return rRepo.save(r);
	}

	@Override
	public List<Role> getRoles() {
		
		return rRepo.findAll();
	}

	@Override
	public Role updateRole(Role r) {
		Optional<Role> role = rRepo.findById(r.getRoleId());
		if (role.isPresent()) {
			return rRepo.save(r);
		}
		return null;
	}

	@Override
	public void deleteRole(Long rId) {
		rRepo.deleteById(rId);		
	}

	@Override
	public Role getRoleById(Long rId) {
		Optional<Role> role = rRepo.findById(rId);
		if (role.isPresent()) return role.get();
		return null;
	}

	@Override
	public Role findByRoleName(String name) {
		return rRepo.findByRoleName(name);
	}

	@Override
	public Long getMaxId() {
		
		return rRepo.getMaxId();
	}
	
	

}
