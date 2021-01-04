/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.protocol;

import java.util.HashMap;

public class MpSessions {
	private static HashMap<Integer, MpSession> sessions = new HashMap<>();
  
	public static void put(MpSession mpSession) {
		sessions.put(mpSession.getSessionId(), mpSession);
	}
  
	public static MpSession get(MpTalker mpTalker) {
		return sessions.get(mpTalker.getSessionId());
	}
}
