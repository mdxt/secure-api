package com.mdxt.secureapi.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mdxt.secureapi.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails{
	//TODO: update this class to use User class
	private final List<? extends GrantedAuthority> grantedAuthorities;
	private final String username;
	private final String password;
	private final boolean isAccountNonExpired;
	private final boolean isAccountNonLocked;
	private final boolean isCredentialsNonExpired;
	private final boolean isEnabled;
	
	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles()
												.stream()
												.map(role -> role.getGrantedAuthorities().get(0)) 
													//index 0 was used assuming each role like USER has equivalent granted authority ROLE_USER
												.collect(Collectors.toList());
		return new UserDetailsImpl(authorities, user.getEmail(), user.getPassword(), true, true, true, true);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}

}
