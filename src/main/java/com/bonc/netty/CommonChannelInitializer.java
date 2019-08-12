package com.bonc.netty;

import java.util.Map;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
/**
 * 类似HandlerMapping，映射channel
 * @author litianlin
 * @date   2019年8月12日上午11:15:31
 * @Description TODO
 */
public class CommonChannelInitializer extends ChannelInitializer<SocketChannel>{
	private Map<String,ChannelHandler> chs;
	
	public CommonChannelInitializer(Map<String, ChannelHandler> chs) {
		super();
		this.chs = chs;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		if(chs==null && chs.isEmpty()) {
			return;
		}
		
		chs.forEach(ch.pipeline()::addLast);
	}

}
