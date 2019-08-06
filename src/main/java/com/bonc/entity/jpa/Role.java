package com.bonc.entity.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;


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
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="role_permission",joinColumns= {@JoinColumn(name="r_id")},
			inverseJoinColumns= {@JoinColumn(name="p_id")})
	@NotFound(action=NotFoundAction.IGNORE)
	List<Permission> permissons=new ArrayList<>();
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
	
	public List<Permission> getPermissons() {
		return permissons;
	}
	public void setPermissons(List<Permission> permissons) {
		this.permissons = permissons;
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
	@DomainEvents//保存Role后发布事件,不等事务结束,在保存时的事务中；EventListener若非异步、新开事务，也在保存时事务中
	public List<RoleSaveEvent> domainEvents() throws IllegalArgumentException, IllegalAccessException, SecurityException{
		return Arrays.asList(new RoleSaveEvent(rId));
	}
	@AfterDomainEventPublication//@DomainEvents后立即执行，不等事务结束
	public void afterDomainEventPublication() {
		System.out.println("这玩意有什么用？");
	}
	public static class RoleSaveEvent{
		private Integer rId;
		public RoleSaveEvent(Integer rId) {
			this.rId=rId;
		}
		public RoleSaveEvent() {}
		public Integer getrId() {
			return rId;
		}
		public void setrId(Integer rId) {
			this.rId = rId;
		}
		
	}
	
}
