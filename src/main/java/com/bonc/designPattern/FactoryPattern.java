package com.bonc.designPattern;
/**
 * 静态工厂、工厂方法、抽象工厂
 * @author Administrator
 *
 */
public class FactoryPattern {
	//静态工厂
	public static Product staticFactory(Type type){
		Product p;
		switch (type) {
		case A:
			p=new ProductA();
			break;

		default:
			p=new ProductA();
			break;
		}
		return p;
	}
	//工厂方法，每个类型一个实现类
	public static interface FactoryMethod{
		Product getProduct();
	}
	public static class FactoryMethodImplA implements FactoryMethod{
		@Override
		public Product getProduct() {
			return new ProductA();
		}
		
	}
	public static class FactoryMethodImplB implements FactoryMethod{
		@Override
		public Product getProduct() {
			return new ProductB();
		}
		
	}
	//抽象工厂，类似多个工厂方法的集合，一个实现类实现一个工厂方法，其余工厂方法返回null
	public static interface AbstractFactory{
		public Product getProduct(String type);
		public Test getTest(String type);
	}
	public static class AbstractFactoryProduct implements AbstractFactory{

		@Override
		public Product getProduct(String type) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Test getTest(String type) {
			return null;
		}
		
	}
	public static class AbstractFactoryTest implements AbstractFactory{

		@Override
		public Product getProduct(String type) {
			return null;
		}

		@Override
		public Test getTest(String type) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	//返回工厂
	public static AbstractFactory getAbstractFactory(String type){
		return "product".equalsIgnoreCase(type)?new AbstractFactoryProduct()
				:new AbstractFactoryTest();
	}
	public static interface Product{}
	private static class ProductA implements Product{
		
	}
	private static class ProductB implements Product{
		
	}
	public static enum Type{
		A,B;
	}
	public static interface Test{}
	private static class TestA implements Test{
		
	}
	private static class TestB implements Test{
		
	}
	
}
