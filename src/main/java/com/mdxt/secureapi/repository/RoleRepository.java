package com.mdxt.secureapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdxt.secureapi.entity.Role;
import com.mdxt.secureapi.security.RolesEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByName(RolesEnum name);
}
