package com.bonc.test.gbf;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;
/**
 * 测试Thread、Object、锁、线程协调器等方法运行后，线程状态。
 * 只有LockSupport响应中断，退出阻塞。
 * @author litianlin
 * @date   2019年11月13日上午9:46:20
 * @Description TODO
 */
public class ThreadStatusTest {
	Boolean flag=true;
	//Thread测试，join、sleep等方法后的状态
	Thread sleep=new Thread(()->{
		try {
			System.out.println("Sleep run.");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	});
	Thread objWait=new Thread(()->{
		try {
			synchronized (flag) {
				System.out.println("Obj wait run.");
				flag.wait();				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	});
	
	
	@Test
	public void testThreadSleep() throws Exception {
		sleep.start();
		Thread.sleep(100);
		System.out.println("sleep:"+sleep.getState());
	}
	@Test
	public void testThreadJoin() throws Exception {
		Thread join=new Thread(()->{
			try {
				sleep.start();
				sleep.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		join.start();
		Thread.sleep(100);
		System.out.println("join:"+join.getState());
	}
	@Test
	public void testObjectWait() throws Exception {
		objWait.start();
		Thread.sleep(100);
		System.out.println("objWait:"+objWait.getState());
	}
	@Test//synchronized阻塞
	public void testSyncBlock() throws Exception {
		Thread sync=new Thread(()->{
			try {
				synchronized (flag) {
					System.out.println("Sync wait run.");
					Thread.sleep(1000);;				
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		sync.start();
		objWait.start();
		Thread.sleep(100);
		System.out.println("Sync:"+objWait.getState());
	}
	@Test//Lock阻塞
	public void testLock() throws Exception {
		Thread lock=new Thread(()->lockTest());
		Thread lock2=new Thread(()->lockTest());
		lock.start();
		lock2.start();
		Thread.sleep(100);
		lock2.interrupt();
		Thread.sleep(100);
		System.out.println("Lock.lock:"+lock2.getState());
	}
	ReentrantLock lock = new ReentrantLock();
	private void lockTest() {
		try {
			lock.lock();
			System.out.println("lockTest run.");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	@Test//lockInterruptibly，异常时必须手动释放锁。
	public void testLockInterruptibly() throws Exception {
		Thread lock=new Thread(()->lockInterruptiblyTest());
		Thread lock2=new Thread(()->lockInterruptiblyTest());
		lock.start();
		lock2.start();
		Thread.sleep(100);
		lock.interrupt();
		Thread.sleep(100);
		System.out.println("Lock.lockInterruptiblyTest:"+lock2.getState());
	}
	private void lockInterruptiblyTest() {
		try {
			lock.lockInterruptibly();
			System.out.println("lockInterruptiblyTest run.");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("lockInterruptiblyTest, interrupted: "+Thread.interrupted());
		}finally {
			lock.unlock();
		}
	}
	
	@Test//LockSupport响应中断，退出阻塞
	public void testLockSupport() throws Exception {
		Thread lock=new Thread(()->parkTest());
		lock.start();
		Thread.sleep(100);
		System.out.println("LockSupport.park:"+lock.getState());
		lock.interrupt();
		Thread.sleep(100);
		System.out.println("end");
		
	}
	private void parkTest() {
		System.out.println("parkTest run.");
		LockSupport.park();
		System.out.println("parkTest, interrupted:"+Thread.interrupted());
	}
	
}
