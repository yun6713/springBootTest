package com.bonc.entity.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.RowId;
import org.hibernate.annotations.Target;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
/**
 * 注解优先使用java.persistence包。<p>
 * 列注解：@Id、@Column、@Temporal；@ColumnTransformer、@Lob、@Formula<p>
 * 值填充：@GeneratedValue、@CreationTimestamp、@UpdateTimestamp<p>
 * generator：@SequenceGenerator、@TableGenerator、@GenericGenerator<p>
 * 参考：<a href="https://www.jianshu.com/p/d04fd3256e59">jpa主键生成策略</a><p>
 * 内嵌类：@Embeddable、@Embedded、@Target、@Parent<p>
 * 主键：@Id、@EmbeddedId+@Embeddable(主键类)、@RowId、@NaturalId<p>
 * 关联：@OneToOne、@OneToMany、@ManyToOne、@ManyToMany；双向时通过mappedBy将外键维护交由Many端。<p>
 * 关联关系：@JoinColumn、@JoinTable、@PrimaryKeyJoin、@NotFound<p>
 * 排序：@OrderColumn、@OrderBy<p>
 * 分组：@ElementCollection、@Embeddable<p>
 * 事件：@DomainEvents、@AfterDomainEventPublication、@TransactionalEventListener；监听事件，默认事务提交后处理，使用@EventListener会在接收到事件后立即处理<p>
 * <b>注意：</b><p>
 * 1,锁必须在事务中执行。<p>
 * 2,乐观锁，必须使用java.persistence.Version标记辅助字段<p>
 * @author Administrator
 *
 */
@Entity//jpa
@RedisHash("{user}")//Redis
@Table(name="user")
//@RowId(value = "rowId")//根据rowId查询记录，需数据库支持rowId；否则报错
@NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u")//命名查询
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id//spring-data id标记
	@javax.persistence.Id//hibernate id标记
	@Column(name="u_id")
	//从序列中获取主键，关闭druid wall过滤器。
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="sGenerator")
	@SequenceGenerator(name = "sGenerator",allocationSize=1)
	@Indexed//redis索引
	private Integer uId;
//	@JsonIgnore
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	@Temporal(TemporalType.TIMESTAMP)//标记数据库字段类型
	@CreationTimestamp
//	@Column(name="create_time",nullable=false,columnDefinition=" timestamp default CURRENT_TIMESTAMP")
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)//标记数据库字段类型
	@UpdateTimestamp
	private Date updateTime;
	//@ColumnTransformer//列转换，可定义读写时sql函数
	//标记只读属性，sql函数表达式；@ColumnTransformer简化版
	@Formula(value = "concat(username,'lalala')")
	private String test;
	@ManyToMany(fetch=FetchType.EAGER)
	//默认主键匹配
	@JoinTable(name="user_role",joinColumns= {@JoinColumn(name="u_id")},
	inverseJoinColumns= {@JoinColumn(name="r_id")})
	@NotFound(action=NotFoundAction.IGNORE)//未找到策略
//	@ElementCollection
	private Collection<Role> roles=new ArrayList<>();
	@Embedded
	@Target(value = Address.class)//多态时，标记实现类
	private Address addr;
	public Integer getuId() {
		return uId;
	}
	public void setuId(Integer uId) {
		this.uId = uId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	
	public Address getAddr() {
		return addr;
	}
	public void setAddr(Address addr) {
		this.addr = addr;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uId == null) ? 0 : uId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (uId == null) {
			if (other.getuId() != null)
				return false;
		} else if (!uId.equals(other.getuId()))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [uId=" + uId + ", username=" + username + ", password=" + password + "]";
	}
	
}
