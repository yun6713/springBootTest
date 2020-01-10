package com.bonc.test.gbf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
@sun.misc.Contended
public class Java8 {
	
	public void concept() {
		
	}
	@Test
	public void lambdaTest() {
		List<Integer> list=new ArrayList<>();
		list.addAll(Arrays.asList(1,2,3));
		list.stream().forEach(i->i=i!=null?++i:0);
		List<String> l2=list.stream()
				.map(Double::toString).collect(Collectors.toList());
		System.out.println(list);
		System.out.println(l2);
	}
	@FunctionalInterface
	public interface InterfaceTest1 {
//		被Object实现的方法，不被视作抽象方法
		public boolean equals(Object obj);
		public String lkk();
		public default void ltl() {
			System.out.println("ltl");
		}
	}
	@FunctionalInterface
	public interface InterfaceTest2 {
//		被Object实现的方法，不被视作抽象方法
		public boolean equals(Object obj);
		public String lkk2();
		public default void ltl() {
			System.out.println("ltl2");
		}
	}
	class A implements InterfaceTest2,InterfaceTest1{

		@Override
		public String lkk() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String lkk2() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void ltl() {
			// TODO Auto-generated method stub
			InterfaceTest1.super.ltl();
		}
		
	}
	@Test
	public void defaultMethodTest() {
		A a=new A();
		a.ltl();
	}
	@Test
	public void parallelTest() {
		int[] ints=IntStream.range(1, 1000)
				.parallel()
				.sorted().toArray();
		System.out.println(Arrays.toString(ints));
		int[] arr=new int[1000];
		Random r=new Random();
		Arrays.parallelSetAll(arr, i->r.nextInt(i+1));
		System.out.println(Arrays.toString(arr));
		Arrays.parallelSort(arr);
		System.out.println(Arrays.toString(arr));
	}
	@Test
	public void completableFuture() {
		CompletableFuture<Integer> cf=getCf(),
				cf2=getCf();
		cf.thenApply(i->{System.out.println(i);return ++i;})
//			串行
			.thenCompose(i->getCf(i))
			.thenApply(i->{System.out.println(i);return i;})
//			并行
			.thenCombine(cf2, (a,b)->{System.out.println(b);return a+b;})
			.thenAccept(System.out::println)
			.thenRun(()->System.out.println("thenRun"))
			.whenComplete((r,e)->System.out.println("whenComplete"));
	}
	Random random=new Random();
	public CompletableFuture<Integer> getCf(){
		return getCf(Integer.MAX_VALUE);
	}
	public CompletableFuture<Integer> getCf(int i){
		return CompletableFuture.supplyAsync(()->random.nextInt(i));
	}
	@Test
	public void stampedLockTest() throws InterruptedException {
		StampedLock sl=new StampedLock();
		Thread t1=new Thread(()-> {
			sl.writeLock();
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Thread t2=new Thread(()-> {
			sl.readLock();
			System.out.println("get read lock");
		});
		t1.start();
		t2.start();
		System.out.println("thread started");
		t2.interrupt();
		Thread.sleep(100);
		System.out.println("t2: "+t2.getState());
		Thread.sleep(1000);
		System.out.println("t2: "+t2.getState());
		Thread.sleep(1000);
	}
	public void longAdderTest() {
		LongAdder la=new LongAdder();
		DoubleAdder d=new DoubleAdder();
		
	}
}
