/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.handler.race;

import br.com.sbrw.mp.pktvo.IMainPacket;
import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.MainRacePacket;
import br.com.sbrw.mp.pktvo.race.sync.ISrvPkt;
import br.com.sbrw.mp.pktvo.race.sync.SrvPkt;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktHello;
import br.com.sbrw.mp.protocol.MpAllTalkers;
import br.com.sbrw.mp.protocol.MpSession;
import br.com.sbrw.mp.protocol.MpSessions;
import br.com.sbrw.mp.protocol.MpTalker;
import br.com.sbrw.mp.util.UdpDebug;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class HelloHandler extends ChannelInboundHandlerAdapter {
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	DatagramPacket datagramPacket = (DatagramPacket)msg;
	ByteBuf buf = (ByteBuf)datagramPacket.content();
	if (isHelloPacket(buf) && isTicketOk(buf)) {
		Integer sessionId = Integer.valueOf(buf.getInt(9));
		byte maxUsers = buf.getByte(13);
		short cliTime = buf.getShort(69);
		MainRacePacket mainRacePacket = new MainRacePacket(sessionId, Integer.valueOf(maxUsers), cliTime);
		MpTalker mpTalker = new MpTalker(ctx, datagramPacket, (IMainPacket)mainRacePacket);
		MpSession mpSession = MpSessions.get(mpTalker);
		if (mpSession == null) {
			mpSession = new MpSession(mpTalker);
		} 
		mpSession.put(mpTalker);
		MpSessions.put(mpSession);
		
		if (mpSession.isFull()) {
			Map<Integer, MpTalker> mpTalkers = mpSession.getMpTalkers();
			Iterator<Map.Entry<Integer, MpTalker>> iterator = mpTalkers.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, MpTalker> next = iterator.next();
				MpTalker mpTalkerTmp = next.getValue();
				mpTalkerTmp.send(answer(mpTalkerTmp));
			} 
		}
		MpAllTalkers.put(mpTalker);
    } 
	super.channelRead(ctx, msg);
	}
  
	private boolean isHelloPacket(ByteBuf buf) {
		return (buf.getByte(0) == 0 && buf.getByte(3) == 6);
	}
  
	private boolean isTicketOk(ByteBuf buf) {
		String sysPasswd = System.getProperty("password");
		if (sysPasswd == null) {
			return true; 
		}
			
	byte[] bytes = ByteBufUtil.getBytes(buf, 52, 16);
	String keyFile = UdpDebug.byteArrayToHexString(bytes).replaceAll(":", "");
	File file = new File("keys/" + keyFile);
	if (file.isFile()) {
		file.delete();
		return true;
	} 
	return false;
	}
  
	private byte[] answer(MpTalker mpTalker) {
		MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
    	SrvPktHello srvPktHello = new SrvPktHello();
    	SrvPkt srvPkt = mainPacket.getSrvPkt();
    	srvPkt.setHelloCliTime(mainPacket.getCliTime());
    	srvPkt.setSrvPkt((ISrvPkt)srvPktHello);
    	return mpTalker.getMainPacket((IPkt)srvPkt);
	}
  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
	}
}
