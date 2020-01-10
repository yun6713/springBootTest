package com.bonc.test.gbf;

import org.junit.Test;

public class DisrupterTest1 {
	Long l=0L;
	public static final int SIZE=50000*10000;
	@Test
	public void testNoLock(){
		Long start=System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++) {
			l++;
		}
		System.out.println(System.currentTimeMillis()-start);
	}
	public void inc() {		
		for (int i = 0; i < SIZE; i++) {
			synchronized (l) {
				l++;
			}
		}
		System.out.println(l);
	}
	@Test
	public void testLock1T() throws InterruptedException{
		Long start=System.currentTimeMillis();
		Thread t=new Thread(()->{
			inc();
			System.out.println(System.currentTimeMillis()-start);
		});
		t.start();
		t.join();
	}
	
}
