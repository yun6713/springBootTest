package com.bonc.repository.jpa;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.entity.jpa.User;
import com.bonc.repository.jpa.integrate.UserMapper;
/**
 * 空值注解：@NonNull、@Nullable
 * 前缀：find...By、count...By(数量)；方法名第一个By为分隔符
	前缀关键词：Distinct、First[num]、Top[num]
	逻辑：And、Or
	关系表达式，列名[关键词]，关键词为空则为等于：
	LessThan、GreaterThan；大小，
	LessThanEqual、GreaterThanEqual；
	Not；不等于，findBySexNot
	Is、Equals；等于
	After、Before；时间
	IsNull、NotNull；空、非空
	Like、NotLike；字符串
	StartingWith、EndingWith、Containing；字符串，自动拼接%
	IgnoreCase、AllIgnoreCase；不区分大小写
	In、NotIn、Between；
	TRUE、FALSE；为true、false；findByIsSuccessTrue
	OrderBy；排序，默认Asc
	参数：Sort排序、Pageable分页排序；位于形参最后
	分类，group by；已通过@ElementCollection、@Embeddable实现。
 * @author litianlin
 * @date   2019年7月29日下午3:50:41
 * @Description TODO
 */
public interface UserRepository extends JpaRepository<User,Integer>,UserMapper {
	//锁模式，加锁必须在事务中执行。乐观锁必须有比对字段？
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	//设置锁超时时间
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	User findByUId(Integer id);
	User findByUsername(@NonNull String username);
}
