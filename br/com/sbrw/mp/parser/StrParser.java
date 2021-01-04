/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.parser;

import io.netty.buffer.ByteBuf;

public class StrParser implements IParser {
	private String playerInfo;
  
	public void parseInputData(byte[] packet) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("player: [");
		stringBuilder.append((new String(packet)).trim());
		stringBuilder.append("]\n\n");
		this.playerInfo = stringBuilder.toString();
	}
  
	public boolean isOk() {
		if (this.playerInfo != null && !this.playerInfo.isEmpty()) {
			return true; 
		}
		return false;
	}
  
	public byte[] getPlayerPacket(long timeDiff) {
		return this.playerInfo.getBytes();
	}
  
	public boolean isCarStateOk() {
		return false;
	}
  
	public byte[] getCarStatePacket(long timeDiff) {
		return null;
	}
  
	public void parseInputData(ByteBuf buf) {}
}
