/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.handler.string;

import br.com.sbrw.mp.pktvo.IMainPacket;
import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.string.MainStringPacket;
import br.com.sbrw.mp.pktvo.string.StrHelloPkt;
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

public class StrHelloHandler extends ChannelInboundHandlerAdapter {
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket)msg;
		ByteBuf buf = (ByteBuf)datagramPacket.content();
		if (isHelloPacket(buf)) {
			char charSession = (char)buf.getByte(1);
			char charMaxUsers = (char)buf.getByte(2);
			Integer sessionId = Integer.valueOf(String.valueOf(charSession));
			Integer maxUsers = Integer.valueOf(String.valueOf(charMaxUsers));
			MainStringPacket mainStringPacket = new MainStringPacket(sessionId, maxUsers);
			MpTalker mpTalker = new MpTalker(ctx, datagramPacket, (IMainPacket)mainStringPacket);
			MpSession mpSession = MpSessions.get(mpTalker);
			if (mpSession == null)
				mpSession = new MpSession(mpTalker); 
			mpSession.put(mpTalker);
			MpSessions.put(mpSession);
			if (mpSession.isFull()) {
				Map<Integer, MpTalker> mpTalkers = mpSession.getMpTalkers();
				Iterator<Map.Entry<Integer, MpTalker>> iterator = mpTalkers.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<Integer, MpTalker> next = iterator.next();
					MpTalker mpTalkerTmp = next.getValue();
					MainStringPacket mainPacket = (MainStringPacket)mpTalkerTmp.getMainPacket();
				long sessionTimeStart = mpSession.getSessionTimeStart();
				StrHelloPkt strHelloPkt = new StrHelloPkt(sessionTimeStart, mpTalkerTmp);
				mpTalkerTmp.send(mainPacket.getPacket((IPkt)strHelloPkt));
				} 
			} 
			MpAllTalkers.put(mpTalker);
		} 
	}
  
	private boolean isHelloPacket(ByteBuf buf) {
		char charTmp = (char)buf.getByte(0);
		if (charTmp == '!')
			return true; 
		return false;
	}
}
