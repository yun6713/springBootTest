package com.bonc.test.akka;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.bonc.utils.FileUtils;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxDumper;
import com.github.odiszapc.nginxparser.NgxEntry;
import com.github.odiszapc.nginxparser.NgxParam;

public class NginxReload {
//	private static String TEMPLATE="";
//	private static String MODULE="";
//	@Before
//	public void init() {
//		TEMPLATE=new StringBuilder("worker_processes  1;\n")
//				.append("events { worker_connections  1024; }\n")
//				.append("http { \n")
//				.append("    include       mime.types;\n")
//				.append("    default_type  application/octet-stream;\n")
//				.append("    sendfile        on;\n")
//				.append("    keepalive_timeout  65;\n")
//				.append("    server {\n")
//				.append("        listen       80;\n")
//				.append("        server_name  localhost;\n")
//				.append("        location / {\n")
//				.append("            root   html;\n")
//				.append("            index  index.html index.htm;\n")
//				.append("        }\n")
//				.append("    }\n")
//				.append("}\n")
//				.toString();
//		
//	}
	//测试nginx reload
	@Test
	public void printNginxConf() throws IOException {
		String path="C:/Users/Administrator/Desktop/nginx-1.17.6/conf/nginx.conf";
		NgxConfig conf = NgxConfig.read(path);
		// ...
		NgxDumper dumper = new NgxDumper(conf);
		dumper.dump(System.out);
	}
	@Test
	public void makeNginxConf() throws IOException, InterruptedException {
		String path="C:/Users/Administrator/Desktop/nginx-1.17.6/conf/nginx.conf";
		String content=getConf(Arrays.asList(new WebHost("","")));
		System.out.println(content);
//		FileUtils.string2File(content, path);
//		Process p=Runtime.getRuntime().exec("cmd.exe /c start C:/Users/Administrator/Desktop/nginx-1.17.6/test.bat");
		
	}
	//只拼接http块
	private String getConf(List<WebHost> hosts) {
		//NgxConfig、NgxBlock为块指令，NgxParam行指令
		NgxConfig conf=new NgxConfig();
		addCommonInfo(conf);
		WebHost wh=new WebHost("/","http://localhost:8081/");
//		WebHost wh=new WebHost("/","https://www.baidu.com/");
		NgxBlock server=conf.findBlock("http","server");
//		server.findAll(clazz, params)
		server.addEntry(getLocationBlock(wh));
		return new NgxDumper(conf).dump();
	}
	public void addCommonInfo(NgxConfig conf) {
		String[] confEntrys= {"worker_processes  1"};
		addEntrys(conf,confEntrys);
		//块指令
		String[] eventsEntrys= {"worker_connections  1024"};
		NgxBlock events=getNgxBlock("events",eventsEntrys);
		conf.addEntry(events);
		//http块
		String[] httpEntrys= {"include       mime.types",
				"default_type  application/octet-stream",
				"sendfile        on",
				"keepalive_timeout  65"};
		NgxBlock http=getNgxBlock("http",httpEntrys);
		conf.addEntry(http);
		//http-server块
		String[] serverEntrys= {"listen       8080",
				"server_name  localhost",
				"error_page   500 502 503 504  /50x.html"};
		NgxBlock server=getNgxBlock("server",serverEntrys);
		http.addEntry(server);		
	}
	private NgxBlock getNgxBlock(String[] blockValues,String[] entrys) {
		NgxBlock block=new NgxBlock();
		//块指令名，可加入多次；会用空格分隔
		for (String blockValue:blockValues) {
			block.addValue(blockValue);
		}
		addEntrys(block,entrys);
		return block;
	}
	private NgxBlock getNgxBlock(String blockValue,String[] entrys) {
		return getNgxBlock(new String[] {blockValue},entrys);
	}
	/**
	 * 根据WebHost，获取Location块指令
	 * @param wh
	 * @return
	 */
	private NgxBlock getLocationBlock(WebHost wh) {
		return getNgxBlock(wh.getBlockValues(),wh.getEntrys());
	}
	private NgxBlock addEntrys(NgxBlock block,String[] entrys) {
		if(entrys!=null && entrys.length>0) {
			for (String entry:entrys) {
				NgxParam param=new NgxParam();
				block.addEntry(param);
				//行指令，直接加入；不要分号
				param.addValue(entry);
			}
		}
		return block;
	}
	@Test
	public void testNgxBlock() {
		NgxBlock http=new NgxBlock();
		http.addValue("http");
		NgxBlock events=new NgxBlock();
		//块指令名，可加入多次；会用空格分隔
		events.addValue("events");
	}
	class WebHost{
		private String urlFrom;
		private String urlTo;
		
		public WebHost(String urlFrom, String urlTo) {
			super();
			this.urlTo = urlTo;
			this.urlFrom = urlFrom;
		}
		public String getUrlTo() {
			return urlTo;
		}
		public void setUrlTo(String urlTo) {
			this.urlTo = urlTo;
		}
		public String getUrlFrom() {
			return urlFrom;
		}
		public void setUrlFrom(String urlFrom) {
			this.urlFrom = urlFrom;
		}
		public String[] getEntrys() {
			return new String[] {"proxy_pass "+getUrlTo()};
		}
		public String[] getBlockValues() {
			return new String[] {"location",getUrlFrom()};
		}
	}
	@Test
	public void testBat() throws Exception {
		Process p=Runtime.getRuntime().exec("cmd /c C:/Users/Administrator/Desktop/nginx-1.17.6/test2.bat");
//		p.destroy();
		p.waitFor();
		
	}
}
