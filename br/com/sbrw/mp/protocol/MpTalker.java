/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.protocol;

import br.com.sbrw.mp.pktvo.IMainPacket;
import br.com.sbrw.mp.pktvo.IPkt;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Date;

public class MpTalker {
	private ChannelHandlerContext ctx;
	private DatagramPacket datagramPacket;
	private IMainPacket mainPacket;
	private long mpTalkerTimerStart = (new Date()).getTime();
  
	public MpTalker(ChannelHandlerContext ctx, DatagramPacket datagramPacket, IMainPacket mainPacket) {
		this.ctx = ctx;
		this.datagramPacket = datagramPacket;
		this.mainPacket = mainPacket;
		System.out.println("session: [" + mainPacket.getSessionId() + "]");
		System.out.println("maxUsers: [" + mainPacket.getMaxUsers().intValue() + "]");
		System.out.println("");
	}
  
	public Integer getSessionId() {
		return this.mainPacket.getSessionId();
	}
  
	public Integer getPort() {
		return Integer.valueOf(((InetSocketAddress)this.datagramPacket.sender()).getPort());
	}
  
	public void send(byte[] packetData) {
		this.ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(packetData), (InetSocketAddress)this.datagramPacket.sender()));
	}
  
	public void broadcastToSession(byte[] packetData) {
		MpSession mpSession = MpSessions.get(this);
		mpSession.broadcastFrom(this, packetData);
	}
  
	public byte[] getMainPacket(IPkt pkt) {
		return this.mainPacket.getPacket(pkt);
	}
  
	public IMainPacket getMainPacket() {
		return this.mainPacket;
	}
  
	public long getMpTalkerTimerStart() {
		return this.mpTalkerTimerStart;
	}
}
