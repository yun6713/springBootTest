package com.bonc.test.gbf;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并行的快速排序；每次分组，多开一个线程排序右边数据
 * 递归不适合并行
 * @author litianlin
 * @date   2019年11月15日下午4:39:07
 * @Description TODO
 */
public class ParallelFastSort {
	static int[] arr=new int[1000];
	static CyclicBarrier cb=new CyclicBarrier(2);
	static AtomicInteger ai=new AtomicInteger();
	static ExecutorService es=Executors.newCachedThreadPool();
	public static void qsort(int[] arr,int start,int end) throws InterruptedException, BrokenBarrierException {        
	    int pivot = arr[start];        
	    int i = start;        
	    int j = end;        
	    while (i<j) {            
	        while ((i<j)&&(arr[j]>pivot)) {                
	            j--;            
	        }            
	        while ((i<j)&&(arr[i]<pivot)) {                
	            i++;            
	        }            
	        if ((arr[i]==arr[j])&&(i<j)) {                
	            i++;            
	        } else {                
	            int temp = arr[i];                
	            arr[i] = arr[j];                
	            arr[j] = temp;            
	        }        
	    } 
	    int num=i-1;
	    if (num>start) {
//	    	es.execute(()->{
//	    		try {
//					qsort(arr,start,num);
//				} catch (InterruptedException | BrokenBarrierException e) {
//					e.printStackTrace();
//				}
//	    	});
	    	new Thread(()->{
	    		try {
	    			ai.incrementAndGet();
					qsort(arr,start,num);
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}finally {
					ai.decrementAndGet();
				}
	    	}).start();
	    }else if(start==0){
//	    	执行完毕，等待解除阻塞
	    	System.out.println("start："+cb.getNumberWaiting());
	    	cb.await();
	    }
//	    if (num>start) qsort(arr,start,num);        
	    if (j+1<end) {
	    	qsort(arr,j+1,end); 
	    }else if(end==arr.length-1){
	    	System.out.println("end："+cb.getNumberWaiting());
	    	cb.await();
	    }
	}    
	public static void initArr() {
		Random random=new Random();
		for (int i = 0; i < arr.length; i++) {
			arr[i]=random.nextInt(arr.length);
		}
	}
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {   
		initArr();
	    System.out.println(Arrays.toString(arr)); 
	    int len = arr.length-1;         
	    qsort(arr,0,len); 
	    es.shutdown();
	    do {
	    	Thread.sleep(100);
	    }while(ai.get()!=0);
	    System.out.println(Arrays.toString(arr)); 
	}
}
