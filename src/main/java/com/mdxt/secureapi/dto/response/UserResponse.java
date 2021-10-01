package com.mdxt.secureapi.dto.response;

import java.util.HashSet;
import java.util.Set;

import com.mdxt.secureapi.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
	private String email;
	private Set<Role> roles;
}
