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
import br.com.sbrw.mp.pktvo.race.sync.SrvPktSyncStart;
import br.com.sbrw.mp.protocol.MpAllTalkers;
import br.com.sbrw.mp.protocol.MpSession;
import br.com.sbrw.mp.protocol.MpSessions;
import br.com.sbrw.mp.protocol.MpTalker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import java.util.Iterator;
import java.util.Map;

public class SyncHelloHandler extends ChannelInboundHandlerAdapter {
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket)msg;
		ByteBuf buf = (ByteBuf)datagramPacket.content();
		if (isHelloSync(ByteBufUtil.getBytes(buf))) {
			MpTalker mpTalker = MpAllTalkers.get(datagramPacket);
			if (mpTalker != null) {
				MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
				mainPacket.setHelloSyncBuf(buf);
				byte playerIdx = parsePlayerIdx(buf);
				mainPacket.getPlayerMainPkt().setPlayerIdx(playerIdx);
				mpTalker.send(answer(mpTalker));
				syncHelloOk(mpTalker);
			} 
		} 
		super.channelRead(ctx, msg);
	}
  
	private void syncHelloOk(MpTalker mpTalker) {
		MpSession mpSession = MpSessions.get(mpTalker);
		if (isAllSyncHelloOk(mpSession)) {
			Map<Integer, MpTalker> mpTalkers = mpSession.getMpTalkers();
			Iterator<Map.Entry<Integer, MpTalker>> iterator = mpTalkers.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, MpTalker> next = iterator.next();
				MpTalker mpTalkerTmp = next.getValue();
				mpTalkerTmp.send(answer(mpTalkerTmp));
			} 
		} 
	}
  
	private boolean isAllSyncHelloOk(MpSession mpSession) {
		Map<Integer, MpTalker> mpTalkersTmp = mpSession.getMpTalkers();
		Iterator<Map.Entry<Integer, MpTalker>> iterator = mpTalkersTmp.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, MpTalker> next = iterator.next();
			MpTalker mpTalker = next.getValue();
			MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
			if (!mainPacket.isSyncHelloOk())
				return false; 
			} 
		return true;
	}
  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
	}
  
	private boolean isHelloSync(byte[] dataPacket) {
		if (dataPacket[0] == 0 && dataPacket[3] == 7 && dataPacket.length == 26) {
			return true; 
		}
		return false;
	}
  
	private byte[] answer(MpTalker mpTalker) {
		MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
		SrvPkt srvPkt = mainPacket.getSrvPkt();
		ByteBuf helloSyncBuf = mainPacket.getHelloSyncBuf();
		short counter = helloSyncBuf.getShort(9);
		int sessionId = helloSyncBuf.getInt(16);
		byte maxPlayers = parseMaxPlayers(helloSyncBuf);
		SrvPktSyncStart srvPktSyncStart = new SrvPktSyncStart();
		srvPktSyncStart.setSessionId(sessionId);
		srvPktSyncStart.setMaxPlayers(maxPlayers);
		SrvPktMainSync srvPktMainSync = mainPacket.getSrvPktMainSync();
		srvPktMainSync.setPktSync((IPkt)srvPktSyncStart);
		srvPktMainSync.setCounter(counter);
		srvPkt.setSrvPkt((ISrvPkt)srvPktMainSync);
		return mpTalker.getMainPacket((IPkt)srvPkt);
	}
  
	private byte parsePlayerIdx(ByteBuf helloSyncBuf) {
		byte playerIdx = helloSyncBuf.getByte(20);
		playerIdx = (byte)(playerIdx & 0xE0);
		playerIdx = (byte)(playerIdx >> 1);
		playerIdx = (byte)(playerIdx & Byte.MAX_VALUE);
		playerIdx = (byte)(playerIdx >> 4);
		return playerIdx;
	}
  
	private byte parseMaxPlayers(ByteBuf helloSyncBuf) {
		byte maxPlayers = helloSyncBuf.getByte(20);
		maxPlayers = (byte)(maxPlayers & 0xE);
		maxPlayers = (byte)(maxPlayers >> 1);
		return maxPlayers;
	}
}
