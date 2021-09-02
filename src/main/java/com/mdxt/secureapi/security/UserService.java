package com.mdxt.secureapi.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mdxt.secureapi.entity.Role;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.repository.RoleRepository;
import com.mdxt.secureapi.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("called loadUserByUsername with "+username);
		User user =  userRepository.findByEmail(username)
									.orElseThrow(() -> new UsernameNotFoundException("No user named '"+username+"' found"));
		return UserDetailsImpl.build(user);
	}

	
	
	//Uncomment bean to create a sample user of each category.
	//Will delete all existing users
	//@Bean
	private List<UserDetailsImpl> createSampleUsers() {
		List<UserDetailsImpl> result = new ArrayList<>();
		
		userRepository.deleteAll();
		roleRepository.deleteAll();
		
		User user = new User();
		user.setEmail("username@site.com");
		user.setPassword(AppSecurityConfig.passwordEncoder().encode("password"));
		Set<Role> userRoles = new HashSet<Role>();
		Role userRole = new Role();
		userRole.setName(RolesEnum.USER);
		userRoles.add(userRole);
		user.setRoles(userRoles);
		result.add(UserDetailsImpl.build(user));
		
		User underwriter_user = new User();
		underwriter_user.setEmail("underwriter@site.com");
		underwriter_user.setPassword(AppSecurityConfig.passwordEncoder().encode("underwriter"));
		Set<Role> underwriterRoles = new HashSet<Role>();
		Role underwriterRole = new Role();
		underwriterRole.setName(RolesEnum.UNDERWRITER);
		underwriterRoles.add(underwriterRole);
		underwriterRoles.add(userRole);
		underwriter_user.setRoles(underwriterRoles);
		result.add(UserDetailsImpl.build(underwriter_user));
		
		User admin_user = new User();
		admin_user.setEmail("admin@site.com");
		admin_user.setPassword(AppSecurityConfig.passwordEncoder().encode("admin"));
		Set<Role> adminRoles = new HashSet<Role>();
		Role adminRole = new Role();
		adminRole.setName(RolesEnum.ADMIN);
		adminRoles.add(adminRole);
		adminRoles.add(underwriterRole);
		adminRoles.add(userRole);
		admin_user.setRoles(adminRoles);
		result.add(UserDetailsImpl.build(admin_user));
		
		roleRepository.saveAndFlush(userRole);
		roleRepository.saveAndFlush(adminRole);
		roleRepository.saveAndFlush(underwriterRole);
		userRepository.saveAndFlush(user);
		userRepository.saveAndFlush(underwriter_user);
		userRepository.saveAndFlush(admin_user);
		
		return result;
	}
}
