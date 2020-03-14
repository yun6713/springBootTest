package com.bonc.test.gbf;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//排序子数组
public class ParallelHillSort {
	static int[] arr=new int[100];
	public static class SortTask implements Runnable{
		int loc,step;
		CountDownLatch cdl;
//loc初始位置，step步进量
		public SortTask(int loc, int step, CountDownLatch cdl) {
			super();
			this.loc = loc;
			this.step = step;
			this.cdl = cdl;
		}
//排序子数组
		@Override
		public void run() {
			sortDesc(loc,step);
			cdl.countDown();//计数减一
		}
//		降序，插入排序，只和已排序好的数组比较
		public static void sortDesc(int loc,int step) {
			int j=loc-step;
//			防越界
			if(arr[loc]<arr[j]) {
				int tmp=arr[loc];
				arr[loc]=arr[j];
		//				排序子数组后续数值,用tmp比较
				while((j-=step)>=0 && tmp<arr[j]) {
					arr[j+step]=arr[j];
//					System.out.println(j);
				}
				arr[j+step]=tmp;//最终位置;while判定会加一个step
			}			
		}
	}
	public static void initArr() {
		Random random=new Random();
		for (int i = 0; i < arr.length; i++) {
			arr[i]=random.nextInt(1000);
		}
	}
	public static void main(String[] args) throws InterruptedException {
		ExecutorService es=Executors.newCachedThreadPool();
		CountDownLatch cdl;
		initArr();
//		希尔排序初始子数组最多元素数为3，增量为3倍；计算步进量
		int step=1;
		while(step<=arr.length/3) {
			step=step*3+1;//确保初始子数组最多元素数为3
		}
//		并行计算，每个子数组一个任务
		while(step>0) {
			System.out.println("step: "+step);
//			子数组最后一个元素无比较对象
			if(step>=4) {
				cdl=new CountDownLatch(arr.length-step);
				for (int i = step; i < arr.length; i++) {
					es.execute(new SortTask(i,step,cdl));
				}
				cdl.await();
			}else {
				step=1;
				for (int i = step; i < arr.length; i++) {
					SortTask.sortDesc(i, step);
				}			
			}
				step=(step-1)/3;//???
		}
		es.shutdown();
		System.out.println("result: "+Arrays.toString(arr));
	}
}