/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.srv;

import br.com.sbrw.mp.util.MemoryUsage;
import io.netty.channel.ChannelFuture;

public class Main {
	public static void main(String[] args) {
		int port = 9998;
		if (args.length == 1) {
			port = Integer.parseInt(args[0]); 
		}
		try {
			new MemoryUsage(Integer.valueOf(port));
			NettyUdpServer server = new NettyUdpServer(port);
			ChannelFuture future = server.start();
			System.out.println("### SoapboxRaceMP module has been started, port: " + port);
			future.channel().closeFuture().sync();
		} catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		} 
	}
}
