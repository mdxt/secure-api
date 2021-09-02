package com.mdxt.secureapi.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mdxt.secureapi.entity.User;

@Service
public class UserService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("called loadUserByUsername with "+username);
//		getUsers()
//		.stream()
//		.forEach(appUser -> System.out.println(appUser.getUsername()));
		return getUsers()
				.stream()
				.filter(appUser -> appUser.getUsername().equals(username))
				.findFirst()
				.orElseThrow(() -> new UsernameNotFoundException(username+" not found"));
	}

	private List<UserDetailsImpl> getUsers() {
		List<UserDetailsImpl> result = new ArrayList<>();
		UserDetailsImpl testUser =	new UserDetailsImpl(RolesEnum.USER.getGrantedAuthorities(),
										"username",
										AppSecurityConfig.passwordEncoder().encode("password"), 
										true, true, true, true);
		UserDetailsImpl testAdminUser =	new UserDetailsImpl(RolesEnum.ADMIN.getGrantedAuthorities(),
										"admin",
										AppSecurityConfig.passwordEncoder().encode("admin"), 
										true, true, true, true);		
		result.add(testUser);
		result.add(testAdminUser);
		return result;

	}
}
