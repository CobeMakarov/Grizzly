package com.grizzly.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.grizzly.Grizzly;
import com.grizzly.events.EventRequest;
import com.grizzly.habbohotel.sessions.Session;

public class CodecDecoder extends FrameDecoder
{

	@Override
	protected Object decode(ChannelHandlerContext Handler, Channel Channel, ChannelBuffer Buffer)
	{
		try 
		{
			if (Buffer.readableBytes() < 5)
			{
				return null;
			}
			
			int BufferIndex = Buffer.readerIndex();
			
			Session Habbo = Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSession(Channel);
			
			byte Delimeter = Buffer.readByte();
			
			if (Delimeter == 0x3C)
			{
				Buffer.clear();

				Habbo.GrabChannel().write(
	                    "<?xml version=\"1.0\"?>\r\n" +
	                            "<!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\r\n" +
	                            "<cross-domain-policy>\r\n" +
	                            "<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n" +
	                            "</cross-domain-policy>\0"
	            );
				
				Grizzly.WriteOut("Habbo Policy sent to " + Habbo.GrabIP() + " [" + Habbo.GrabID() + "]");
				
				Habbo.RecievedPolicy = true;
				
				Channel.close();
				
				return null;
			}
			else
			{
				Buffer.readerIndex(BufferIndex);
				
				int Len = (Buffer.readInt() - 2);
				
				if (!(Buffer.readableBytes() >= Len))
				{
					Buffer.readerIndex(BufferIndex);
					return null;
				}
				
				return new EventRequest(Buffer.readShort(), Buffer.readBytes(Len));
			}		
		}
		catch (Exception e)
		{
			//
		}
		return null;
	}

}
