package net.cobem.grizzly.net;


import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import net.cobem.grizzly.events.*;
import net.cobem.grizzly.net.codec.*;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class Connection 
{
	private NioServerSocketChannelFactory ChannelFactory;
	private ServerBootstrap Bootstrap;
	private EventHandler EventHandler;
	private int ConnectionPort;
	
	public Connection(int Port)
	{

		ChannelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		Bootstrap = new ServerBootstrap(ChannelFactory);
		ConnectionPort = Port;
		EventHandler = new EventHandler();
		ConfigurePipeline();
	}
	
	private void ConfigurePipeline()
	{
		ChannelPipeline Line = Bootstrap.getPipeline();

		Line.addLast("encoder", new CodecEncoder());
		Line.addLast("decoder", new CodecDecoder());
		Line.addLast("handler", new ConnectionHandler());
	}
	
	public boolean Listen()
	{
		try
		{
			this.Bootstrap.bind(new InetSocketAddress(ConnectionPort));

		}
		catch (ChannelException ex)
		{
			return false;
		}

		return true;
	}
	
	public EventHandler GrabEventHandler()
	{
		return EventHandler;
	}
}
