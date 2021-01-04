/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.race.player;

public class PlayerInfo {
	private byte[] playerInfo;
	private byte[] playerStatePos;
  
	public byte[] getPlayerInfo() {
		return this.playerInfo;
	}
  
	public void setPlayerInfo(byte[] playerInfo) {
		this.playerInfo = playerInfo;
	}
  
	public byte[] getPlayerStatePos() {
		return this.playerStatePos;
	}
  
	public void setPlayerStatePos(byte[] playerStatePos) {
		this.playerStatePos = playerStatePos;
	}
}
