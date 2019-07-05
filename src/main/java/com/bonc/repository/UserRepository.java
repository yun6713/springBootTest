package com.bonc.repository;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.entity.User;
public interface UserRepository extends JpaRepository<User,Integer> {
	@Transactional
	@QueryHints(value={@QueryHint(name="n1",value="v1")})
	User findByUId(Integer id);
}
