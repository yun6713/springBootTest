package com.bonc.integrate;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.bonc.service.TestService;
/**
 * spring.quartz.*，配置
 * @author litianlin
 * @date   2019年8月26日下午5:12:18
 * @Description TODO
 */
@Configuration
public class QuartzConfig {
	private static final Logger LOG=LoggerFactory.getLogger(QuartzConfig.class);
	@Scheduled(fixedRate=5*1000)
	public void scheduled() {
		LOG.info("QuatzConfig#scheduled()");
	}
	@Bean
	public JobDetail quartzJobBean(){
		JobDataMap map = new JobDataMap();
		map.put("k1", "v2");
		return JobBuilder.newJob(TestJob.class)
				.withIdentity("testTask1")
				.setJobData(map)
				.storeDurably()//持久化
				.requestRecovery(false)//recovery、fail-over时是否重新执行
				.build();
	}
	@Bean
    public Trigger testQuartzTrigger1() {
        //5秒执行一次
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .withMisfireHandlingInstructionFireNow()//misfire策略
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzJobBean())
                .withIdentity("testTask1")
//                .startNow()
                .withSchedule(scheduleBuilder)
                .build();
    }
	//继承SpringBeanJobFactory，由其创建job；而后托管给context，使Job类可通过注解注入bean
//	public static class AutowireJobFactory extends SpringBeanJobFactory{
//		AutowireCapableBeanFactory beanFactory;
//		public AutowireJobFactory(ApplicationContext context) {
//			beanFactory = context.getAutowireCapableBeanFactory();
//		}
//		@Override
//		protected Object createJobInstance(TriggerFiredBundle arg0) throws Exception {
//			Object job = super.createJobInstance(arg0);
//			beanFactory.autowireBean(job);
//			return job;
//		}
//	}
////	@Bean
//	public JobFactory jobFactory(ApplicationContext context) {
//		return new AutowireJobFactory(context);
//	}
//	@Bean
//	public Scheduler schedulerFactoryBean(JobFactory jobFactory) throws Exception {
//		SchedulerFactoryBean sfb = new SchedulerFactoryBean();
//		sfb.setJobFactory(jobFactory);
//		sfb.afterPropertiesSet();
//		Scheduler s=sfb.getObject();
//		s.start();
//		s.scheduleJob(testQuartzTrigger1());
//		return s;
//	}
//	@Bean//必须绑定job
    public Trigger testQuartzTrigger2() {
        //5秒执行一次
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever();
        return TriggerBuilder.newTrigger()
//        		.forJob(jd)
//                .withIdentity("testTask2")
//                .startNow()
                .withSchedule(scheduleBuilder)
                .build();
    }
	public static class TestJob extends QuartzJobBean{
		@Autowired
		TestService ts;
		private String k1;
		
		public String getK1() {
			return k1;
		}

		public void setK1(String k1) {
			this.k1 = k1;
		}
		@Override
		protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
			ts.testAsyncService();
			LOG.info("TestJob JobDataMap value:k1={}",k1);
		}
	}
	public static class TestJob2 implements Job{
		@Autowired
		TestService ts;
		private String k1;
		
		public String getK1() {
			return k1;
		}

		public void setK1(String k1) {
			this.k1 = k1;
		}

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			ts.testAsyncSecurity();Object obj;
			LOG.info("TestJob2 JobDataMap value:k1={}",k1);
//			LOG.info("JobDataMap:{}",context.getJobDetail().getJobDataMap());
			
		}
	}
}
