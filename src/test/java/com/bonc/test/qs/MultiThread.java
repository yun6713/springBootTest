package com.bonc.test.qs;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

/**
 * 多线程
 * @author litianlin
 * @date   2020年3月12日下午12:09:47
 * @Description TODO
 */
public class MultiThread {
	@Test
	public void thread() throws InterruptedException {
		Thread th1 = new Thread() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				long origin = System.currentTimeMillis();
				for (int i = 0; ; i++) {
					if(System.currentTimeMillis()-start>1000) {
						if(System.currentTimeMillis()-origin>10000) {
							break;
						}
						start = System.currentTimeMillis();
						System.out.println(i);
					}
				}
			}
		};
		Thread th2 = new Thread() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				long origin = System.currentTimeMillis();
				for (int i = 0; ; i++) {
					if(System.currentTimeMillis()-start>1000) {
						if(System.currentTimeMillis()-origin>10000) {
							break;
						}
						start = System.currentTimeMillis();
						System.out.println("nana"+i);
					}
				}
			}
		};
		th1.start();
		th2.start();
		th1.join();
		th2.join();
		System.out.println("nana");
		Callable call;
		Runnable run;
	}
	@Test
	public void threadRun() throws InterruptedException {
		Thread th1 = new Thread() {
			@Override
			public void run() {
				System.out.println("nana");
			}
		};
		th1.run();
		th1.start();
		th1.join();
		System.out.println("nana");
		Callable call;
		Runnable run;
	}

	public void threadPool() {
		Executor exe;
		Executors exex;
		ExecutorService es;
		ThreadPoolExecutor tpe;
		RejectedExecutionHandler reh;//拒绝策略
		ScheduledExecutorService ses;
		ForkJoinPool fjp;
		ForkJoinTask fjt;
		RecursiveTask rt;
		RecursiveAction ra;
		CompletableFuture cf;
	}
	
	public void threadCoordinate() {
//		synchronized
		Object obj = new Object();
//		obj.wait();
		obj.notify();
		
		Lock lock;
		Condition cond;
		LockSupport ls;
		
//		aqs
		AbstractQueuedSynchronizer aqs;
		Semaphore s;
		CountDownLatch cdl;
		CyclicBarrier cb;
//		cb.await();
		
		
	}
}
