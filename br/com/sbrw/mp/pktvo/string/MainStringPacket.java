/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.string;

import br.com.sbrw.mp.pktvo.IMainPacket;
import br.com.sbrw.mp.pktvo.IPkt;

public class MainStringPacket implements IMainPacket {
	private Integer sessionId;
	private Integer maxUsers;
  
	public MainStringPacket(Integer sessionId, Integer maxUsers) {
		this.sessionId = sessionId;
		this.maxUsers = maxUsers;
	}
  
	public byte[] getPacket(IPkt pkt) {
		return pkt.getPacket();
	}
  
	public Integer getMaxUsers() {
		return this.maxUsers;
	}
  
	public Integer getSessionId() {
		return this.sessionId;
	}
}
