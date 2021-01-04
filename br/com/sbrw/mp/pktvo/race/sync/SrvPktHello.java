/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.sync;

public class SrvPktHello implements ISrvPkt {
	public byte getSrvPktType() {
		return 1;
	}
  
	public byte[] getPacket() {
		return new byte[0];
	}
}
