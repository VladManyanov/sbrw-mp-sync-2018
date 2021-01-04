/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.parser;

import io.netty.buffer.ByteBuf;

public interface IParser {
	void parseInputData(ByteBuf paramByteBuf);
}
