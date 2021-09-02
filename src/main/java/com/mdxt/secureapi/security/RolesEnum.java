package com.mdxt.secureapi.security;
import java.util.List;
import java.util.ArrayList;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum RolesEnum {
	ADMIN,
	UNDERWRITER,
	USER;
	
	private final List<SimpleGrantedAuthority> granted;

	private RolesEnum() {
		SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_"+toString());
		this.granted = new ArrayList<SimpleGrantedAuthority>();
		this.granted.add(sga);
		System.out.println("added granted authority "+sga.getAuthority()+" to RoleEnum "+toString());
	}

	public List<SimpleGrantedAuthority> getGrantedAuthorities() {
		return granted;
	}
	
}
