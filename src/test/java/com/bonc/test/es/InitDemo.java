package com.bonc.test.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

public class InitDemo {
	private static TransportClient client;
	public static TransportClient getClient(){
		if(client==null){
			Settings settings=Settings.builder()
					.put("cluster_name","jftest")
					.put("sniff",true).build();
		}
		
		return client;
	}
}
