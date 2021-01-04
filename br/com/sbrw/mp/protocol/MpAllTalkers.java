/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.protocol;

import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class MpAllTalkers {
	private static HashMap<Integer, MpTalker> mpTalkers = new HashMap<>();
  
	public static void put(MpTalker mpTalker) {
		mpTalkers.put(mpTalker.getPort(), mpTalker);
	}
  
	public static MpTalker get(DatagramPacket datagramPacket) {
		return mpTalkers.get(Integer.valueOf(((InetSocketAddress)datagramPacket.sender()).getPort()));
	}
  
	public static Map<Integer, MpTalker> getMpTalkers() {
		return mpTalkers;
	}
}
