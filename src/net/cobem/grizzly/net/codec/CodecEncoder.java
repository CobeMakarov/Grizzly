package net.cobem.grizzly.net.codec;

import java.nio.charset.Charset;

import net.cobem.grizzly.events.EventResponse;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;


public class CodecEncoder extends SimpleChannelHandler
{
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
		try 
		{
			if (e.getMessage() instanceof String)
			{
				Channels.write(ctx, e.getFuture(), ChannelBuffers.copiedBuffer(
						(String) e.getMessage(), Charset.forName("UTF-8")));
			}
			else
			{
				EventResponse Message = (EventResponse) e.getMessage();
				
				Channels.write(ctx, e.getFuture(), Message.Get());
			}
		}
		catch (Exception ex) 
		{
			
		}
	}
}
