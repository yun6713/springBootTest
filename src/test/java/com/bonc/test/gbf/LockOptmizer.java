package com.bonc.test.gbf;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.apache.log4j.helpers.ThreadLocalMap;

public class LockOptmizer {
	
	public void threadLocalCpt() {
		Thread t;
		ThreadLocalMap map;
		SoftReference sf;
		WeakReference wr;
		PhantomReference pr;
		ReferenceQueue rq;
		ThreadLocal<String> tl=new ThreadLocal<>();
		tl.set("ltl");
		System.out.println(tl.get());
		AtomicReferenceArray ara;
		
	}
	public void atomicCpt() {
		AtomicInteger ai=new AtomicInteger();
		ai.addAndGet(1);
		int num=ai.get();
		ai.compareAndSet(num, num+1);
		ai.incrementAndGet();
		sun.misc.Unsafe UNSAFE;
	}
	
	public void futureMode() {
		Future f;
		FutureTask ft;
	}
	
}
