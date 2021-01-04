/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.srv;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.net.InetSocketAddress;

public class NettyUdpServer {
	private int port;
	private Channel channel;
	private EventLoopGroup workerGroup;
  
	public NettyUdpServer(int port) {
		this.port = port;
	}
  
	public ChannelFuture start() throws InterruptedException {
		this.workerGroup = (EventLoopGroup)new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		((Bootstrap)((Bootstrap)bootstrap.group(this.workerGroup))
		.channel(NioDatagramChannel.class))
		.handler((ChannelHandler)new ServerChannelInitializer());
		
		ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(this.port)).syncUninterruptibly();
		this.channel = channelFuture.channel();
		return channelFuture;
	}
  
	public void stop() {
		if (this.channel != null) {
			this.channel.close(); 
		}
		this.workerGroup.shutdownGracefully();
	}
}
