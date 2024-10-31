package com.synergisticit.service;

import org.springframework.stereotype.Service;

import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;

import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired UserService uService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = uService.getUserByUsername(username);
		List<Role> roles = user.getUserRoles();
		
		Collection<GrantedAuthority> authorities = new HashSet<>();
		
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getUserPassword(), authorities);
	}

}
