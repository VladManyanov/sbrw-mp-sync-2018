/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.protocol;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MpSession {
	private HashMap<Integer, MpTalker> mpTalkers = new HashMap<>();
	private Integer sessionId;
	private Integer maxUsers;
	private long sessionTimeStart = (new Date()).getTime();
  
	public MpSession(MpTalker mpTalker) {
		this.maxUsers = mpTalker.getMainPacket().getMaxUsers();
		this.sessionId = mpTalker.getSessionId();
		put(mpTalker);
	}
  
	public Integer getSessionId() {
		return this.sessionId;
	}
  
	public void put(MpTalker mpTalker) {
		this.mpTalkers.put(mpTalker.getPort(), mpTalker);
	}
  
	public boolean isFull() {
		return (this.mpTalkers.size() == this.maxUsers.intValue());
	}
  
	public Map<Integer, MpTalker> getMpTalkers() {
		return this.mpTalkers;
	}
  
	public long getSessionTimeStart() {
		return this.sessionTimeStart;
	}
  
	public void broadcastFrom(MpTalker mpTalker, byte[] packetData) {
		Iterator<Map.Entry<Integer, MpTalker>> iterator = this.mpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, MpTalker> next = iterator.next();
			if (!((Integer)next.getKey()).equals(mpTalker.getPort())) {
				MpTalker value = next.getValue();
				value.send(packetData);
			} 
		} 
	}
  
	public void broadcast(byte[] packetData) {
		Iterator<Map.Entry<Integer, MpTalker>> iterator = this.mpTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, MpTalker> next = iterator.next();
			MpTalker value = next.getValue();
			value.send(packetData);
			}
		}
}
