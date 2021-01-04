/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.player;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.ByteBufUtil;
import java.nio.ByteBuffer;

public class PlayerMainPkt implements IPkt {
	private byte playerIdx = 0;
	private short counter = 0;
	private short unknownCounter = -1;
	private boolean counterRunning = false;
	private boolean allInfo = true;
	private PlayerInfo playerInfo;
  
	public byte[] getPacket() {
		ByteBuffer bytebuff = ByteBuffer.allocate(2048);
		bytebuff.put((byte)1);
		bytebuff.put(this.playerIdx);
		this.counter = (short)(this.counter + 1);
		bytebuff.putShort(this.counter);
		if (this.counterRunning) {
			this.unknownCounter = (short)(this.unknownCounter + 1);
			bytebuff.putShort(this.unknownCounter);
		} else {
			bytebuff.putShort(this.unknownCounter);
		} 
		bytebuff.put((byte)-1);
		bytebuff.put((byte)-1);
		if (this.allInfo) {
			bytebuff.put(this.playerInfo.getPlayerInfo());
		} else {
			bytebuff.put(this.playerInfo.getPlayerStatePos());
		} 
		bytebuff.put((byte)-1);
		return ByteBufUtil.getByteBuffArray(bytebuff);
	}
  
	public void enableUnknownCounter() {
		this.counterRunning = true;
		this.unknownCounter = 1;
	}
  
	public void enableAllInfo() {
		this.allInfo = true;
	}
  
	public void disableAllInfo() {
		this.allInfo = false;
	}
  
	public void setPlayerInfo(PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}
  
	public void setPlayerIdx(byte playerIdx) {
		this.playerIdx = playerIdx;
	}
}
