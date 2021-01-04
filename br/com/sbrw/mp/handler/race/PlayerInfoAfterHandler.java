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
import br.com.sbrw.mp.util.UdpDebug;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class PlayerInfoAfterHandler extends ChannelInboundHandlerAdapter {
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	DatagramPacket datagramPacket = (DatagramPacket)msg;
	ByteBuf buf = (ByteBuf)datagramPacket.content();
	if (isPlayerInfoPacket(buf)) {
		MpTalker mpTalker = MpAllTalkers.get(datagramPacket);
		if (mpTalker != null) {
			playerInfoAfterOk(mpTalker, buf); 
		}
	} 
	super.channelRead(ctx, msg);
	}
  
	private void playerInfoAfterOk(MpTalker mpTalker, ByteBuf buf) {
		MpSession mpSession = MpSessions.get(mpTalker);
		if (isAllPlayerInfoBeforeOk(mpSession)) {
			mpTalker.broadcastToSession(sendPlayerInfo(mpTalker, buf)); 
		}
	}
  
	private boolean isAllPlayerInfoBeforeOk(MpSession mpSession) {
		Map<Integer, MpTalker> mpTalkersTmp = mpSession.getMpTalkers();
		Iterator<Map.Entry<Integer, MpTalker>> iterator = mpTalkersTmp.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, MpTalker> next = iterator.next();
			MpTalker mpTalker = next.getValue();
			MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
		if (!mainPacket.isPlayerInfoOk()) {
			return false; 
			}
		} 
		return true;
	}
  
	private byte[] sendPlayerInfo(MpTalker mpTalker, ByteBuf buf) {
		MainRacePacket mainPacket = (MainRacePacket)mpTalker.getMainPacket();
		PlayerMainPkt playerMainPkt = mainPacket.getPlayerMainPkt();
		playerMainPkt.disableAllInfo();
		playerMainPkt.enableUnknownCounter();
		PlayerInfo playerInfo = new PlayerInfo();
		byte[] bytesTmp = ByteBufUtil.getBytes(buf);
		int limit = bytesTmp.length;
		limit -= 6;
		byte[] copyOfRange = Arrays.copyOfRange(bytesTmp, 10, limit);
		playerInfo.setPlayerStatePos(copyOfRange);
		bytesTmp = null;
		copyOfRange = null;
		playerMainPkt.setPlayerInfo(playerInfo);
		return mpTalker.getMainPacket((IPkt)playerMainPkt);
	}
  
	private boolean isPlayerInfoPacket(ByteBuf buf) {
		return (buf.getByte(0) == 1);
	}
  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
	}
  
	public static void main(String[] args) {
		byte[] fullPacket = UdpDebug.hexStringToByteArray("01:00:00:6e:00:07:00:05:ff:ff:10:45:00:01:00:00:00:09:00:00:00:a4:32:c1:1c:65:00:00:00:00:00:00:00:00:00:80:40:ac:fa:18:00:a0:56:20:1d:bc:fa:18:00:ac:a0:0f:1d:90:18:16:1d:80:41:68:00:00:00:00:00:d8:fa:18:00:30:3d:69:00:80:41:68:00:a4:e4:7c:22:12:1a:a4:e5:98:08:72:36:d0:e5:9b:e9:c4:25:89:c4:1f:3e:fb:f9:d3:96:96:96:9b:34:08:1f:ff:aa:56:c9:3c:ff:");
		int subPacketStart = 10;
		int count = 0;
		byte nextType = -1;
		while (fullPacket[subPacketStart] != -1 && count < 4) {
			int supPacketLengh = fullPacket[subPacketStart + 1] + 2;
			subPacketStart += supPacketLengh;
			nextType = fullPacket[subPacketStart];
			if ((nextType == 18 || nextType == 16 || nextType != 2) && nextType == 18) {
				System.out.println("eh 12"); 
			}
			System.out.println("loop");
			count++;
			} 
		}
}
