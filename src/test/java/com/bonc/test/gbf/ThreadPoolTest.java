package com.bonc.test.gbf;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ThreadPoolTest {
	@Test
	public void testExecutors() throws InterruptedException {
		ExecutorService es=Executors.newCachedThreadPool();
		ScheduledExecutorService ses=Executors.newScheduledThreadPool(3);
		ses.scheduleAtFixedRate(()->{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(System.currentTimeMillis());
		}, 0, 1, TimeUnit.SECONDS);
		ses.scheduleWithFixedDelay(()->{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(System.currentTimeMillis()%2==0) {
				System.out.println("error");
				throw new RuntimeException();
			}
			System.out.println("t2:"+System.currentTimeMillis());
		}, 0, 1, TimeUnit.SECONDS);
		Thread.sleep(100000);
	}
	public void threadPoolExecutorTest() {
		ThreadPoolExecutor tpe;
		
//		任务队列
		SynchronousQueue sq;
		ArrayBlockingQueue abq;
		LinkedBlockingQueue lbq;
		PriorityBlockingQueue pbq;
//		拒绝策略
		List<RejectedExecutionHandler> handlers=Arrays.asList(
				new ThreadPoolExecutor.AbortPolicy(),
				new ThreadPoolExecutor.CallerRunsPolicy(),
				new ThreadPoolExecutor.DiscardOldestPolicy(),
				new ThreadPoolExecutor.DiscardPolicy()
				);
		
	}
	@Test//测试扩展线程池
	public void tpeExTest() throws InterruptedException {
		ThreadPoolExecutor tpe=new ThreadPoolExecutor(20,20,100,
				TimeUnit.SECONDS,new SynchronousQueue<Runnable>()) {
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				System.out.println("Task start");
			}
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				if(t==null) {
					System.out.println("Task success");
				}else {
					System.out.println(t.getMessage());
				}
			}
			@Override
			protected void terminated() {
				System.out.println("lkk");
			}
			@Override
			public Future<?> submit(Callable task) {
				System.out.println("121");
				// TODO Auto-generated method stub
				return super.submit(wrap(task,new Exception("log"),Thread.currentThread().getName()));
			}
			private Exception clientTrace() {
				return new Exception("log");
			}
			private Runnable wrap(Runnable task,Exception logExp,String tName) {
				return new Runnable() {
					@Override
					public void run() {
						try {
							task.run();
						} catch (Exception e) {
							System.out.println(tName);
							logExp.printStackTrace();
							throw e;
						}
					}
				};
			}
			private Callable wrap(Callable task,Exception logExp,String tName) {
				return new Callable() {
					@Override
					public Object call() throws Exception {
						try {
							return task.call();
						} catch (Exception e) {
							System.out.println(tName);
							logExp.printStackTrace();
							throw e;
						}
					}
				};
			}
		};
		tpe.submit(()->{System.out.println("ltl");throw new RuntimeException();});
		tpe.shutdown();
		System.out.println(Runtime.getRuntime().availableProcessors());
		Thread.sleep(100);
	}
	public void forkJoinPool() {
		ForkJoinPool fjp;
		ForkJoinTask fjt;
		RecursiveTask rt;
		RecursiveAction ra;
	}
	class Task extends RecursiveTask<Integer>{
		private static final long serialVersionUID = 1L;
		
		int n;
		public Task(int n) {
			this.n=n;
		}

		@Override
		protected Integer compute() {
			if(n<2) return n;
			ForkJoinTask<Integer> f1=new Task(n-1).fork();
			Task t2=new Task(n-2);//减少使用线程数
			return f1.join()+t2.compute();
		}
		
	}
	@Test
	public void forkJoinPoolTest() throws InterruptedException, ExecutionException {
		int n=5;
		System.out.println(new ForkJoinPool().submit(new Task(n)).get());
	}
}
