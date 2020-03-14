package com.bonc.test.debug;

import java.util.ArrayList;

import org.junit.Test;

public class DebugTest {
	ArrayList<String> al=new ArrayList<>();
	private void add() {
		for (int i = 0; i < 10000; i++) {
			al.add("lkk");
		}
		System.out.println(Thread.currentThread().getName());
	}
	@Test
	public void testAdd() throws InterruptedException {
		Thread t1=new Thread(()->add(),"t1");
		Thread t2=new Thread(()->add(),"t2");
		Thread t3=new Thread(()->{
			try {
				while(true) {Thread.sleep(100);}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		},"t3");
		t1.start();
		t2.start();
		t3.start();
		t3.join();
	}
}
