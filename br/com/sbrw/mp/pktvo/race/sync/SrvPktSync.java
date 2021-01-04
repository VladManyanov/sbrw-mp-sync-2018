/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.sync;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.ByteBufUtil;
import java.nio.ByteBuffer;

public class SrvPktSync implements IPkt {
	public byte[] getPacket() {
		ByteBuffer bytebuff = ByteBuffer.allocate(20);
		bytebuff.put((byte)1);
		bytebuff.put((byte)3);
		bytebuff.put((byte)0);
		bytebuff.put((byte)64);
		bytebuff.put((byte)-73);
		bytebuff.put((byte)-1);
		return ByteBufUtil.getByteBuffArray(bytebuff);
	}
}
