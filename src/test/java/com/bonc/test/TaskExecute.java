package com.bonc.test;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;

public class TaskExecute {
	public static final ExecutorService ES=Executors.newFixedThreadPool(20);
	public static final Logger LOG=LoggerFactory.getLogger(TaskExecute.class);
	public static void main(String[] args) throws InterruptedException {
		Long start=System.currentTimeMillis();
		for (int i = 0; i < 200; i++) {
			final int rcd=i;
			execute(()->{
				LOG.info(Thread.currentThread().getName()+";"+rcd);
//				System.out.println(Thread.currentThread().getName());
//				return null;
			});
			System.out.println(i);
		}
		ES.shutdown();
		ES.awaitTermination(60, TimeUnit.SECONDS);
		System.out.println(System.currentTimeMillis()-start);
	}
	public static void execute(Callable<?> c) {
		ES.submit(c);
	}
	public static void execute(Runnable r) {
		ES.execute(r);
	}
	volatile int[] a;
	@Test
	public void test() {
		FutureTask ft;
		LockSupport ls;
		AbstractQueuedSynchronizer aqs;
		Phaser p;
		Constructor c;
		ReflectUtils r;
		System.out.println(2==new Double(2.0));
		System.out.println(1==new Double(1.0));
	}
}
