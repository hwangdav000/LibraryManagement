package com.synergisticit.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.synergisticit.domain.Librarian;
import com.synergisticit.domain.Member;
import com.synergisticit.domain.Role;
import com.synergisticit.domain.User;
import com.synergisticit.repository.RoleRepository;
import com.synergisticit.repository.UserRepository;
import com.synergisticit.service.LibrarianService;
import com.synergisticit.service.MemberService;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;

@Component
public class AppCommandLineRunner implements CommandLineRunner {
	@Autowired UserRepository uRepo;
	@Autowired RoleRepository rRepo;
	
	@Autowired UserService uService;
	@Autowired RoleService rService;
	
	@Autowired MemberService mService;
	@Autowired LibrarianService lService;
	
	@Override
	public void run(String... args) throws Exception {
		if (uService.getUsers().size() == 0) {
			Role admin = new Role(1L, "ADMIN", new ArrayList<>());
			Role user = new Role(2L, "USER", new ArrayList<>());
			
			List<Role> rList = new ArrayList<>();
			rList.add(admin);
			
			User testAdmin = new User(1L, "admin", "password", rList, null, null);
			
			List<Role> uList = new ArrayList<>();
			uList.add(user);
			
			User testUser = new User(2L, "user", "password", uList, null, null);
			
			// save roles
			rService.saveRole(admin);
			rService.saveRole(user);
			
			// save users
			User sAdmin = uService.saveUser(testAdmin);
			User sUser = uService.saveUser(testUser);

			// save user as member
			Member m = new Member(null, "MEMBER", "MEMBER", "address1", "address2", 
					"1112223333", 0.0, "member@gmail.com", new Date(), null, null, null);
			m.setUser(sUser);
			mService.saveMember(m);
			
			// save admin as librarian
			Librarian l = new Librarian(null, "ADMIN", "ADMIN", "1112223333", "admin@gmail.com", null, null, null);
			l.setUser(sAdmin);
			lService.saveLibrarian(l);
			
		}
		
	}

}
