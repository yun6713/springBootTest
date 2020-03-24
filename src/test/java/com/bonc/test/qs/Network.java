package com.bonc.test.qs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

import org.junit.Test;

public class Network {
	@Test
	public void basic() throws IOException{
		InetAddress ia=InetAddress.getByName("baidu.com");
		System.out.println(ia.isReachable(3000));
		URL url;
		URI uri;
	}
}
