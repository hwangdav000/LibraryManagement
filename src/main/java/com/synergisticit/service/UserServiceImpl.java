package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;
import com.synergisticit.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired BCryptPasswordEncoder bCrypt;
	@Autowired UserRepository uRepo;
	
	@Override
	public User saveUser(User u) {
		u.setUserPassword(bCrypt.encode(u.getUserPassword()));
		return uRepo.save(u);
	}

	@Override
	public User getUserByUsername(String username) {
		
		return uRepo.findUserByUsername(username);
	}

	@Override
	public List<User> getUsers() {
		return uRepo.findAll();
	}

	@Override
	public User updateUser(User u) {
		
		Optional<User> user = uRepo.findById(u.getUserId());
		if (user.isEmpty()) {
			// user does not exist
			return null;
		}
		
		return uRepo.save(u);
	}

	@Override
	public void deleteUser(Long uId) {
		uRepo.deleteById(uId);
	}

	@Override
	public User getUserById(Long uId) {
		Optional<User> user = uRepo.findById(uId);
		if (user.isPresent()) return user.get();
		return null;
	}

	@Override
	public Long getMaxId() {
		
		return uRepo.getMaxId();
	}

	@Override
	public Boolean userIsRole(String username, String role) {
		User u = uRepo.findUserByUsername(username);
		
		for (Role r : u.getUserRoles()) {
			if (r.getRoleName().equals(role)) {
				return true;
			}
		}
		return false;
	}

}
