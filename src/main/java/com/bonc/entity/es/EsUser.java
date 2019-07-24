package com.bonc.entity.es;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.bonc.entity.jpa.Role;
import com.bonc.entity.jpa.User;

@Document(indexName = "jftest",type="user")
public class EsUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id//spring-data id标记
	private Integer uId;
	private String username;
	private String roles;
	
	public EsUser() {
		super();
	}
	public EsUser(User user) {
		this(user.getuId(),user.getUsername(),
				user.getRoles().stream().filter(Objects::nonNull).map(Role::getRoleName).collect(Collectors.joining(",")));
	}
	public EsUser(Integer uId, String username, String roles) {
		super();
		this.uId = uId;
		this.username = username;
		this.roles = roles;
	}
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

	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
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
