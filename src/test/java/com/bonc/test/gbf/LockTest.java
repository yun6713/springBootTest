package com.bonc.test.gbf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class LockTest {
	Lock lock;
	LockSupport ls;
	static int size=100;
	static CountDownLatch cdl=new CountDownLatch(size);
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < size; i++) {
			cdl.countDown();
			new Thread(){
				public void run() {
					try {
						cdl.await();
						System.out.println(Thread.currentThread().getName()+":"+System.nanoTime());
						System.out.println(Thread.currentThread().getName()+":"+System.nanoTime());
//								System.out.println(System.nanoTime());
						for (int i = 0; i < size; i++) {
							Map map=new HashMap();
							map.put("ltl", "love yjn");
							map.remove("");
						}
						System.out.println(Thread.currentThread().getName()+":"+System.nanoTime());
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
		}
	}
}
