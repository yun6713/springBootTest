package com.bonc;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.bonc.controller.H2Controller;
import com.bonc.integrate.Aop.AopTestInterface;
import com.bonc.integrate.QuartzConfig.TestJob2;
/**
 * 输出项目路径
 * @author litianlin
 * @date   2019年7月5日上午11:12:40
 * @Description TODO
 */
@Component
@Lazy//懒加载
public class PathPrintCommandRunner implements CommandLineRunner{
	private final static Logger LOG = LoggerFactory.getLogger(PathPrintCommandRunner.class);
	@Value("${server.address:localhost}:${server.port:8080}/${server.servlet.context-path:}")
	String addr;
	@Autowired
	H2Controller h2;
	@Autowired
	Scheduler scheduler;
	@Override
	public void run(String... args) throws Exception {
		JobDataMap map = new JobDataMap();
		map.put("k1", "v2");
		JobDetail jd = JobBuilder.newJob(TestJob2.class)
				.withIdentity("test21", "group1")
				.setJobData(map).build();
		JobDetail jd2 = JobBuilder.newJob(TestJob2.class)
				.withIdentity("test22", "group1")
				.setJobData(map).build();
		//Trigger--JobDetail,多对一
		Trigger trigger = TriggerBuilder.newTrigger()
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever())
				.withIdentity("1", "Trigger")
				.build();
		if(scheduler.getTrigger(new TriggerKey("1", "Trigger"))==null
				) {
			scheduler.scheduleJob(jd, trigger);
		}
		//输出项目路径
		if(LOG.isInfoEnabled())
			LOG.info(addr);
		if(h2 instanceof AopTestInterface)
			LOG.info(((AopTestInterface)h2).test());
	}
}
