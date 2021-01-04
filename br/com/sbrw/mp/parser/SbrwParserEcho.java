/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.parser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.nio.ByteBuffer;

public class SbrwParserEcho implements IParser {
	private byte[] header;
	private byte[] playerInfo;
	private byte[] carState;
	private byte[] crc;
  
	public void parseInputData(byte[] inputData) {
		byte[] fullPacket = (byte[])inputData.clone();
		this.header = new byte[10];
		System.arraycopy(fullPacket, 0, this.header, 0, 10);
		this.crc = new byte[4];
		System.arraycopy(fullPacket, fullPacket.length - 5, this.crc, 0, 4);
		int subPacketStart = 10;
		int count = 0;
		while (fullPacket[subPacketStart] != -1 && count < 4) {
			int supPacketLengh = fullPacket[subPacketStart + 1] + 2;
			supPacketLengh = fullPacket[subPacketStart + 1] + 2;
			if (fullPacket[subPacketStart] == 2) {
				this.playerInfo = new byte[supPacketLengh];
				System.arraycopy(fullPacket, subPacketStart, this.playerInfo, 0, supPacketLengh);
				subPacketStart += supPacketLengh;
			} 
			supPacketLengh = fullPacket[subPacketStart + 1] + 2;
			if (fullPacket[subPacketStart] == 18) {
				this.carState = new byte[supPacketLengh];
				System.arraycopy(fullPacket, subPacketStart, this.carState, 0, supPacketLengh);
				subPacketStart += supPacketLengh;
			} 
			count++;
		} 
		fullPacket = null;
	}
  
	public byte[] getHeader() {
		return this.header;
	}

	public byte[] getPlayerInfo() {
		return this.playerInfo;
	}
  
	public byte[] getCarState() {
		return this.carState;
	}
  
	public byte[] getCrc() {
		return this.crc;
	}
  
	public boolean isOk() {
		if (this.playerInfo == null || this.carState == null) {
			return false; 
		}
		return true;
	}
  
	public boolean isCarStateOk() {
		if (this.carState == null) {
			return false; 
		}
		return true;
	}
  
	public byte[] getPlayerPacket(long timeDiff) {
		if (isOk()) {
			byte[] statePosPacket = getStatePosPacket(timeDiff);
			int bufferSize = this.playerInfo.length + statePosPacket.length;
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
			byteBuffer.put(this.playerInfo);
			byteBuffer.put(statePosPacket);
			byte[] array = byteBuffer.array();
			statePosPacket = null;
			return array;
		} 
		return null;
	}
  
	public byte[] getCarStatePacket(long timeDiff) {
		if (isOk()) {
			byte[] statePosPacket = getStatePosPacket(timeDiff);
			int bufferSize = statePosPacket.length;
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
			byteBuffer.put(this.carState);
			byte[] array = byteBuffer.array();
			statePosPacket = null;
			return array;
		} 
		return null;
	}
  
	public byte[] getStatePosPacket(long timeDiff) {
		if (isOk()) {
			byte[] clone = (byte[])this.carState.clone();
			byte[] timeDiffBytes = ByteBuffer.allocate(2).putShort((short)(int)timeDiff).array();
			clone[2] = timeDiffBytes[0];
			clone[3] = timeDiffBytes[1];
			int bufferSize = clone.length;
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
			byteBuffer.put(clone);
			byte[] array = byteBuffer.array();
			clone = null;
			timeDiffBytes = null;
			return array;
		} 
		return null;
	}
  
	public String getName() {
		if (isOk()) {
			byte[] playerPacket = getPlayerPacket(50000L);
			byte[] playerName = new byte[15];
			System.arraycopy(playerPacket, 39, playerName, 0, 15);
			String playerNameStr = (new String(playerName)).trim();
			playerPacket = null;
			playerName = null;
			return playerNameStr;
		} 
		return null;
	}
  
	public void parseInputData(ByteBuf buf) {
		parseInputData(ByteBufUtil.getBytes(buf));
	}
}
