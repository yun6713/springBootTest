package com.bonc.entity.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * 乐观锁，必须使用java.persistence.Version标记辅助字段<p>
 * @author litianlin
 * @date   2019年7月31日上午10:46:22
 * @Description TODO
 */
@Entity
@Table(name="role")
public class Role extends JpaAuditing implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="role_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer rId;
	@Column(name="role_name")
	private String roleName;
	@Version//乐观锁，必须使用java.persistence.Version标记辅助字段
	private Integer version;
	public Integer getrId() {
		return rId;
	}
	public void setrId(Integer rId) {
		this.rId = rId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String role) {
		this.roleName = role;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rId == null) ? 0 : rId.hashCode());
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
		Role other = (Role) obj;
		if (rId == null) {
			if (other.rId != null)
				return false;
		} else if (!rId.equals(other.rId))
			return false;
		return true;
	}
	
	
}
