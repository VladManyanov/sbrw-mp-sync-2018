/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteBufUtil {
	public static byte[] getByteBuffArray(ByteBuffer bytebuff) {
		return Arrays.copyOfRange(bytebuff.array(), 0, bytebuff.position());
	}
}
