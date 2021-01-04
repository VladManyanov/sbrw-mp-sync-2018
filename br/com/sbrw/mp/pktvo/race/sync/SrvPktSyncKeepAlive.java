/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.sync;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.pktvo.race.ByteBufUtil;
import java.nio.ByteBuffer;

public class SrvPktSyncKeepAlive implements IPkt {
	public byte[] getPacket() {
		ByteBuffer bytebuff = ByteBuffer.allocate(1);
		bytebuff.put((byte)-1);
		return ByteBufUtil.getByteBuffArray(bytebuff);
	}
}
