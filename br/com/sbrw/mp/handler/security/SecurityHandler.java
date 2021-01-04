/*
 * This file is part of the Soapbox Race World MP source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Original source code by Nilzao, 2018
 */

package br.com.sbrw.mp.handler.security;

import br.com.sbrw.mp.util.UdpDebug;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import java.io.File;
import java.net.InetSocketAddress;

public class SecurityHandler extends ChannelInboundHandlerAdapter {
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket)msg;
		ByteBuf buf = (ByteBuf)datagramPacket.content();
		byte[] packetData = "1".getBytes();
		if (isSecurityPacket(buf) && isPasswordOk(buf)) {
			byte[] bytes = ByteBufUtil.getBytes(buf, 9, 16);
			String fileName = UdpDebug.byteArrayToHexString(bytes).replaceAll(":", "");
			File file = new File("keys/" + fileName);
			file.createNewFile();
			packetData = "0".getBytes();
		} 
		ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(packetData), (InetSocketAddress)datagramPacket.sender()));
		super.channelRead(ctx, msg);
	}
  
	private boolean isSecurityPacket(ByteBuf buf) {
		return (buf.getByte(0) == -103);
	}
  
	private boolean isPasswordOk(ByteBuf buf) {
		String sysPasswd = System.getProperty("password");
		if (sysPasswd == null)
			return true; 
		byte[] bytes = ByteBufUtil.getBytes(buf, 1, 8);
		String udpPasswd = new String(bytes);
		if (sysPasswd.equals(udpPasswd)) {
			return true; 
		}
		return false;
	}
  
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
	}
}
