/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.sync;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.ByteBufUtil;
import java.nio.ByteBuffer;

public class SrvPktSyncStart implements IPkt {
	private byte playerIdx = 0;
	private int sessionId;
	private int maxPlayers = 2;
  
	public byte[] getPacket() {
		ByteBuffer bytebuff = ByteBuffer.allocate(20);
		bytebuff.put((byte)0);
		bytebuff.put((byte)6);
		bytebuff.put(this.playerIdx);
		bytebuff.putInt(this.sessionId);
		bytebuff.put(getMaxUsersBits());
		bytebuff.put((byte)-1);
		return ByteBufUtil.getByteBuffArray(bytebuff);
	}
  
	private byte getMaxUsersBits() {
		switch (this.maxPlayers) {
			case 2:
				return 3;
			case 3:
				return 7;
			case 4:
				return 15;
			case 5:
				return 31;
			case 6:
				return 63;
			case 7:
				return Byte.MAX_VALUE;
			case 8:
				return -1;
		} 
		return -1;
	}
  
	public void setPlayerIdx(byte playerIdx) {
		this.playerIdx = playerIdx;
	}
  
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
  
	public void setMaxPlayers(byte maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
  
	private static byte generateSlotsBits(int numberOfPlayers) {
		byte res = 0;
		for (int i = 0; i < numberOfPlayers; i++) {
			res = (byte)(res | 1 << i); 
		}
		return res;
	}
}
