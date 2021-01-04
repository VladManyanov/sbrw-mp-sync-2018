/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.handler.race;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.MainRacePacket;
import br.com.sbrw.mp.pktvo.race.player.PlayerInfo;
import br.com.sbrw.mp.pktvo.race.player.PlayerMainPkt;
import br.com.sbrw.mp.protocol.MpAllTalkers;
import br.com.sbrw.mp.protocol.MpSession;
import br.com.sbrw.mp.protocol.MpSessions;
import br.com.sbrw.mp.protocol.MpTalker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import java.util.Iterator;
import java.util.Map;

public class PlayerInfoBeforeHandler extends ChannelInboundHandlerAdapter {
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket)msg;
		ByteBuf buf = (ByteBuf)datagramPacket.content();
		if (isPlayerInfoPacket(buf)) {
			MpTalker mpTalker = MpAllTalkers.get(datagramPacket);
			if (mpTalker != null) {
				MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
				mainPacket.parsePlayerInfo(buf);
				playerInfoBeforeOk(mpTalker);
			} 
		} 
	super.channelRead(ctx, msg);
	}
  
	private void playerInfoBeforeOk(MpTalker mpTalker) {
		MpSession mpSession = MpSessions.get(mpTalker);
		Map<Integer, MpTalker> mpTalkersTmp = mpSession.getMpTalkers();
		if (isAllPlayerInfoBeforeOk(mpSession)) {
			Iterator<Map.Entry<Integer, MpTalker>> iterator = mpTalkersTmp.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, MpTalker> next = iterator.next();
				MpTalker mpTalkerTmp = next.getValue();
				mpTalkerTmp.broadcastToSession(sendPlayerInfo(mpTalkerTmp));
			} 
		} 
	}
  
	private boolean isAllPlayerInfoBeforeOk(MpSession mpSession) {
		Map<Integer, MpTalker> mpTalkersTmp = mpSession.getMpTalkers();
		Iterator<Map.Entry<Integer, MpTalker>> iterator = mpTalkersTmp.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, MpTalker> next = iterator.next();
			MpTalker mpTalker = next.getValue();
			MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
			if (!mainPacket.isPlayerInfoOk())
				return false; 
			}
		return true;
	}
  
	private boolean isPlayerInfoPacket(ByteBuf buf) {
		return (buf.getByte(0) == 1 && buf
		.getByte(6) == -1 && buf
		.getByte(7) == -1 && buf
		.getByte(8) == -1 && buf
		.getByte(9) == -1);
	}
  
	private byte[] sendPlayerInfo(MpTalker mpTalker) {
		MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
		PlayerMainPkt playerMainPkt = mainPacket.getPlayerMainPkt();
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setPlayerInfo(mainPacket.getSbrwParser().getPlayerInfo());
		playerInfo.setPlayerStatePos(mainPacket.getSbrwParser().getCarState());
		playerMainPkt.setPlayerInfo(playerInfo);
		return mpTalker.getMainPacket((IPkt)playerMainPkt);
	}
  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
	}
}
