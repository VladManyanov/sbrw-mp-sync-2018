/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.srv;

import br.com.sbrw.mp.handler.race.HelloHandler;
import br.com.sbrw.mp.handler.race.PlayerInfoAfterHandler;
import br.com.sbrw.mp.handler.race.PlayerInfoBeforeHandler;
import br.com.sbrw.mp.handler.race.SyncHandler;
import br.com.sbrw.mp.handler.race.SyncHelloHandler;
import br.com.sbrw.mp.handler.race.SyncKeepAliveHandler;
import br.com.sbrw.mp.handler.security.EndHandler;
import br.com.sbrw.mp.handler.security.SecurityHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;

public class ServerChannelInitializer extends ChannelInitializer<DatagramChannel> {
	protected void initChannel(DatagramChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("security", (ChannelHandler)new SecurityHandler());
		pipeline.addLast("hello", (ChannelHandler)new HelloHandler());
		pipeline.addLast("syncHello", (ChannelHandler)new SyncHelloHandler());
		pipeline.addLast("playerInfoBefore", (ChannelHandler)new PlayerInfoBeforeHandler());
		pipeline.addLast("syncKeepAlive", (ChannelHandler)new SyncKeepAliveHandler());
		pipeline.addLast("sync", (ChannelHandler)new SyncHandler());
		pipeline.addLast("playerInfoAfter", (ChannelHandler)new PlayerInfoAfterHandler());
		pipeline.addLast("endHandler", (ChannelHandler)new EndHandler());
	}
}
