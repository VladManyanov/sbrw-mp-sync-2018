/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.sync;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.ByteBufUtil;
import java.nio.ByteBuffer;

public class SrvPktMainSync implements ISrvPkt {
	private short counter = 1;
	private IPkt pktSync;
  
	public byte getSrvPktType() {
		return 2;
	}
  
	public byte[] getPacket() {
		ByteBuffer bytebuff = ByteBuffer.allocate(128);
		this.counter = (short)(this.counter + 1);
		bytebuff.putShort(this.counter);
		bytebuff.putShort(walkBits());
		bytebuff.put(this.pktSync.getPacket());
		return ByteBufUtil.getByteBuffArray(bytebuff);
	}
  
	private short walkBits() {
		short counterTmp = (short)(this.counter - 1);
		switch (counterTmp) {
			case 1:
				return Short.MAX_VALUE;
			case 2:
        		return -16385;
			case 3:
				return -8193;
			case 4:
				return -4097;
			case 5:
				return -2049;
			case 6:
				return -1025;
			case 7:
				return -513;
			case 8:
				return -257;
			case 9:
				return -129;
			case 10:
				return -65;
			case 11:
				return -33;
			case 12:
        		return -17;
			case 13:
				return -9;
			case 14:
				return -5;
			case 15:
				return -3;
			case 16:
				return -2;
		} 
		return -1;
	}
  
	public void setPktSync(IPkt pktSync) {
		this.pktSync = pktSync;
	}
  
	public void setCounter(short counter) {
		this.counter = counter;
	}
}
