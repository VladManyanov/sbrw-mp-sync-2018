/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race;

import br.com.sbrw.mp.parser.SbrwParserEcho;
import br.com.sbrw.mp.pktvo.IMainPacket;
import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.player.PlayerInfo;
import br.com.sbrw.mp.pktvo.race.player.PlayerMainPkt;
import br.com.sbrw.mp.pktvo.race.sync.ISrvPkt;
import br.com.sbrw.mp.pktvo.race.sync.SrvPkt;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktHello;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktMainSync;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktSync;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktSyncKeepAlive;
import br.com.sbrw.mp.pktvo.race.sync.SrvPktSyncStart;
import br.com.sbrw.mp.util.UdpDebug;
import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;

public class MainRacePacket implements IMainPacket {
	private Integer sessionId;
	private Integer maxUsers;
	private short cliTime;
	private SrvPkt srvPkt = new SrvPkt();
	private SrvPktMainSync srvPktMainSync = new SrvPktMainSync();
	private PlayerMainPkt playerMainPkt = new PlayerMainPkt();
	private ByteBuf helloSyncBuf;
	private byte[] crc = new byte[] { 1, 1, 1, 1 };
	private boolean syncHelloOk = false;
	private SbrwParserEcho sbrwParser = new SbrwParserEcho();
  
	public MainRacePacket(Integer sessionId, Integer maxUsers, short cliTime) {
		this.sessionId = sessionId;
		this.maxUsers = maxUsers;
		this.cliTime = cliTime;
	}
  
	public byte[] getPacket(IPkt pkt) {
		ByteBuffer bytebuff = ByteBuffer.allocate(1024);
		bytebuff.put(pkt.getPacket());
		bytebuff.put(this.crc);
		return ByteBufUtil.getByteBuffArray(bytebuff);
	}
  
	public Integer getMaxUsers() {
		return this.maxUsers;
	}
  
	public Integer getSessionId() {
		return this.sessionId;
	}
  
	public SrvPkt getSrvPkt() {
		return this.srvPkt;
	}
  
	public SrvPktMainSync getSrvPktMainSync() {
		return this.srvPktMainSync;
	}
  
	public PlayerMainPkt getPlayerMainPkt() {
		return this.playerMainPkt;
	}
  
	public ByteBuf getHelloSyncBuf() {
		return this.helloSyncBuf;
	}
  
	public void setHelloSyncBuf(ByteBuf helloSyncBuf) {
		this.syncHelloOk = true;
		this.helloSyncBuf = helloSyncBuf;
	}
  
	public void parsePlayerInfo(ByteBuf playerInfoBuf) {
		this.sbrwParser.parseInputData(playerInfoBuf);
	}
  
	public short getCliTime() {
		return this.cliTime;
	}
  
	public boolean isSyncHelloOk() {
		return this.syncHelloOk;
	}
  
	public boolean isPlayerInfoOk() {
		return this.sbrwParser.isOk();
	}
  
	public SbrwParserEcho getSbrwParser() {
		return this.sbrwParser;
	}
  
	public static void main(String[] args) {
		MainRacePacket mainPacket = new MainRacePacket(Integer.valueOf(1), Integer.valueOf(2), (short)0);
		SrvPkt srvPkt = new SrvPkt();
		SrvPktHello srvPktHello = new SrvPktHello();
		srvPkt.setSrvPkt((ISrvPkt)srvPktHello);
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		SrvPktMainSync srvPktMainSync = new SrvPktMainSync();
		SrvPktSyncStart srvPktSyncStart = new SrvPktSyncStart();
		srvPktSyncStart.setPlayerIdx((byte)102);
		srvPktSyncStart.setSessionId(99999);
		srvPktMainSync.setPktSync((IPkt)srvPktSyncStart);
		srvPkt.setSrvPkt((ISrvPkt)srvPktMainSync);
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		SrvPktSync srvPktSync = new SrvPktSync();
		srvPktMainSync.setPktSync((IPkt)srvPktSync);
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		SrvPktSyncKeepAlive srvPktSyncKeepAlive = new SrvPktSyncKeepAlive();
		srvPktMainSync.setPktSync((IPkt)srvPktSyncKeepAlive);
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)srvPkt)));
		PlayerMainPkt playerMainPkt = new PlayerMainPkt();
		PlayerInfo playerInfo = new PlayerInfo();
		playerMainPkt.setPlayerInfo(playerInfo);
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		playerMainPkt.enableUnknownCounter();
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		playerMainPkt.disableAllInfo();
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
		System.out.println(UdpDebug.byteArrayToHexString(mainPacket.getPacket((IPkt)playerMainPkt)));
	}
}
