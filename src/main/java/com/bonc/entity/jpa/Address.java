package com.bonc.entity.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Parent;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 复用，或构建多列id。
 * @Parent引用父级。
 * @author litianlin
 * @date   2019年7月29日下午1:22:34
 * @Description TODO
 */
@Embeddable
public class Address implements Serializable{
	private static final long serialVersionUID = 1L;
	//	@Id
//	@GeneratedValue(generator="uuid")
//	@GenericGenerator(name="uuid",strategy="uuid")
//	private String id;
	private String addr;
	private String zipCode;
	@Parent//引用父级
	@JsonIgnore//必须忽略，否则json序列化循环引用，报错
	private transient User user;//循环引用，增加transient修饰
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
