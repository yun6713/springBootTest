package com.bonc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Response entity")
public class Response<T> {
	@ApiModelProperty(value="状态",example="200")
	private int status;
	@ApiModelProperty(value="泛型返回对象",example="Hello world")
	private T entity;
	@ApiModelProperty(value="返回信息",example="OK")
	private String msg;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
