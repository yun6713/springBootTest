package com.bonc.integrate;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;

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
	@Scheduled(fixedRate=60*1000)
	public void scheduled() {
		LOG.info("QuatzConfig#scheduled()");
	}
	@Bean
	public JobDetail quartzJobBean(){
		return JobBuilder.newJob(TestJob.class)
				.withIdentity("testTask1")
				.storeDurably()
				.build();
	}
	@Bean
    public Trigger testQuartzTrigger1() {
        //5秒执行一次
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzJobBean())
                .withIdentity("testTask1")
                .withSchedule(scheduleBuilder)
                .build();
    }
	public static class TestJob extends QuartzJobBean{
		@Autowired
		TestService ts;
		@Override
		protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
			ts.testAsyncSecurity();
		}
	}
}
