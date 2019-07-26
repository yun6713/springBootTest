package com.bonc.utils;

import java.util.function.Function;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class FactoryBeanWrapper<T> implements FactoryBean<T>,InitializingBean,DisposableBean{
	private T t;
	private Function<T,Void> init;
	private Function<T,Void> destroy;
	
	public FactoryBeanWrapper(T t, Function<T,Void> init, Function<T,Void> destroy) {
		super();
		this.t = t;
		this.init = init;
		this.destroy = destroy;
	}

	private FactoryBeanWrapper() {
	}
	public static <T> Builder<T> builder(T t){
		return new Builder<T>().value(t);
	}
	@Override
	public T getObject() throws Exception {
		// TODO Auto-generated method stub
		return t;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return t.getClass();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(init!=null)
			init.apply(t);
	}

	@Override
	public void destroy() throws Exception {
		if(destroy!=null)
			destroy.apply(t);
	}
	public static class Builder<T>{
		private FactoryBeanWrapper<T> f = new FactoryBeanWrapper<T>();
		public Builder<T> value(T t){
			f.t=t;
			return this;
		}
		public Builder<T> init(Function<T,Void> init){
			f.init=init;
			return this;
		}
		public Builder<T> destroy(Function<T,Void> destroy){
			f.destroy=destroy;
			return this;
		}
		public FactoryBeanWrapper<T> build(){
			return f;
		}
		
	}
}
