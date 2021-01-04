/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.sync;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.ByteBufUtil;
import java.nio.ByteBuffer;
import java.util.Date;

public class SrvPkt implements IPkt {
	private short counter = 0;
	private short helloCliTime = 0;
	private ISrvPkt srvPkt;
  
	public void setSrvPkt(ISrvPkt srvPkt) {
		this.srvPkt = srvPkt;
	}
  
	public short getHelloCliTime() {
		return this.helloCliTime;
	}
  
	public void setHelloCliTime(short helloCliTime) {
		this.helloCliTime = helloCliTime;
	}
  
	public byte[] getPacket() {
		ByteBuffer bytebuff = ByteBuffer.allocate(128);
		bytebuff.put((byte)0);
		this.counter = (short)(this.counter + 1);
		bytebuff.putShort(this.counter);
		bytebuff.put(this.srvPkt.getSrvPktType());
		bytebuff.putShort(getTime());
		bytebuff.putShort(getHelloCliTime());
		bytebuff.put(this.srvPkt.getPacket());
		return ByteBufUtil.getByteBuffArray(bytebuff);
	}
  
	public short getTime() {
		return (short)(int)(new Date()).getTime();
	}
}
