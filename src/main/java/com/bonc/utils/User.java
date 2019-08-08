package com.bonc.utils;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
//建议重写hashCode，equals方法
@Entity
@Table(name="user")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name="PASSWORD")//
private String password;
@Column(name="ZIP_CODE")//
private String zipCode;
@Column(name="USERNAME")//
private String username;
@Temporal((TemporalType.TIMESTAMP))
@Column(name="CREATE_TIME")//
private Date createTime;
@Temporal((TemporalType.TIMESTAMP))
@Column(name="UPDATE_TIME")//
private Date updateTime;
@Id
//@GeneratedValue(strategy=GenerationType.SEQUENCE)
@Column(name="U_ID")//
private Integer uId;
@Column(name="ADDR")//
private String addr;
   public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
   public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
   public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
   public Integer getUId() {
		return uId;
	}
	public void setUId(Integer uId) {
		this.uId = uId;
	}
   public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}

    }
