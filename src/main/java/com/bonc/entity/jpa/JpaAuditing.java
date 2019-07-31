package com.bonc.entity.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * jpa审计
 * @CreatedDate、@LastModifiedDate；时间
 * @CreatedBy、@LastModifiedBy；用户，需实现AuditorAware以提供用户信息
 * @author Administrator
 *
 */
@MappedSuperclass//审计信息基类，不对应表。
@EntityListeners(value = { AuditingEntityListener.class })//审计信息回调类
public class JpaAuditing implements Serializable{
	private static final long serialVersionUID = 1L;
	@CreatedBy
	protected String creater;
	@CreatedDate
	protected Date createDate;
	@LastModifiedBy
	protected String updater;
	@LastModifiedDate
	protected Date updateDate;
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
