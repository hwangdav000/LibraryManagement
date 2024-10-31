package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;

public interface UserService {
	User saveUser(User u);
	User getUserByUsername(String username);
	User getUserById(Long uId);
	List<User> getUsers();
	User updateUser(User u);
	void deleteUser(Long uId);
	Long getMaxId();
	Boolean userIsRole(String username, String role);
}
