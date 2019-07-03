package com.bonc.utils;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class FactoryBeanWrapper<T> implements FactoryBean<T>,InitializingBean,DisposableBean{
	private T t;
	private Callback<T> init;
	private Callback<T> destroy;
	
	public FactoryBeanWrapper(T t, Callback<T> init, Callback<T> destroy) {
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
			init.call(t);
	}

	@Override
	public void destroy() throws Exception {
		if(destroy!=null)
			destroy.call(t);
	}
	
	public static interface Callback<T>{
		void call (T t);
	}
	public static class Builder<T>{
		private FactoryBeanWrapper<T> f = new FactoryBeanWrapper<T>();
		public Builder<T> value(T t){
			f.t=t;
			return this;
		}
		public Builder<T> init(Callback<T> init){
			f.init=init;
			return this;
		}
		public Builder<T> destroy(Callback<T> destroy){
			f.destroy=destroy;
			return this;
		}
		public FactoryBeanWrapper<T> build(){
			return f;
		}
		
	}
}
