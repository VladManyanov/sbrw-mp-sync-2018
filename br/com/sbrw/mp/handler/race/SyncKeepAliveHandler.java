/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.handler.race;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.MainRacePacket;
import br.com.sbrw.mp.pktvo.race.sync.ISrvPkt;
import br.com.sbrw.mp.pktvo.race.sync.SrvPkt;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktMainSync;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktSyncKeepAlive;
import br.com.sbrw.mp.protocol.MpAllTalkers;
import br.com.sbrw.mp.protocol.MpTalker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

public class SyncKeepAliveHandler extends ChannelInboundHandlerAdapter {
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket)msg;
		ByteBuf buf = (ByteBuf)datagramPacket.content();
		byte[] packet = ByteBufUtil.getBytes(buf);
		if (isSyncKeepAlive(packet)) {
			MpTalker mpTalker = MpAllTalkers.get(datagramPacket);
			if (mpTalker != null) {
				mpTalker.send(answer(mpTalker, buf)); 
			}
		} 
		super.channelRead(ctx, msg);
	}
  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
	}
  
	private boolean isSyncKeepAlive(byte[] dataPacket) {
		if (dataPacket[0] == 0 && dataPacket[3] == 7 && dataPacket.length == 18) {
			return true; 
		}
		return false;
	}
  
	private byte[] answer(MpTalker mpTalker, ByteBuf buf) {
		short counter = buf.getShort(9);
		MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
		SrvPktSyncKeepAlive srvPktSyncKeepAlive = new SrvPktSyncKeepAlive();
		SrvPkt srvPkt = mainPacket.getSrvPkt();
		SrvPktMainSync srvPktMainSync = mainPacket.getSrvPktMainSync();
		srvPktMainSync.setCounter(counter);
		srvPktMainSync.setPktSync((IPkt)srvPktSyncKeepAlive);
		mainPacket.getSrvPkt().setSrvPkt((ISrvPkt)srvPktMainSync);
		return mpTalker.getMainPacket((IPkt)srvPkt);
	}
}
