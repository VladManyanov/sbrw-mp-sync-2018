/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.pktvo.string;

import br.com.sbrw.mp.pktvo.IPkt;
import br.com.sbrw.mp.protocol.MpTalker;
import java.util.Date;

public class StrHelloPkt implements IPkt {
	private long sessionTimeStart;
	private MpTalker mpTalker;
  
	public StrHelloPkt(long sessionTimeStart, MpTalker mpTalker) {
		this.sessionTimeStart = sessionTimeStart;
		this.mpTalker = mpTalker;
	}
  
	public byte[] getPacket() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Welcome!\n sessionTimeStart@");
		stringBuilder.append(this.sessionTimeStart);
		stringBuilder.append(" mpTimeStart@");
		stringBuilder.append(this.mpTalker.getMpTalkerTimerStart());
		stringBuilder.append(" now@");
		long now = (new Date()).getTime();
		stringBuilder.append(now);
		stringBuilder.append("\n timediff[");
		stringBuilder.append(now - this.mpTalker.getMpTalkerTimerStart());
		stringBuilder.append("]\n");
		return stringBuilder.toString().getBytes();
	}
}
