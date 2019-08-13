package com.bonc.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
/**
 * 事件驱动，NIO，异步
 * @author Administrator
 *
 */
@Component
public class NettyServer implements InitializingBean{
	private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
    public static final int PORT = 7878;
	public static void main(String[] args) throws InterruptedException {
		new NettyServer().init();
	}
	public void init() throws InterruptedException {
		new Thread() {
			@Override
			public void run() {
				EventLoopGroup bossGroup = new NioEventLoopGroup();
				EventLoopGroup workerGroup = new NioEventLoopGroup();
				try {
					log.info("Server:");
					ServerBootstrap sb = new ServerBootstrap();
					//服务器事件组，处理ServerChannel、Channel事件
					sb.group(bossGroup, workerGroup);
					//channel工厂
					sb.channel(NioServerSocketChannel.class);
					//频道处理，Channel事件处理器集合
					sb.childHandler(new HelloServerInitializer());
					//监听绑定端口
					ChannelFuture cf = sb.bind(PORT).sync();
					//监听服务器关闭监听,阻塞方法，需单开线程执行
					cf.channel().closeFuture().sync();
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}
			}
		}.start();
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		log.info("Server inited");
	}
}
class HelloServerInitializer extends ChannelInitializer<SocketChannel>{
	private static final Logger log = LoggerFactory.getLogger(HelloServerInitializer.class);
    
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline cp = ch.pipeline();
		cp.addLast("frame", new DelimiterBasedFrameDecoder(8192,Delimiters.lineDelimiter()));
		cp.addLast("decoder",new StringDecoder());
		cp.addLast("encoder",new StringEncoder());
		cp.addLast("handler",new HelloServerHandler());
	}
	
}
class HelloServerHandler extends SimpleChannelInboundHandler<String>{
	private static final Logger log = LoggerFactory.getLogger(HelloServerHandler.class);
    
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		log.info("{} say: {}",ctx.channel().remoteAddress(),msg);
		ctx.writeAndFlush("Receive your msg.\n");
		
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("{} active. ",ctx.channel().remoteAddress());
		ctx.writeAndFlush("Welcome\n");
		super.channelActive(ctx);
	}
	
}