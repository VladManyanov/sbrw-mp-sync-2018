/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.parser;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.BitSet;

public class SbrwParser {
	private byte[] header;
	private byte[] channelInfo;
	private byte[] playerInfo;
	private byte[] carState;
	private byte[] crc;
	private int lastSeq = 0;
	private int lastTime = 0;
  
	public SbrwParser(byte[] inputData) {
		parseInputData(inputData);
	}
  
	public boolean parseInputData(byte[] inputData) {
		byte[] fullPacket = (byte[])inputData.clone();
		boolean good2go = false;
		try {
			this.header = new byte[16];
			System.arraycopy(fullPacket, 0, this.header, 0, 16);
			this.crc = new byte[4];
			System.arraycopy(fullPacket, fullPacket.length - 4, this.crc, 0, 4);
			byte[] seqBytes = { this.header[0], this.header[1] };
			byte[] timeBytes = { this.header[3], this.header[4] };
			int seq = (new BigInteger(seqBytes)).intValue(), time = (new BigInteger(timeBytes)).intValue();
			if (seq == -1) {
				System.out.println("Reached seq limit, resetting");
				seq = 0;
				this.lastSeq = 0;
			} 
			if (seq < this.lastSeq && time < this.lastTime) {
				System.err.println("[packet] out-of-order seq/time: " + seq + "/" + time);
			} else {
				good2go = true;
			} 
			
			this.lastSeq = seq;
			this.lastTime = time;
			int subPacketStart = 16;
			int count = 0;
			while (fullPacket[subPacketStart] != -1 && count < 4) {
			int supPacketLengh = fullPacket[subPacketStart + 1] + 2;
			if (fullPacket[subPacketStart] == 0) {
				this.channelInfo = new byte[supPacketLengh];
				System.arraycopy(fullPacket, subPacketStart, this.channelInfo, 0, supPacketLengh);
				subPacketStart += supPacketLengh;
			} 
			supPacketLengh = fullPacket[subPacketStart + 1] + 2;
			if (fullPacket[subPacketStart] == 1) {
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
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} 
		return good2go;
	}
  
	public byte[] getHeader() {
		return this.header;
	}
  
	public byte[] getChannelInfo() {
		return this.channelInfo;
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
  
	public int getTime() {
		if (isOk()) {
			return (new BigInteger(new byte[] { this.header[3], this.header[4] })).intValue(); 
		}
		return -1;
	}
  
	public boolean isOk() {
		if (this.channelInfo == null || this.playerInfo == null || this.carState == null) {
			return false; 
		}
		return true;
	}
  
	public byte[] getPlayerPacket(long timeDiff) {
		if (isOk()) {
			byte[] statePosPacket = getStatePosPacket(timeDiff);
			int bufferSize = this.channelInfo.length + this.playerInfo.length + statePosPacket.length;
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
			byteBuffer.put(this.channelInfo);
			byteBuffer.put(this.playerInfo);
			byteBuffer.put(statePosPacket);
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
  
	public String getChannel() {
		if (isOk()) {
			byte[] channelName = new byte[15];
			System.arraycopy(this.channelInfo, 2, channelName, 0, 15);
			String channelStr = (new String(channelName)).trim();
			channelName = null;
			return channelStr;
		} 
		return null;
	}
  
	public String getName() {
		if (isOk()) {
			byte[] playerName = new byte[15];
			System.arraycopy(this.playerInfo, 3, playerName, 0, 15);
			String playerNameStr = (new String(playerName)).trim();
			playerName = null;
			return playerNameStr;
		} 
		return null;
	}
  
	public long getPlayerId() {
		if (isOk()) {
			long personaId = 0L;
			for (int i = 43; i <= 46 && this.playerInfo[i] != 0; i++)
        	personaId += this.playerInfo[i]; 
			return personaId;
		} 
		return -1L;
	}
  
	public int getXPos() {
		if (isOk()) {
			byte[] byteTmp = { this.carState[9], this.carState[10], this.carState[11] };
			return (new BigInteger(cleanX(byteTmp))).intValue();
		} 
		return -1;
	}
  
	public int getYPos() {
		if (isOk()) {
			byte[] byteTmp = { this.carState[5], this.carState[6], this.carState[7] };
			return (new BigInteger(cleanY(byteTmp))).intValue();
		} 
		return -1;
	}
  
	public int getZPos() {
		if (isOk()) {
			byte[] byteTmp = { this.carState[7], this.carState[8], this.carState[9] };
			return (new BigInteger(cleanZ(byteTmp))).intValue();
		} 
		return -1;
	}
  
	private byte[] cleanX(byte[] byteOrig) {
		byte[] byteTmp = (byte[])byteOrig.clone();
		if (isLowerY()) {
			byteTmp[0] = (byte)(byteTmp[0] & Byte.MAX_VALUE);
			byteTmp[2] = (byte)(byteTmp[2] & 0xE0);
			return shiftRight(byteTmp, 5);
		} 
		byteTmp[0] = (byte)(byteTmp[0] & 0x3F);
		byteTmp[2] = (byte)(byteTmp[2] & 0xF0);
		return shiftRight(byteTmp, 4);
	}
  
	private byte[] cleanY(byte[] byteOrig) {
		byte[] byteTmp = (byte[])byteOrig.clone();
		if (isLowerY()) {
			byteTmp[2] = (byte)(byteTmp[2] & 0xF8);
		} else {
			byteTmp[2] = (byte)(byteTmp[2] & 0xFC);
		} 
		return shiftRight(byteTmp, 2);
	}
  
	private byte[] cleanZ(byte[] byteOrig) {
		byte[] byteTmp = (byte[])byteOrig.clone();
		if (isLowerY()) {
			byteTmp[0] = (byte)(byteTmp[0] & 0x7);
			byteTmp[2] = (byte)(byteTmp[2] & 0x80);
			return shiftRight(byteTmp, 7);
		} 
		byteTmp[0] = (byte)(byteTmp[0] & 0x3);
		byteTmp[2] = (byte)(byteTmp[2] & 0xC0);
		return shiftRight(byteTmp, 6);
	}
  
	private boolean isLowerY() {
		int intValue = (new BigInteger(new byte[] { this.carState[5], this.carState[6] })).intValue();
		if (intValue <= 1941)
			return true; 
		return false;
	}
  
	private byte[] shiftRight(byte[] byteOrig, int n) {
		byte bitMask = bitMask(8 - n);
		ByteBuffer allocate = ByteBuffer.allocate(byteOrig.length);
		for (int i = 0; i < byteOrig.length; i++) {
			byte byteTmp = (byte)(byteOrig[i] >> n & bitMask);
			if (i > 0) {
				byteTmp = (byte)(byteOrig[i - 1] << 8 - n | byteTmp); 
			}
			allocate.put(byteTmp);
		} 
		return allocate.array();
	}
  
	private byte bitMask(int n) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < n; i++) {
			bitSet.set(i); 
		}
		return bitSet.toByteArray()[0];
	}
}
