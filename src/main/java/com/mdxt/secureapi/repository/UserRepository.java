package com.mdxt.secureapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdxt.secureapi.entity.Role;
import com.mdxt.secureapi.entity.User;
import com.mdxt.secureapi.security.RolesEnum;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	
	Boolean existsByEmail(String email);
	
	List<User> findByRolesContaining(Role role);
	
	List<User> findByEmailContaining(String email);
}
