package com.bonc.repository.jpa;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.bonc.entity.jpa.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {

	Role findByRoleName(String roleName);
	@Lock(LockModeType.READ)//乐观锁，必须包含在事务中，实体类必须有java.persistence.Version标记的辅助字段
	Optional<Role> findByRId(Integer rId);
}
