/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.util;

public class UdpDebug {
	public static byte[] hexStringToByteArray(String s) {
		s = s.replace(":", "");
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
			data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16)); 
		return data;
	}
  
	public static String byteArrayToHexString(byte[] b) {
		int len = b.length;
		String data = new String();
		for (int i = 0; i < len; i++) {
			data = data + Integer.toHexString(b[i] >> 4 & 0xF);
			data = data + Integer.toHexString(b[i] & 0xF);
			data = data + ":";
		} 
		return data;
	}
}
