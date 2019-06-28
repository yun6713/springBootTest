package com.bonc.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bonc.entity.Role;

public interface RoleRepository extends JpaRepository<Role,String> {
	
}
