package com.bonc.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
