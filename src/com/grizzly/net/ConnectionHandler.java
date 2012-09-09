package com.grizzly.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.grizzly.Grizzly;

import com.grizzly.events.EventRequest;

/*
 * From Crowley
 * Thanks Jordan
 */

public class ConnectionHandler extends SimpleChannelHandler 
{
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception 
    {
        if (e instanceof ChannelStateEvent) 
        {
            Grizzly.GrabHabboHotel().GrabSessionHandler().CreateSession(ctx.getChannel());
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) 
    {
        if (e instanceof ChannelStateEvent) 
        {
        	Grizzly.GrabHabboHotel().GrabSessionHandler().KillSession(ctx.getChannel());
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) 
    {
        try 
        {
        	Grizzly.GrabConnection().GrabEventHandler().handleRequest(
					Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSession(ctx.getChannel()), (EventRequest)e.getMessage());
		} 
        catch (Exception e1) 
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) 
    {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
