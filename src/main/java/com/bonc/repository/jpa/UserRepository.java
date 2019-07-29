package com.bonc.repository.jpa;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.entity.jpa.User;
import com.bonc.repository.jpa.integrate.UserMapper;
/**
 * 空值注解：@NonNull、@Nullable
 * @author litianlin
 * @date   2019年7月29日下午3:50:41
 * @Description TODO
 */
public interface UserRepository extends JpaRepository<User,Integer>,UserMapper {
	@Transactional
	@QueryHints(value={@QueryHint(name="n1",value="v1")})
	User findByUId(Integer id);
	User findByUsername(String username);
}
