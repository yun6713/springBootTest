package com.bonc.repository.jpa;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bonc.entity.jpa.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {

	Role findByRoleName(String roleName);
	
}
