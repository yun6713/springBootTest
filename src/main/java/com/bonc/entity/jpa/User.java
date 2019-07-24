package com.bonc.entity.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@RedisHash("{user}")
@Table(name="user")
@NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id//spring-data id标记
	@javax.persistence.Id//hibernate id标记
	@Column(name="u_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Indexed//redis索引
	private Integer uId;
	@JsonIgnore
	private String username;
	private String password;
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="user_role",joinColumns= {@JoinColumn(name="u_id")},
	inverseJoinColumns= {@JoinColumn(name="r_id")})
	private Collection<Role> roles=new ArrayList<>();
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
	
}
