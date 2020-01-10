package com.bonc.test.gbf;

import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * disruptor核心类：Disruptor、WorkHandler/EventHandler、RingBuffer
 * 示例项目
 * @author litianlin
 * @date   2019年11月15日下午3:19:38
 * @Description TODO
 */
public class DisrupterTest {
	public static void main(String[] args) throws InterruptedException {
		Disruptor<StringEvent> disruptor=new Disruptor<>(StringEvent::new, 1024*1024, 
				Executors.defaultThreadFactory(), ProducerType.MULTI, new BlockingWaitStrategy());
		Consumer2[] c2Arr=new Consumer2[] {new Consumer2()};
		disruptor.handleEventsWith(c2Arr);
		Consumer1[] c1Arr=new Consumer1[] {new Consumer1()};		
		disruptor.handleEventsWithWorkerPool(c1Arr);
		disruptor.setDefaultExceptionHandler(new ExpHandlerImpl());
		RingBuffer<StringEvent> rb=disruptor.start();
		Producer[] ps=getProducers(3,rb);
		Thread t1=new Thread(()->{
			for (int j = 0; j < 999; j++) {
				for (int i = 0; i < ps.length; i++) {
					ps[i].publish("lkk times："+(j*ps.length+i));
				}
			}
		});
		t1.start();
		t1.join();
		disruptor.shutdown();
	}
	private static Producer[] getProducers(int num,RingBuffer<StringEvent> rb) {
		Producer[] ps=new Producer[num];
		for (int i = 0; i < num; i++) {
			ps[i]=new Producer(rb);
		}
		return ps;
	}
	static class StringEvent {
		private String info="";

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		@Override
		public String toString() {
			return "StringEvent [info=" + info + "]";
		}
		
	}
	static class Consumer1 implements WorkHandler<StringEvent>{
		@Override
		public void onEvent(StringEvent event) throws Exception {
			System.out.println("Consume1: "+event);
		}		
	}
	static class Consumer2 implements EventHandler<StringEvent>{
		@Override
		public void onEvent(StringEvent event, long sequence, boolean endOfBatch) throws Exception {
			System.out.println(String.format("Consume2: %s,%s,%s",event,sequence,endOfBatch));
		}		
	}
	static class ExpHandlerImpl implements ExceptionHandler<StringEvent> {

		@Override
		public void handleEventException(Throwable ex, long sequence, StringEvent event) {
			System.out.println("handleEventException");
		}

		@Override
		public void handleOnStartException(Throwable ex) {
			System.out.println("handleOnStartException");
		}

		@Override
		public void handleOnShutdownException(Throwable ex) {
			System.out.println("handleOnShutdownException");
		}

		
	}
	static class Producer{
		private RingBuffer<StringEvent> rb;

		public Producer(RingBuffer<StringEvent> rb) {
			super();
			this.rb = rb;
		}
		public void publish(String info) {
			Long sequence=rb.next();
			try {
				StringEvent se=rb.get(sequence);
				se.setInfo(info);
			} finally {
				rb.publish(sequence);
			}
		}
	}
}
