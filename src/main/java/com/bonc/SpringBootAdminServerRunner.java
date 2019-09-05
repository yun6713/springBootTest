package com.bonc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 初始化spring boot admin server
 * @author litianlin
 * @date   2019年7月5日上午11:12:40
 * @Description TODO
 */
//@Order
//@RestController
//@ConditionalOnExpression("'${spring.boot.admin.server.start-command:}'!=''")
public class SpringBootAdminServerRunner implements CommandLineRunner{
	private Process process;	
	private final static Logger LOG = LoggerFactory.getLogger(SpringBootAdminServerRunner.class);
	@Value("${spring.boot.admin.server.start-command:}")
	String command;
	@Value("${spring.boot.admin.server.pid-command:}")
	String pidCommand;
	@Override
	public void run(String... args) throws Exception {
		stopPreProcess();//可能会误关闭其他占用指定端口的进程，慎用
		start();
	}
	private void start() {
		try {
			process=Runtime.getRuntime().exec(command);
			LOG.info("Start spring boot admin server success");
		} catch (IOException e) {
			LOG.error("Start spring boot admin server failed, cause:{}", e.getMessage());
			e.printStackTrace();
		}
	}
	//关闭占用端口的进程;可能会误关闭其他占用指定端口的进程，慎用
	private void stopPreProcess() throws IOException {
		//找到pid
		if(pidCommand==null||"".equals(pidCommand.trim()))
			return;
		String[] cmds= {"cmd.exe","/c",pidCommand};
		Process process=Runtime.getRuntime().exec(cmds);
		CharBuffer cb=CharBuffer.allocate(8*1024);
		StringBuilder sb=new StringBuilder();
		InputStreamReader reader=new InputStreamReader(process.getInputStream());
		while(reader.read(cb)>-1) {
			cb.flip();
			sb.append(cb);
			cb.clear();
		};
		process.destroy();
		String str=sb.toString();
		String[] strs=str.split("\r\n");
		for (int i = 0; i < strs.length; i++) {
			String[] temp=strs[i].split("\\s+");
			if(!temp[temp.length-1].equals("0")&&!StringUtils.isEmpty(temp[temp.length-1])){
				//按pid关闭
				cmds[2]="taskkill /F /PID "+temp[temp.length-1];
				process=Runtime.getRuntime().exec(cmds);
				process.destroy();
				break;
			}
		}
	}
	@RequestMapping("/stopSpringbootAdminServer")
	public Object stopSpringbootAdminServer() throws IOException{
		String result="Spring boot admin server doesn't run";
		if(process!=null &&process.isAlive()) {
//			process.destroy();
			stopPreProcess();
			result="Stop spring boot admin server success";
			LOG.info("Stop spring boot admin server success");
		}
		return result;
	}
	@RequestMapping("/startSpringbootAdminServer")
	public Object startSpringbootAdminServer() throws Exception{
		String result="Spring boot admin server is running";
		if(process==null || !process.isAlive()) {
			start();
			result="Start spring boot admin server success";
		}
		return result;
	}
	@EventListener(classes= {ContextClosedEvent.class,ContextStoppedEvent.class})
	public void shutdown(ApplicationEvent ae) throws IOException {
		System.out.println(ae);
		stopSpringbootAdminServer();
	}
}
