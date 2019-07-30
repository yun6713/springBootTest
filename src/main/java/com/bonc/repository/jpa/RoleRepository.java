package com.bonc.repository.jpa;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.bonc.entity.jpa.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {

	Role findByRoleName(String roleName);
	//无法重写继承的方法
	@Lock(LockModeType.READ)
	Optional<Role> findByRId(Integer rId);
}
