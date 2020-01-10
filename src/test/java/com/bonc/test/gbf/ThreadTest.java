package com.bonc.test.gbf;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;

public class ThreadTest implements Runnable{
	static Boolean lkk=true;
	@Test
	public void test01() throws InterruptedException {
//		Thread.interrupted();
//		Thread.currentThread().interrupt();
//		this.wait();
//		this.notify();
//		测试不notify，能否唤醒线程
		Thread a=new Thread() {
			@Override
			public void run() {
				synchronized (lkk) {
					try {
						Thread.sleep(100);
						lkk.wait(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("ltl");
				}
			}
		},b=new Thread() {
			@Override
			public void run() {
				synchronized (lkk) {
					try {
						Thread.sleep(100);
//						lkk.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("ltl2");
//					lkk.notify();
				}
			}
		};
		a.start();
		b.start();
		Thread.sleep(10000);
		System.out.println("over");
	}
	@Test
	public void testJoin() throws InterruptedException {
		Thread t1=new Thread(()->{
			try {
				Thread.sleep(1000);
//				lkk.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("sync map1 "+System.currentTimeMillis());});
		Thread t2=new Thread(()->{
			try {
				Thread.sleep(1000);
//				lkk.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("sync map2 "+System.currentTimeMillis());});
//		先start后join
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println("lkk");
	}
	static class ReaderThread extends Thread{
		@Override
		public void run() {
			System.out.println("begin");
			while(lkk) {
				System.out.println(lkk);
			}
			System.out.println(lkk);
		}
	}
	@Test
	public void test2() throws InterruptedException {
		new ReaderThread().start();
		Thread.sleep(10);
		lkk=false;
		System.out.println("over1");
		Thread.sleep(1000);
		System.out.println("over");
		Vector v;
		Integer i;
	}
	
	static volatile Integer j=0;
	static ThreadTest t=new ThreadTest();
	public void run() {
		for (int i = 0; i < 60; i++) {
			synchronized (t) {
				j++;
			}
		}
	}
	@Test
	public void test3() throws InterruptedException {
		Thread t1=new Thread(t);
		Thread t2=new Thread(t);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		t2.interrupt();
		System.out.println(j);
	}
	@Test
	public void testReentrantLock() throws Exception {
//		非公平锁
		ReentrantLock rl=new ReentrantLock(false);
		rl.lockInterruptibly();
		rl.lock();
		Condition c=rl.newCondition();
		Semaphore s=new Semaphore(6);
		s.acquire();
		s.acquireUninterruptibly();
		ReadWriteLock rrwl=new ReentrantReadWriteLock();
		rrwl.readLock().lockInterruptibly();;
		CountDownLatch cdl=new CountDownLatch(5);
		cdl.await();
		CyclicBarrier cb=new CyclicBarrier(0, null);
		cb.await();
		LockSupport.park();
		LockSupport.park(this);
		LockSupport.unpark(Thread.currentThread());
		LockSupport.getBlocker(Thread.currentThread());
	}
	@Test
	public void testBlocker() throws Exception {
		Map<String,String> map=new HashMap<>();
		map.put("ltl", "lkk");
		Thread t1=new Thread(()->{
			System.out.println("sync map");
			LockSupport.park(map);});
		t1.start();
		Thread t2=new Thread(()->{
			System.out.println("sync2 map");
			LockSupport.park(map);});
		t2.start();
		Thread.sleep(10);t2.wait();
		System.out.println("get map");
		((Map)(LockSupport.getBlocker(t1))).put("hello", "world");
		System.out.println(LockSupport.getBlocker(t1));
		System.out.println(LockSupport.getBlocker(t2));
		LockSupport.unpark(t1);
		LockSupport.unpark(t2);
		Thread.sleep(10);
		System.out.println(LockSupport.getBlocker(t1));
		System.out.println(LockSupport.getBlocker(t2));
	}
}
